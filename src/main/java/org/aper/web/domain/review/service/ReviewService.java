package org.aper.web.domain.review.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.aper.web.domain.chat.entity.ChatRoom;
import org.aper.web.domain.chat.repository.ChatRoomRepository;
import org.aper.web.domain.review.entity.Review;
import org.aper.web.domain.review.entity.ReviewDetail;
import org.aper.web.domain.review.repository.ReviewRepository;
import org.aper.web.domain.user.dto.UserRequestDto.CreateReviewRequestDto;
import org.aper.web.domain.user.dto.UserResponseDto.CreatedReviewDto;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.entity.constant.ReviewTypeEnum;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ReviewRepository reviewRepository;
    
    @Transactional
    public CreatedReviewDto createReview(User reviewer, CreateReviewRequestDto requestDto) {
        Long reviewerId = reviewer.getUserId();
        String reviewerPenName = reviewer.getPenName();
        Long revieweeId = requestDto.revieweeId();
        Long chatRoomId = requestDto.chatRoomId();
        List<ReviewTypeEnum> reviewTypes = requestDto.reviewTypes();

        User reviewee = userRepository.findByIdExceptMe(revieweeId, reviewerId).orElseThrow(() ->
                new ServiceException(ErrorCode.USER_NOT_FOUND)
        );
        ChatRoom chatRoom = chatRoomRepository.findByIdForReview(chatRoomId).orElseThrow(() ->
                new ServiceException(ErrorCode.CHAT_ROOM_NOT_FOUND)
        );
        String revieweePenName = reviewee.getPenName();

        Review review = Review.builder()
                .revieweePenName(revieweePenName)
                .reviewerPenName(reviewerPenName)
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
}
