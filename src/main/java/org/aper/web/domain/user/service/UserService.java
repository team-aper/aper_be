package org.aper.web.domain.user.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.aper.web.domain.image.service.S3ImageService;
import org.aper.web.domain.user.dto.UserRequestDto.*;
import org.aper.web.domain.user.dto.UserResponseDto.*;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.entity.UserRoleEnum;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3ImageService s3ImageService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, S3ImageService s3ImageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3ImageService = s3ImageService;
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

        return new SignupResponseDto(penName);
    }

    @Transactional
    public void ChangePenName(User user, ChangePenNameDto changePenNameDto) {
        String newPenName = changePenNameDto.penName();
        user.updatePenName(newPenName);
        userRepository.save((user));
    }

    @Transactional
    public void changeEmail(User user, ChangeEmailDto changeEmailDto) {
        String newEmail = changeEmailDto.email();
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
}
