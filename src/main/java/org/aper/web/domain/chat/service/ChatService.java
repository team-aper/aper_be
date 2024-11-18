package org.aper.web.domain.chat.service;

import org.aper.web.domain.chat.dto.ChatParticipatingResponseDto;
import org.aper.web.domain.chat.entity.ChatParticipant;
import org.aper.web.domain.chat.entity.ChatRoom;
import org.aper.web.domain.chat.entity.ChatRoomView;
import org.aper.web.domain.chat.repository.ChatParticipantRepository;
import org.aper.web.domain.chat.repository.ChatRoomRepository;
import org.aper.web.domain.chat.repository.ChatRoomViewRepository;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatRoomViewRepository viewRepository;

    public ChatService(UserRepository userRepository, ChatRoomRepository chatRoomRepository, ChatParticipantRepository chatParticipantRepository, ChatRoomViewRepository viewRepository) {
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipantRepository = chatParticipantRepository;
        this.viewRepository = viewRepository;
    }

//    @Transactional
//    public void createChat(Long userId, Long tutorId) {
//        if (isCreatedChat(userId, tutorId)) {
//            throw new ServiceException(ErrorCode.CHAT_ALREADY_PARTICIPATING);
//        }
//        ChatRoom chatRoom = new ChatRoom();
//
//        User user = findByIdAndCheckPresent(userId, false);
//        User tutor = findByIdAndCheckPresent(tutorId, true);
//
//        ChatParticipant userChatParticipant = new ChatParticipant(chatRoom, user, false);
//        ChatParticipant tutorChatParticipant = new ChatParticipant(chatRoom, tutor, true);
//
//        chatRoomRepository.save(chatRoom);
//        chatParticipantRepository.save(userChatParticipant);
//        chatParticipantRepository.save(tutorChatParticipant);
//    }
//
//    private boolean isCreatedChat(Long userId, Long tutorId) {
//        String tag = tutorId + "-" + userId;
//        viewRepository.updateChatRoomParticipantsView();
//        List<ChatRoomView> existingChatRoom = viewRepository.findByParticipants(tag);
//        return !existingChatRoom.isEmpty();
//    }

    @Transactional
    public List<ChatParticipatingResponseDto> getParticipatingChats(Long userId) {
        List<ChatParticipant> participatingChats = chatParticipantRepository.findByUserUserId(userId);

        if (participatingChats.isEmpty()) {
            throw new ServiceException(ErrorCode.NO_PARTICIPATING_CHAT);
        }

        List<ChatParticipatingResponseDto> participatingResponseDtos = new ArrayList<>();
        for (ChatParticipant chatParticipant : participatingChats) {
            ChatRoom chatRoom = chatParticipant.getChatRoom();
            if (!chatRoom.getIsRejected()) {
                ChatParticipatingResponseDto participatingResponseDto = new ChatParticipatingResponseDto(
                        chatRoom.getId(),
                        chatParticipant.getIsTutor(),
                        chatRoom.getIsAccepted(),
                        chatRoom.getStartTime()
                );
                participatingResponseDtos.add(participatingResponseDto);
            }
        }

        return participatingResponseDtos;
    }

//    @Transactional
//    public void rejectChatRoomRequest(Long roomId, Long tutorId) {
//        Optional<ChatParticipant> chatParticipantOptional = chatParticipantRepository.findByIsTutorAndUserUserIdAndChatRoomId(true, tutorId, roomId);
//
//        if (chatParticipantOptional.isEmpty()) {
//            throw new ServiceException(ErrorCode.CHAT_ROOM_REQUEST_NOT_FOUND);
//        }
//        ChatRoom chatRoom = chatParticipantOptional.get().getChatRoom();
//
//        if (chatRoom.getIsAccepted()) {
//            throw new ServiceException(ErrorCode.CHAT_ROOM_REQUEST_ACCEPTED);
//        }
//        if (chatRoom.getIsRejected()) {
//            throw new ServiceException(ErrorCode.CHAT_ROOM_REQUEST_REJECTED);
//        }
//        chatRoom.reject();
//        chatRoomRepository.save(chatRoom);
//    }
//
//    private User findByIdAndCheckPresent(Long id, Boolean tutor) {
//        Optional<User> user = userRepository.findById(id);
//
//        if (user.isEmpty()){
//            if (tutor) {
//                throw new ServiceException(ErrorCode.TUTOR_NOT_FOUND);
//            }
//            throw new ServiceException(ErrorCode.USER_NOT_FOUND);
//        }
//        return user.get();
//    }


}
