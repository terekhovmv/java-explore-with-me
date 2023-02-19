package ru.practicum.ewm.category.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateCategoryDto {
    @NotBlank
    String name;
}
