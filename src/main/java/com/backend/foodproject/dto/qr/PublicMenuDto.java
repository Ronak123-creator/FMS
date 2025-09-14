package com.backend.foodproject.dto.qr;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicMenuDto {
    private List<PublicCategoryDto> categories;
}
