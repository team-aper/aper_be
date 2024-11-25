package org.aper.web.domain.main.service;

import com.aperlibrary.curation.entity.Curation;
import com.aperlibrary.episode.entity.Episode;
import com.aperlibrary.story.entity.constant.StoryGenreEnum;
import com.aperlibrary.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.curation.repository.CurationRepository;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.episode.specification.EpisodeSpecification;
import org.aper.web.domain.main.dto.MainResponseDto.*;
import org.aper.web.domain.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MainService {

    private final CurationRepository curationRepository;
    private final UserRepository userRepository;
    private final EpisodeRepository episodeRepository;

    public MainService(CurationRepository curationRepository, UserRepository userRepository, EpisodeRepository episodeRepository) {
        this.curationRepository = curationRepository;
        this.userRepository = userRepository;
        this.episodeRepository = episodeRepository;
    }

    public GetCurationsListResponseDto getCurations(int page, int size) {
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), size, Sort.Direction.DESC, "createdAt");

        List<Curation> curations = curationRepository.findAllForMain(pageRequest).getContent();

        return MainMapper.toGetCurationsListResponseDto(curations);
    }

    public GetUsersListResponseDto getUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), size, Sort.Direction.ASC, "userId");

        List<User> users = userRepository.findAllForMain(pageRequest).getContent();

        return MainMapper.toGetUsersListResponseDto(users);
    }

    public GetEpisodesListResponseDto getEpisodes(int page, int size, StoryGenreEnum storyGenre) {
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), size, Sort.Direction.DESC, "createdAt");

        Specification<Episode> spec = Specification
                .where(EpisodeSpecification.isOnDisplay())
                .and(EpisodeSpecification.isStoryOnDisplay())
                .and(EpisodeSpecification.hasStoryGenre(storyGenre));

        List<Episode> episodes = episodeRepository.findAll(spec, pageRequest).getContent();

        return MainMapper.toGetEpisodesListResponseDto(episodes);
    }
}
