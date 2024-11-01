package org.aper.web.domain.field.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.chat.repository.ChatParticipantRepository;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.field.dto.FieldResponseDto.*;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.story.repository.StoryRepository;
import org.aper.web.domain.user.dto.UserResponseDto.ClassDescriptionResponseDto;
import org.aper.web.domain.user.dto.UserResponseDto.HistoryResponseDto;
import org.aper.web.domain.user.entity.ReviewDetail;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.entity.UserHistory;
import org.aper.web.domain.user.repository.ReviewDetailRepository;
import org.aper.web.domain.user.repository.UserHistoryRepository;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldService {

    private final EpisodeRepository episodeRepository;
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ReviewDetailRepository reviewDetailRepository;
    private final FieldMapper fieldMapper;
    private final FieldHelper fieldHelper;

    public FieldHeaderResponseDto getAuthorInfo(Long authorId) {
        User user = userRepository.findById(authorId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        return new FieldHeaderResponseDto(user.getPenName(), user.getFieldImage(), user.getDescription(), user.getContactEmail());
    }

    public HomeResponseDto getFieldHomeData(UserDetailsImpl userDetails, Long authorId, int page, int size) {
        Pageable pageAble = PageRequest.of(page, size);
        boolean isMyField = fieldHelper.isOwnField(authorId, userDetails);
        Page<Episode> episodeList = isMyField
                ? episodeRepository.findAllByUserIdWithPageAble(authorId, pageAble)
                : episodeRepository.findAllByEpisodeOnlyPublishedWithPageAble(authorId, pageAble);

        List<HomeDetailsResponseDto> detailsList = fieldMapper.toHomeDetailsResponseDtoList(episodeList.getContent());
        return new HomeResponseDto(isMyField, detailsList);
    }

    public StoriesResponseDto getStoriesData(UserDetailsImpl userDetails, Long authorId, int page, int size) {
        Pageable pageAble = PageRequest.of(page, size);
        boolean isMyField = fieldHelper.isOwnField(authorId, userDetails);
        Page<Story> storyList = isMyField
                ? storyRepository.findAllByStoriesWithPageAble(authorId, pageAble)
                : storyRepository.findAllByStoriesOnlyPublishedWithPageAble(authorId, pageAble);

        List<StoriesDetailsResponseDto> storiesList = fieldMapper.toStoriesDetailsResponseDtoList(storyList.getContent());
        return new StoriesResponseDto(isMyField, storiesList);
    }

    public DetailsResponseDto getDetailsData(Long authorId) {
        User user = userRepository.findById(authorId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        return fieldMapper.toDetailsResponseDto(user);
    }

    public HistoryResponseDto getHistory(Long authorId, UserDetailsImpl userDetails) {
        boolean isMyField = fieldHelper.isOwnField(authorId, userDetails);
        List<UserHistory> historyList = userHistoryRepository.findAllByUserUserId(authorId);
        return fieldMapper.toHistoryResponseDto(historyList, isMyField);
    }

    public ClassDescriptionResponseDto getClassDescription(Long authorId) {
        User user = userRepository.findById(authorId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        Long totalClasses = chatParticipantRepository.countByUserUserIdAndIsTutorTrue(authorId);
        List<ReviewDetail> reviews = reviewDetailRepository.findReviewDetailsByUserId(authorId);
        return fieldMapper.classDescriptionToDto(user, totalClasses, reviews);
    }
}
