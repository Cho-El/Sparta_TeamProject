package com.example.intermediate.controller.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ImageResponseDto {
    private String imgUrl;

    public ImageResponseDto(String imgUrl){
        this.imgUrl = imgUrl;

    }
}
