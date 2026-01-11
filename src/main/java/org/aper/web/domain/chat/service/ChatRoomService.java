package org.aper.web.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.chat.document.ChatRoomSummaryDocument;
import org.aper.web.domain.chat.document.UserReadTrackingDocument;
import org.aper.web.domain.chat.dto.ChatRequestDto.CreateChatRoomRequestDto;
import org.aper.web.domain.chat.dto.ChatResponseDto.ChatRoomResponseDto;
import org.aper.web.domain.chat.dto.ChatResponseDto.CreatedChatRoomResponseDto;
import org.aper.web.domain.chat.entity.ChatRoom;
import org.aper.web.domain.chat.entity.ChatRoomMember;
import org.aper.web.domain.chat.factory.ChatRoomFactory;
import org.aper.web.domain.chat.policy.ChatRoomPolicy;
import org.aper.web.domain.chat.policy.UserPolicy;
import org.aper.web.domain.chat.repository.ChatRoomMemberRepository;
import org.aper.web.domain.chat.repository.ChatRoomRepository;
import org.aper.web.domain.chat.repository.ChatRoomSummaryDocumentRepository;
import org.aper.web.domain.chat.repository.UserReadTrackingDocumentRepository;
import org.aper.web.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserReadTrackingDocumentRepository userReadTrackingDocumentRepository;
    private final ChatRoomSummaryDocumentRepository chatRoomSummaryDocumentRepository;

    private final UserPolicy userPolicy;
    private final ChatRoomFactory chatRoomFactory;
    private final ChatRoomPolicy chatRoomPolicy;

    @Transactional
    public CreatedChatRoomResponseDto createChatRoom(CreateChatRoomRequestDto request, Long userId) {
        User creator = userPolicy.validateUserExists(userId);
        ChatRoom chatRoom = chatRoomFactory.create(request);
        chatRoom = chatRoomRepository.save(chatRoom);

        // MongoDB에 Summary 생성
        ChatRoomSummaryDocument summary = ChatRoomSummaryDocument.builder()
                .chatRoomId(chatRoom.getId())
                .currentSequence(0L)
                .updatedAt(LocalDateTime.now())
                .build();
        chatRoomSummaryDocumentRepository.save(summary);

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

    /**
     * 사용자의 채팅방 목록 조회 - MongoDB 전용, N+1 해결
     *
     * 쿼리 수: 1 (MySQL) + 2 (MongoDB) = 3개
     * 안읽은 메시지 개수는 시퀀스 차이로 메모리 계산
     */
    public Slice<ChatRoomResponseDto> getChatRoomsForUser(Long userId, Pageable pageable) {
        userPolicy.validateUserExists(userId);

        // 1. MySQL에서 채팅방 목록 조회 (1 쿼리)
        Slice<ChatRoom> chatRooms = chatRoomRepository.findRecentChatRooms(userId, pageable);
        List<ChatRoom> chatRoomList = chatRooms.getContent();

        if (chatRoomList.isEmpty()) {
            return new SliceImpl<>(List.of(), pageable, false);
        }

        // 2. 채팅방 ID 리스트 추출
        List<Long> chatRoomIds = chatRoomList.stream()
                .map(ChatRoom::getId)
                .toList();

        // 3. MongoDB에서 채팅방 요약 정보 조회 (1 쿼리)
        Map<Long, ChatRoomSummaryDocument> summaryMap = chatRoomSummaryDocumentRepository
                .findByChatRoomIdIn(chatRoomIds)
                .stream()
                .collect(Collectors.toMap(
                        ChatRoomSummaryDocument::getChatRoomId,
                        s -> s
                ));

        // 4. MongoDB에서 읽기 추적 정보 조회 (1 쿼리)
        Map<Long, UserReadTrackingDocument> trackingMap = userReadTrackingDocumentRepository
                .findByUserIdAndChatRoomIdIn(userId, chatRoomIds)
                .stream()
                .collect(Collectors.toMap(
                        UserReadTrackingDocument::getChatRoomId,
                        t -> t
                ));

        // 5. 안읽은 개수 계산 (메모리 연산 - 시퀀스 차이)
        Map<Long, Integer> unreadCountMap = new HashMap<>();
        for (Long chatRoomId : chatRoomIds) {
            ChatRoomSummaryDocument summary = summaryMap.get(chatRoomId);
            UserReadTrackingDocument tracking = trackingMap.get(chatRoomId);

            if (summary == null || summary.getCurrentSequence() == null) {
                unreadCountMap.put(chatRoomId, 0);
                continue;
            }

            // 시퀀스 기반 계산 (COUNT 쿼리 불필요)
            Long lastSequence = summary.getCurrentSequence();
            Long readSequence = tracking != null && tracking.getLastReadSequence() != null
                    ? tracking.getLastReadSequence()
                    : 0L;
            Integer unreadCount = (int) (lastSequence - readSequence);

            unreadCountMap.put(chatRoomId, Math.max(0, unreadCount));
        }

        // 6. DTO 생성
        List<ChatRoomResponseDto> content = chatRoomList.stream()
                .map(chatRoom -> {
                    ChatRoomSummaryDocument summary = summaryMap.get(chatRoom.getId());
                    Integer unreadCount = unreadCountMap.getOrDefault(chatRoom.getId(), 0);

                    return ChatRoomResponseDto.fromMongo(
                            chatRoom,
                            summary != null ? summary.getLastMessage() : null,
                            unreadCount
                    );
                })
                .toList();

        return new SliceImpl<>(content, pageable, chatRooms.hasNext());
    }

    // 마지막 메시지 기록을 mongoDB에서 관리하도록 이전하여 제거 
//    @Transactional
//    public void updateLastMessageAt(Long chatRoomId, LocalDateTime messageTime) {
//        chatRoomRepository.updateLastMessageAt(chatRoomId, messageTime);
//    }

    @Transactional
    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomPolicy.validateChatRoomExists(chatRoomId);
        chatRoom.delete();
        chatRoomRepository.save(chatRoom);

        // MongoDB 데이터도 삭제 (소프트 삭제)
        chatRoomSummaryDocumentRepository.findByChatRoomId(chatRoomId)
                .ifPresent(chatRoomSummaryDocumentRepository::delete);
    }
}