package org.aper.web.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.elasticsearch.entity.document.ElasticSearchEpisodeDocument;
import org.aper.web.domain.elasticsearch.entity.document.ElasticSearchUserDocument;
import org.aper.web.domain.elasticsearch.repository.EpisodesElasticSearchRepository;
import org.aper.web.domain.elasticsearch.repository.UserElasticSearchRepository;
import org.aper.web.domain.elasticsearch.service.ElasticSearchMapper;
import org.aper.web.domain.search.entity.dto.SearchDto.AuthorListResponseDto;
import org.aper.web.domain.search.entity.dto.SearchDto.SearchAuthorResponseDto;
import org.aper.web.domain.search.entity.dto.SearchDto.SearchPenNameResponseDto;
import org.aper.web.domain.search.entity.dto.SearchDto.SearchStoryResponseDto;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchElasticService implements SearchServiceInterface{
    private final UserElasticSearchRepository userElasticSearchRepository;
    private final EpisodesElasticSearchRepository episodesElasticSearchRepository;
    private final UserRepository userRepository;
    private final SearchMapper searchMapper;
    private final ElasticSearchMapper elasticSearchMapper;
    @Override
    public SearchStoryResponseDto getSearchStory(int page, int size, StoryGenreEnum genre, String filter) {
        Pageable pageAble = PageRequest.of(page, size);
        List<ElasticSearchEpisodeDocument> searchResult = episodesElasticSearchRepository.searchEpisodesWithQueryBuilders(filter, genre, pageAble);
        return new SearchStoryResponseDto(searchMapper.episodesDocumentListToResponseDto(searchResult));
    }

    @Override
    public SearchPenNameResponseDto getOnlyPenName (int page, int size, String penName) {
        Pageable pageAble = PageRequest.of(page, size);
        List<ElasticSearchUserDocument> targetAuthors = userElasticSearchRepository.searchUsersWithQueryBuilders(penName, pageAble);
        return new SearchPenNameResponseDto(searchMapper.userDocumentListToPenNameResponseDto(targetAuthors));
    }

    @Override
    public SearchAuthorResponseDto getSearchAuthor(int page, int size, String penName, UserDetailsImpl userDetails) {
        Pageable pageAble = PageRequest.of(page, size);
        Long userId = userDetails == null ? null : userDetails.user().getUserId();
        List<ElasticSearchUserDocument> targetAuthors = userElasticSearchRepository.searchUsersWithQueryBuilders(penName, pageAble);
        List<Long> userIdList = elasticSearchMapper.UserDocumentListToLong(targetAuthors);
        List<Object[]> userList = userRepository.findUserWithSubscriberAndReviewCounts(userIdList, userId);
        List<AuthorListResponseDto> responseDto = searchMapper.UserListToAuthorListResponseDto(userList);
        return new SearchAuthorResponseDto(responseDto);
    }
}
