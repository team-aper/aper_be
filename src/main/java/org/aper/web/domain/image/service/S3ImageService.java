package org.aper.web.domain.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ImageService {
    @Value("${AWS_IMAGE_BUCKET}")
    private String bucket;

    @Value("${aws.s3.field.dir}")
    private String directory;

    private final AmazonS3 s3Client;

    // S3에 이미지 업로드
    public String uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String[] filNameSplit = fileName.split("\\.");
        String currentTime = String.valueOf(System.currentTimeMillis());

        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            String fileKey = directory + currentTime + fileName;
            s3Client.putObject(bucket, fileKey, file.getInputStream(), objectMetadata);
            return fileKey;
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 중 에러 발생: " + fileName);
        }
    }

    // S3에서 이미지 가져오기
    public String getImageUrl(String fileKey) {
        return s3Client.getUrl(bucket, fileKey).toString();
    }

    // S3에서 이미지 삭제
    public void deleteFile(String fileKey) {
        s3Client.deleteObject(bucket, fileKey);
    }
}
