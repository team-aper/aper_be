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
import org.aper.web.domain.chat.query.ChatRoomSummaryQuery;
import org.aper.web.domain.chat.query.UserReadTrackingQuery;
import org.aper.web.domain.chat.repository.ChatRoomMemberRepository;
import org.aper.web.domain.chat.repository.ChatRoomRepository;
import org.aper.web.domain.chat.repository.ChatRoomSummaryDocumentRepository;
import org.aper.web.domain.chat.repository.UserReadTrackingDocumentRepository;
import org.aper.web.domain.chat.validator.ChatRoomValidator;
import org.aper.web.domain.chat.validator.UserValidator;
import org.aper.web.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserReadTrackingDocumentRepository userReadTrackingDocumentRepository;
    private final ChatRoomSummaryDocumentRepository chatRoomSummaryDocumentRepository;
    private final UnreadCountCacheService unreadCountCacheService;

    private final ChatRoomSummaryQuery chatRoomSummaryQuery;
    private final UserReadTrackingQuery userReadTrackingQuery;
    private final UnreadCountService unreadCountService;

    private final UserValidator userValidator;
    private final ChatRoomFactory chatRoomFactory;
    private final ChatRoomValidator chatRoomValidator;

    @Transactional
    public CreatedChatRoomResponseDto createChatRoom(CreateChatRoomRequestDto request, Long userId) {
        User creator = userValidator.validateUserExists(userId);
        ChatRoom chatRoom = chatRoomFactory.create(request);
        chatRoom = chatRoomRepository.save(chatRoom);

        ChatRoomSummaryDocument summary = new ChatRoomSummaryDocument(chatRoom);
        chatRoomSummaryDocumentRepository.save(summary);

        addMember(chatRoom, creator, true);

        for (Long memberId : request.memberIds()) {
            User member = userValidator.validateUserExists(memberId);
            addMember(chatRoom, member, false);
        }

        return CreatedChatRoomResponseDto.from(chatRoom);
    }

    private void addMember(ChatRoom chatRoom, User user, boolean isOwner) {
        ChatRoomMember member = ChatRoomMember.create(chatRoom, user, isOwner);
        chatRoomMemberRepository.save(member);
    }

    @Transactional
    public Slice<ChatRoomResponseDto> getChatRoomsForUser(Long userId, Pageable pageable) {
        userValidator.validateUserExists(userId);

        Slice<ChatRoom> roomsSlice = chatRoomRepository.findRecentChatRooms(userId, pageable);
        List<ChatRoom> rooms = roomsSlice.getContent();

        if (rooms.isEmpty()) {
            return new SliceImpl<>(List.of(), pageable, false);
        }

        List<Long> roomIds = rooms.stream().map(ChatRoom::getId).toList();

        Map<Long, ChatRoomSummaryDocument> summaryMap = chatRoomSummaryQuery.getSummaryMap(roomIds);
        Map<Long, UserReadTrackingDocument> trackingMap = userReadTrackingQuery.getTrackingMap(userId, roomIds);

        Map<Long, Integer> unreadMap = unreadCountService.resolveUnreadCounts(userId, roomIds, summaryMap, trackingMap);

        List<ChatRoomResponseDto> content = rooms.stream()
                .map(room -> chatRoomFactory.response(room, summaryMap.get(room.getId()), unreadMap.get(room.getId())))
                .toList();

        return new SliceImpl<>(content, pageable, roomsSlice.hasNext());
    }

    @Transactional
    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomValidator.validateChatRoomExists(chatRoomId);
        chatRoom.delete();
        chatRoomRepository.save(chatRoom);

        // MongoDB 데이터 삭제
        chatRoomSummaryDocumentRepository.findByChatRoomId(chatRoomId)
                .ifPresent(chatRoomSummaryDocumentRepository::delete);

        // Redis 캐시 삭제
        unreadCountCacheService.evictChatRoomCache(chatRoomId);
    }
}