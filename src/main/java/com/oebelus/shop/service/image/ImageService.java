package com.oebelus.shop.service.image;

import com.oebelus.shop.DTO.ImageDTO;
import com.oebelus.shop.exceptions.ResourceNotFoundException;
import com.oebelus.shop.model.Image;
import com.oebelus.shop.model.Product;
import com.oebelus.shop.repository.ImageRepository;
import com.oebelus.shop.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final ProductService productService;

    @Override
    public Image getImage(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found!"));
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.findById(id).ifPresent(imageRepository::delete);
    }

    @Override
    public List<ImageDTO> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDTO> savedImageDtos = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                // Create a new image and set its properties
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                // Generate a download URL for the image
                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl + image.getId();
                image.setDownloadUrl(downloadUrl);

                // Save the image to the database to generate the ID
                Image savedImage = imageRepository.save(image);

                // Save the image to the database to update with the correct ID
                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
                imageRepository.save(savedImage);

                // Set the image data to be sent to the client
                ImageDTO imageDto = new ImageDTO(
                        savedImage.getId(),
                        savedImage.getFileName(),
                        savedImage.getDownloadUrl()
                );

                savedImageDtos.add(imageDto);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDtos;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImage(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
