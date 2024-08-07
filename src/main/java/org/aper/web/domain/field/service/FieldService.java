package org.aper.web.domain.field.service;

import org.aper.web.domain.field.dto.DetailsResponseDto;
import org.aper.web.domain.field.dto.HomeResponseDto;
import org.aper.web.domain.field.dto.StoriesResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;

@Service
public class FieldService {
    public HomeResponseDto getFieldHomeData(UserDetailsImpl userDetails, Long authorId) {

    }

    public StoriesResponseDto getStoriesData(UserDetailsImpl userDetails, Long authorId) {

    }

    public DetailsResponseDto getDetailsData(UserDetailsImpl userDetails, Long authorId) {

    }
}
