package com.nsu.danilllo.controllers.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BannerRequest {
    private String name;
    private Double price;
    private List<Long> categoriesIDs;
    private String text;
}
