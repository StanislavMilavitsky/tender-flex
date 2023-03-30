package pl.exadel.milavitsky.tenderflex.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import pl.exadel.milavitsky.tenderflex.dto.FileDto;
import pl.exadel.milavitsky.tenderflex.service.impl.MinioService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.web.servlet.HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/file")
public class FileController {

    private final MinioService minioService;

    @GetMapping()
    public ResponseEntity<Object> getFile(HttpServletRequest request) throws IOException {
        String pattern = (String) request.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE);
        String filename = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(IOUtils.toByteArray(minioService.getObject(filename)));
    }

    @PostMapping()
    public ResponseEntity<Object> upload(@ModelAttribute FileDto request) {
        return ResponseEntity.ok().body(minioService.uploadFile(request));
    }
}
