package pl.exadel.milavitsky.tenderflex.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;


@Configuration
public class MinioConfig {

    @Value("${minio.access.name}")//todo
    String accessKey;

    @Value("${minio.access.secret}")
    String secretKey;

    @Value("${minio.url}")
    String minioUrl;

    @Bean
    @Primary
    public MinioClient minioClient() {
        return new MinioClient.Builder()
                .credentials(accessKey, secretKey)
                .endpoint(minioUrl)
                .build();
    }

}
