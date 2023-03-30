package pl.exadel.milavitsky.tenderflex.service.impl;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.exadel.milavitsky.tenderflex.dto.FileDto;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    public InputStream getObject(String filename) {
        InputStream stream;
        try {
            stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build());
        } catch (Exception e) {
            log.error("Happened error when get list objects from minio: ", e);
            return null;
        }

        return stream;
    }

    public FileDto uploadFile(FileDto request) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(request.getFile().getOriginalFilename())
                    .stream(request.getFile().getInputStream(), request.getFile().getSize(), -1)
                    .build());
        } catch (Exception e) {
            log.error("Happened error when upload file: ", e);
        }
        return FileDto.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .size(request.getFile().getSize())
                .url(getPreSignedUrl(request.getFile().getOriginalFilename()))
                .filename(request.getFile().getOriginalFilename())
                .build();
    }

    private String getPreSignedUrl(String filename) {
        return "http://localhost:8080/file/".concat(filename);
    }
}
