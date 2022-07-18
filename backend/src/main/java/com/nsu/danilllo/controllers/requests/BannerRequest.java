package com.nsu.danilllo.controllers.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class BannerRequest {
    @NotBlank(message = "Banner name can't be empty")
    private String name;
    @NotNull(message = "Banner price can't be empty")
    @Min(value = 0,message = "Price must be positive number")
    private Double price;
    @NotEmpty(message = "List of banner's categories can't be empty")
    private List<Long> categoriesIDs;
    @NotBlank(message = "Banner text can't be empty")
    private String text;
}
