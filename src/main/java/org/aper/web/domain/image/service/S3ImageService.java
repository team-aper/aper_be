package org.aper.web.domain.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ImageService {
    private final ImageEncoder imageEncoder;
    @Value("${aws.s3.image.bucket}")
    private String bucket;

    @Value("${aws.s3.field.dir}")
    private String directory;

    private final AmazonS3 s3Client;

    private String uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String currentTime = String.valueOf(System.currentTimeMillis());

        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            String fileKey = directory + currentTime + fileName;
            s3Client.putObject(bucket, fileKey, file.getInputStream(), objectMetadata);
            return fileKey;
        } catch (IOException e) {
            throw new ServiceException(ErrorCode.S3_UPLOAD_ERROR_OCCURRED);
        }
    }

    private String getImageUrl(String fileKey) {
        return s3Client.getUrl(bucket, fileKey).toString();
    }

    public void deleteFile(String fileKey) {
        s3Client.deleteObject(bucket, fileKey);
    }

    public boolean isDefaultImage(String imageUrl) {
        return imageUrl != null && imageUrl.startsWith("/images");
    }

    public String uploadImageAndGetUrl(String imageBase64) {
        MultipartFile multipartFile = imageEncoder.Base64ToMultipartFile(imageBase64);
        String fileKey = uploadFile(multipartFile);
        return getImageUrl(fileKey);
    }
}
