package com.localcloud.controllers.rest;

import com.localcloud.dtos.FileDetailDto;
import com.localcloud.services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;

    @GetMapping("/list")
    ResponseEntity<List<FileDetailDto>> list() {
        return ResponseEntity.ok(fileService.getListOfFiles());
    }

    @GetMapping("/download")
    ResponseEntity<Resource> download(@RequestParam("filePath") String filePath) {
        HttpHeaders headers = new HttpHeaders();
        File file = new File(filePath);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        InputStreamResource resource = fileService.downloadFileByFilePath(filePath);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping("/upload")
    ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file) {
        fileService.uploadFile(file);
        return ResponseEntity.ok().build();
    }

}
