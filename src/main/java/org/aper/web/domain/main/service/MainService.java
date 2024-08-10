package org.aper.web.domain.main.service;

import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.episode.specification.EpisodeSpecification;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.main.dto.response.GetCurationsResponseDto;
import org.aper.web.domain.curation.entity.Curation;
import org.aper.web.domain.curation.repository.CurationRepository;
import org.aper.web.domain.main.dto.response.GetEpisodesResponseDto;
import org.aper.web.domain.main.dto.response.GetUsersResponseDto;
import org.aper.web.domain.story.constant.StoryGenreEnum;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<GetCurationsResponseDto> getCurations(int page, int size) {
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), size, Sort.Direction.DESC, "createdAt");

        List<Curation> curations  = curationRepository.findAllForMain(pageRequest).getContent();
        List<GetCurationsResponseDto> responseDtoList = new ArrayList<>();

        for (Curation curation : curations) {
            responseDtoList.add(new GetCurationsResponseDto(curation));
        }

        return responseDtoList;
    }

    public List<GetUsersResponseDto> getUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), size, Sort.Direction.ASC, "userId");

        List<User> users  = userRepository.findAllForMain(pageRequest).getContent();
        List<GetUsersResponseDto> responseDtoList = new ArrayList<>();

        for (User user : users) {
            responseDtoList.add(new GetUsersResponseDto(user));
        }

        return responseDtoList;
    }

    public List<GetEpisodesResponseDto> getEpisodes(int page, int size, StoryGenreEnum storyGenre) {
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), size, Sort.Direction.DESC, "createdAt");

        Specification<Episode> spec = Specification
                .where(EpisodeSpecification.isOnDisplay())
                .and(EpisodeSpecification.isStoryOnDisplay())
                .and(EpisodeSpecification.hasStoryGenre(storyGenre));

        List<Episode> episodes  = episodeRepository.findAll(spec, pageRequest).getContent();
        List<GetEpisodesResponseDto> responseDtoList = new ArrayList<>();

        for (Episode episode : episodes) {
            responseDtoList.add(new GetEpisodesResponseDto(episode));
        }

        return responseDtoList;
    }
}
