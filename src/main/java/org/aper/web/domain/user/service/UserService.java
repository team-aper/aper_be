package org.aper.web.domain.user.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.aper.web.domain.chat.repository.ChatRoomRepository;
import org.aper.web.domain.image.service.S3ImageService;
import org.aper.web.domain.kafka.service.KafkaUserProducerService;
import org.aper.web.domain.search.dto.SearchDto;
import org.aper.web.domain.search.service.SearchMapper;
import org.aper.web.domain.user.dto.UserRequestDto.*;
import org.aper.web.domain.user.dto.UserResponseDto;
import org.aper.web.domain.user.dto.UserResponseDto.IsDuplicated;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.entity.constant.UserRoleEnum;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3ImageService s3ImageService;
    private final KafkaUserProducerService producerService;
    private final UserMapper userMapper;
    private final SearchMapper searchMapper;

    public UserService(UserRepository userRepository,
                       ChatRoomRepository chatRoomRepository,
                       PasswordEncoder passwordEncoder,
                       S3ImageService s3ImageService,
                       KafkaUserProducerService producerService,
                       UserMapper userMapper, SearchMapper searchMapper) {
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3ImageService = s3ImageService;
        this.producerService = producerService;
        this.userMapper = userMapper;
        this.searchMapper = searchMapper;
    }

    public User findUser(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
    }

    public void signupUser(@Valid SignupRequestDto requestDto) {
        String penName = requestDto.penName();
        String email = requestDto.email();
        String password = passwordEncoder.encode(requestDto.password());

        if (userRepository.existsByEmail(email)) {
            throw new ServiceException(ErrorCode.ALREADY_EXIST_EMAIL);
        }

        User user = User.builder()
                .email(email)
                .password(password)
                .penName(penName)
                .role(UserRoleEnum.USER)
                .build();

        userRepository.save(user);
        producerService.sendCreate(user);
    }

    @Transactional
    public void ChangePenName(User user, ChangePenNameDto changePenNameDto) {
        String newPenName = changePenNameDto.penName();
        user.updatePenName(newPenName);
        userRepository.save((user));
        producerService.sendUpdate(user);
    }

    @Transactional
    public void changeEmail(User user, ChangeEmailDto changeEmailDto) {
        String newEmail = changeEmailDto.email();
        if (userRepository.existsByEmail(newEmail)) {
            throw new ServiceException(ErrorCode.ALREADY_EXIST_EMAIL);
        }
        user.updateEmail(newEmail);
        userRepository.save((user));
    }

    @Transactional
    public void changeDescription(User user, ChangeDescriptionDto descriptionDto) {
        String newDescription = descriptionDto.description();
        user.updateDescription(newDescription);
        userRepository.save(user);
    }

    @Transactional
    public void changeImage(User user, String imageUrl) {
//        String fileKey = s3ImageService.uploadFile(fieldImageFile);
//        removeExistImage(user);
//
//        String imageUrl = s3ImageService.getImageUrl(fileKey);
        user.updateFieldImage(imageUrl);
        userRepository.save(user);
    }

    private void removeExistImage(User user) {
        String existFieldImage = user.getFieldImage();
        if(existFieldImage != null) {
            String fileKey = existFieldImage.split(".amazonaws.com/")[1];
            s3ImageService.deleteFile(fileKey);
        }
    }

    public void changeContactEmail(User user, ChangeEmailDto changeEmailDto) {
        String newEmail = changeEmailDto.email();
        user.updateContactEmail(newEmail);
        userRepository.save((user));
    }

    public void changeClassDescription(User user, ClassDescriptionRequestDto requestDto) {
        String classDescription = requestDto.description();
        user.updateClassDescription(classDescription);
        userRepository.save((user));
    }

    public IsDuplicated emailCheck(String email) {
        return new IsDuplicated((userRepository.existsByEmail(email)));
    }

    public UserResponseDto.UserInfo getUserInfo(UserDetailsImpl userDetails) {
        User user = userRepository.findByIdWithHistory(userDetails.user().getUserId())
                .orElseThrow(()-> new ServiceException(ErrorCode.USER_NOT_FOUND));
        return userMapper.userToUserInfo(user);
    }

    public void requestTutoring(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        user.setRequestTutor(Boolean.TRUE);
    }

    public SearchDto.SearchAuthorResponseDto getRequestedTutor() {
        List<Object[]> requestedTutors = userRepository.findByRequestTutorIsTrue();
        List<SearchDto.AuthorListResponseDto> responseDto = searchMapper.UserListToAuthorListResponseDto(requestedTutors);
        return new SearchDto.SearchAuthorResponseDto(responseDto);
    }
}
