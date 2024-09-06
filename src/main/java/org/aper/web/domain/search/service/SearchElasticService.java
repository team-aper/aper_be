package org.aper.web.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.search.entity.document.ElasticSearchEpisodeDocument;
import org.aper.web.domain.search.entity.dto.SearchDto.*;
import org.aper.web.domain.search.repository.CustomElasticSearchRepository;
import org.aper.web.domain.search.repository.ElasticSearchRepository;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchElasticService implements SearchServiceInterface{
    private final ElasticSearchRepository elasticSearchRepository;
    private final CustomElasticSearchRepository customElasticSearchRepository;
    private final UserRepository userRepository;
    private final SearchMapper searchMapper;
    @Override
    public SearchStoryResponseDto getSearchStory(int page, int size, StoryGenreEnum genre, String filter) {
        Pageable pageAble = PageRequest.of(page, size);
        String stringGenre = genre == null ? "" : genre.name();
        List<ElasticSearchEpisodeDocument> searchResult = customElasticSearchRepository.searchWithQueryBuilders(filter, stringGenre, pageAble);
        return new SearchStoryResponseDto(searchMapper.documentListToKafkaDto(searchResult));
    }

    @Override
    public SearchAuthorResponseDto getSearchAuthor(int page, int size, String penName) {
        Pageable pageAble = PageRequest.of(page, size);
        List<User> targetAuthors = userRepository.findAllByPenNameContaining(pageAble, penName).getContent();
        return new SearchAuthorResponseDto(searchMapper.UserListToAuthorListResponseDto(targetAuthors));
    }
}
