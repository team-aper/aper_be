package org.aper.web.domain.chat.service;

import org.aper.web.domain.chat.entity.ChatParticipant;
import org.aper.web.domain.chat.entity.ChatRoom;
import org.aper.web.domain.chat.repository.ChatParticipantRepository;
import org.aper.web.domain.chat.repository.ChatRoomRepository;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.dto.ResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    public ChatService(UserRepository userRepository, ChatRoomRepository chatRoomRepository, ChatParticipantRepository chatParticipantRepository) {
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipantRepository = chatParticipantRepository;
    }

    @Transactional
    public ResponseDto<Void> createChat(Long userId, Long tutorId) {
        // 결제 횟수가 남아있는지 확인하는 코드도 추가 되어야 함.

        ChatRoom chatRoom = new ChatRoom();

        User user = findByIdAndCheckPresent(userId, false);
        User tutor = findByIdAndCheckPresent(tutorId, true);

        ChatParticipant userChatParticipant = new ChatParticipant(chatRoom, user, false);
        ChatParticipant tutorChatParticipant = new ChatParticipant(chatRoom, tutor, true);

        // tutor에게 알림 보내야 함.

        chatRoomRepository.save(chatRoom);
        chatParticipantRepository.save(userChatParticipant);
        chatParticipantRepository.save(tutorChatParticipant);

        return ResponseDto.success("성공적으로 채팅방을 생성하였습니다.");
    }

    @Transactional
    public boolean isCreatedChat(Long userId, Long tutorId) {
        User user = findByIdAndCheckPresent(userId, false);
        List<ChatParticipant> chatParticipants = user.getChatParticipants();

        for (ChatParticipant chatParticipant : chatParticipants) {
            List<ChatParticipant> roomParticipants = chatParticipantRepository.findByChatRoomId(chatParticipant.getChatRoom().getId());
            for (ChatParticipant roomParticipant : roomParticipants) {
                if (roomParticipant.getIsTutor() && Objects.equals(roomParticipant.getUser().getUserId(), tutorId)) {
                    return true;
                }
            }
        }
        return false;
    }

    private User findByIdAndCheckPresent(Long id, Boolean tutor) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()){
            if (tutor) {
                throw new IllegalArgumentException("튜터를 찾을 수 없습니다.");
            }
            throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
        }
        return user.get();
    }


}
