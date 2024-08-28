package com.oebelus.shop.service.image;

import com.oebelus.shop.DTO.ImageDTO;
import com.oebelus.shop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    Image getImage(Long id);

    void deleteImage(Long id);

    List<ImageDTO> saveImages(List<MultipartFile> file, Long productId);

    void updateImage(MultipartFile file, Long imageId);
}
