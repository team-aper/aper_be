package org.aper.web.domain.user.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.aper.web.domain.chat.entity.ChatRoom;
import org.aper.web.domain.chat.repository.ChatRoomRepository;
import org.aper.web.domain.image.service.S3ImageService;
import org.aper.web.domain.kafka.service.KafkaUserProducerService;
import org.aper.web.domain.user.dto.UserRequestDto.*;
import org.aper.web.domain.user.dto.UserResponseDto.*;
import org.aper.web.domain.user.entity.Review;
import org.aper.web.domain.user.entity.ReviewDetail;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.entity.constant.ReviewTypeEnum;
import org.aper.web.domain.user.entity.constant.UserRoleEnum;
import org.aper.web.domain.user.repository.ReviewRepository;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3ImageService s3ImageService;
    private final KafkaUserProducerService producerService;

    public UserService(UserRepository userRepository,
                       ChatRoomRepository chatRoomRepository,
                       ReviewRepository reviewRepository,
                       PasswordEncoder passwordEncoder,
                       S3ImageService s3ImageService,
                       KafkaUserProducerService producerService) {
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.reviewRepository = reviewRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3ImageService = s3ImageService;
        this.producerService = producerService;
    }

    public User findUser(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
    }

    public SignupResponseDto signupUser(@Valid SignupRequestDto requestDto) {
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

        return new SignupResponseDto(penName);
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
    public String changeImage(User user, MultipartFile fieldImageFile) {
        String fileKey = s3ImageService.uploadFile(fieldImageFile);
        removeExistImage(user);

        String imageUrl = s3ImageService.getImageUrl(fileKey);
        user.updateFieldImage(imageUrl);
        userRepository.save(user);

        return imageUrl;
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

    @Transactional
    public CreatedReviewDto createReview(User reviewer, CreateReviewRequestDto requestDto) {
        Long reviewerId = reviewer.getUserId();
        Long revieweeId = requestDto.revieweeId();
        Long chatRoomId = requestDto.chatRoomId();
        List<ReviewTypeEnum> reviewTypes = requestDto.reviewTypes();

        User reviewee = userRepository.findByIdExceptMe(revieweeId, reviewerId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 유저입니다.")
        );
        ChatRoom chatRoom = chatRoomRepository.findByIdForReview(chatRoomId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 채팅방입니다.")
        );

        Review review = Review.builder()
                .reviewee(reviewee)
                .reviewer(reviewer)
                .chatRoom(chatRoom)
                .build();

        for (ReviewTypeEnum reviewType : reviewTypes) {
            ReviewDetail reviewDetail = ReviewDetail.builder()
                    .reviewType(reviewType)
                    .review(review)
                    .build();
            review.getReviewDetailList().add(reviewDetail);
        }
        reviewRepository.save((review));

        chatRoom.setReview(review);
        chatRoomRepository.save(chatRoom);

        return new CreatedReviewDto(review.getId());
    }

    public Boolean emailCheck(String email) {
        return userRepository.existsByEmail(email);
    }
}
