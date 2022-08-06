package com.localcloud.services;

import com.localcloud.dtos.FileDetailDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class FileService {

    public List<FileDetailDto> getListOfFiles() {
        try {
            List<FileDetailDto> response = new ArrayList<>();
            File file = new File(Paths.get("").toAbsolutePath().toString());
            for (File f : file.listFiles()) {
                BasicFileAttributes basicFileAttributes = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
                if (basicFileAttributes.isRegularFile()) {
                    FileDetailDto dto = new FileDetailDto();
                    dto.setFileName(f.getName());
                    dto.setFilePath(f.getAbsolutePath());
                    dto.setFileSize(basicFileAttributes.size());
                    response.add(dto);
                }
            }
            log.info("Listing files: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error listing files: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public InputStreamResource downloadFileByFilePath(String filePath) {
        try {
            File file = new File(filePath);
            log.info("Downloading file: {}", file.getAbsolutePath());
            return new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            log.error("File not found: {}", filePath);
            throw new RuntimeException(e);
        }
    }

    public void uploadFile(MultipartFile file) {
        try {
            log.info("Uploading file: {}", file.getOriginalFilename());
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            File dest = new File(Paths.get("").toAbsolutePath() + "/" + fileName);
            file.transferTo(dest);
            log.info("Uploaded file: {}", dest.getAbsolutePath());
        } catch (IOException | NullPointerException e) {
            log.error("Error uploading file: {}", file.getOriginalFilename());
            throw new RuntimeException(e);
        }
    }
}
