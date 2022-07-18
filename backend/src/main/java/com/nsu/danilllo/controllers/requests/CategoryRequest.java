package com.nsu.danilllo.controllers.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CategoryRequest {
    @NotBlank(message = "Category name can't be empty")
    private String name;
    @NotBlank(message = "Category requestID can't be empty")
    private String requestedID;
}
