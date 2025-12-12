package org.aper.web.domain.review.service;

// REMOVED: ChatRoom belongs to aper_chat_renewal;
import org.aper.web.domain.review.entity.Review;
import org.aper.web.domain.review.entity.ReviewDetail;
import org.aper.web.domain.common.constant.ReviewTypeEnum;
import org.aper.web.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
// REMOVED: Chat belongs to aper_chat_renewal - import org.aper.web.domain.chat.repository.ChatRoomRepository;
import org.aper.web.domain.review.repository.ReviewRepository;
import org.aper.web.domain.review.dto.ReviewRequestDto.CreateReviewRequestDto;
import org.aper.web.domain.review.dto.ReviewResponseDto.CreatedReviewDto;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final UserRepository userRepository;
    // REMOVED: private final ChatRoomRepository chatRoomRepository;
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

        // ChatRoom 검증은 aper_chat_renewal 서비스에서 처리
        // MSA 환경에서는 chatRoomId 존재 여부를 API 호출로 검증하거나
        // 이벤트 기반으로 처리할 수 있음

        String revieweePenName = reviewee.getPenName();

        Review review = Review.builder()
                .revieweePenName(revieweePenName)
                .reviewerPenName(reviewerPenName)
                .reviewee(reviewee)
                .reviewer(reviewer)
                .chatRoomId(chatRoomId)  // chatRoom 객체 대신 ID만 저장
                .build();

        for (ReviewTypeEnum reviewType : reviewTypes) {
            ReviewDetail reviewDetail = ReviewDetail.builder()
                    .reviewType(reviewType)
                    .review(review)
                    .build();
            review.getReviewDetailList().add(reviewDetail);
        }
        reviewRepository.save(review);

        // NOTE: ChatRoom의 리뷰 정보는 필요시 chatRoomId로 조회
        // Review에 이미 chatRoomId가 저장되어 있으므로
        // reviewRepository.findByChatRoomId(chatRoomId)로 조회 가능
        //
        // 만약 ChatRoom에 hasReview 같은 캐싱된 값이 필요하다면
        // 이벤트 발행으로 aper_chat_renewal에 알림:
        // eventPublisher.publishEvent(new ReviewCreatedEvent(review.getId(), chatRoomId));

        return new CreatedReviewDto(review.getId());
    }
}
