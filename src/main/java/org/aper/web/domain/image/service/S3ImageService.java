package org.aper.web.domain.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ImageService {
    @Value("${AWS_IMAGE_BUCKET}")
    private String bucket;

    @Value("${aws.s3.field.dir}")
    private String directory;

    private final AmazonS3 s3Client;

    public String uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String[] filNameSplit = fileName.split("\\.");
        String currentTime = String.valueOf(System.currentTimeMillis());

        try {
            String fileKey = fileName + currentTime;
            s3Client.putObject(bucket, fileKey, file.getInputStream().toString());
            return fileKey;
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 중 에러 발생: " + fileName);
        }
    }

    // S3에서 이미지 삭제
    public void deleteFile(String fileName) {
        String[] urlSplit = fileName.split("/");
        String imageUrl = urlSplit[urlSplit.length-1];
        s3Client.deleteObject(bucket, imageUrl);
    }
}
