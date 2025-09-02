package com.backend.foodproject.dto.categoryDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateDto {

    @NotBlank(message = "Name is required")
    @Size(max = 60, message = "Name must not exceed 60 character")
    private String name;

    @NotBlank(message = "Category description is required")
    @Size(max = 200, message = "Category description must not exceed 200 character")
    private String description;
}
