package org.aper.web.domain.chat.service;

import org.aper.web.domain.chat.factory.ChatRoomFactory;
import org.aper.web.domain.chat.policy.ChatRoomPolicy;
import org.aper.web.domain.chat.policy.UserPolicy;
import org.aper.web.domain.chat.query.UnreadCountCalculator;
import org.aper.web.domain.chat.dto.ChatRequestDto.CreateChatRoomRequestDto;
import org.aper.web.domain.chat.dto.ChatResponseDto.ChatRoomResponseDto;
import org.aper.web.domain.chat.dto.ChatResponseDto.CreatedChatRoomResponseDto;
import org.aper.web.entity.chat.*;
import org.aper.web.entity.user.entity.User;
import org.aper.web.domain.chat.repository.ChatRoomMemberRepository;
import org.aper.web.domain.chat.repository.ChatRoomRepository;
import org.aper.web.domain.chat.repository.MessageRepository;
import org.aper.web.domain.chat.repository.UserReadTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MessageRepository messageRepository;
    private final UserReadTrackingRepository userReadTrackingRepository;

    private final UnreadCountCalculator unreadCountCalculator;
    private final UserPolicy userPolicy;
    private final ChatRoomFactory chatRoomFactory;
    private final ChatRoomPolicy chatRoomPolicy;
    // TODO: Redis 캐시 적용

    @Transactional
    public CreatedChatRoomResponseDto createChatRoom(CreateChatRoomRequestDto request, Long userId) {
        User creator = userPolicy.validateUserExists(userId);
        ChatRoom chatRoom = chatRoomFactory.create(request);
        chatRoom = chatRoomRepository.save(chatRoom);

        addMember(chatRoom, creator, true);

        for (Long memberId : request.memberIds()) {
            User member = userPolicy.validateUserExists(memberId);
            addMember(chatRoom, member, false);
        }

        return CreatedChatRoomResponseDto.from(chatRoom);
    }

    private void addMember(ChatRoom chatRoom, User user, boolean isOwner) {
        ChatRoomMember member = ChatRoomMember.create(chatRoom, user, isOwner);
        chatRoomMemberRepository.save(member);
    }

    // 사용자의 채팅방 목록 조회 - 최근 메시지 시간순, 읽지 않은 메시지 수 포함
    // TODO: n+1 문제 생각 필요
    public List<ChatRoomResponseDto> getChatRoomsForUser(Long userId) {
        userPolicy.validateUserExists(userId);

        List<ChatRoom> chatRooms = chatRoomRepository.findRecentChatRooms(userId);
        List<ChatRoomResponseDto> chatRoomResponses = new ArrayList<>();

        for (ChatRoom chatRoom : chatRooms) {
            Message message = messageRepository.findLatestMessage(chatRoom);

            UserReadTracking tracking = userReadTrackingRepository.findByUserUserIdAndChatRoom(userId, chatRoom);
            Integer unreadCount = unreadCountCalculator.calculate(chatRoom.getId(), tracking, message);

            ChatRoomResponseDto chatRoomResponse = ChatRoomResponseDto.from(chatRoom, message, unreadCount);
            chatRoomResponses.add(chatRoomResponse);
        }

        return chatRoomResponses;
    }


    // 채팅방의 마지막 시간 업데이트
    @Transactional
    public void updateLastMessageAt(Long chatRoomId, LocalDateTime messageTime) {
        chatRoomRepository.updateLastMessageAt(chatRoomId, messageTime);
    }

    @Transactional
    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomPolicy.validateChatRoomExists(chatRoomId);
        chatRoom.delete();

        chatRoomRepository.save(chatRoom);
    }
}
