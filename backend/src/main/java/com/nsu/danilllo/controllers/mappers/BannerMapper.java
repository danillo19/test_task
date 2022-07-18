package com.nsu.danilllo.controllers.mappers;

import com.nsu.danilllo.controllers.dto.BannerDto;
import com.nsu.danilllo.model.Banner;

public class BannerMapper {

    public static BannerDto toDto(Banner banner) {
        BannerDto bannerDto = new BannerDto();
        bannerDto.setName(banner.getName());
        bannerDto.setId(banner.getId());
        return bannerDto;
    }
}
