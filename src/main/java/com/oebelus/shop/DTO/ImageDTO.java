package com.oebelus.shop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ImageDTO {
    private Long imageId;
    private String imageName;
    private String downloadUrl;
}
