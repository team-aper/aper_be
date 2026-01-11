package org.aper.web.domain.chat.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.chat.document.MessageDocument;
import org.aper.web.domain.chat.dto.ChatWebSocketDto;
import org.aper.web.domain.chat.entity.ChatRoom;
import org.aper.web.domain.chat.repository.ChatRoomMemberRepository;
import org.aper.web.domain.chat.repository.ChatRoomRepository;
import org.aper.web.domain.chat.repository.MessageDocumentRepository;
import org.aper.web.domain.chat.repository.MessageRepository;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatMessageService {

    private final MessageDocumentRepository messageDocumentRepository;
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;

    public void sendMessage(Long roomId, Long userId, ChatWebSocketDto.SendMessageRequest request){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
        // TODO: 이거 custom exception으로 처리하도록 수정해야 함.

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        chatRoomMemberRepository.findByChatRoomAndUserUserId(chatRoom, userId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방 멤버가 아닙니다."));

        LocalDateTime now = LocalDateTime.now();

        MessageDocument document = MessageDocument.of(roomId, user, request);
    }
}
