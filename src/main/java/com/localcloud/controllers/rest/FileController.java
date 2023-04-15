package com.localcloud.controllers.rest;

import com.localcloud.dtos.FileDetailDto;
import com.localcloud.services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
    public ResponseEntity<Resource> download(@RequestParam("filePath") String filePath,
                                             @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {

        File file = new File(filePath);
        InputStreamResource resource = fileService.downloadFileByFilePath(filePath);
        long fileSize = file.length();

        if (rangeHeader == null) {
            // Send the whole file
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileSize));
            headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            // Send a partial content response
            long start = getStartByte(rangeHeader, fileSize);
            long end = fileSize - 1;

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            headers.add(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize);
            headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(end - start + 1));
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            randomAccessFile.seek(start);
            InputStream inputStream = new BufferedInputStream(new FileInputStream(randomAccessFile.getFD()));

            InputStreamResource partialContentResource = new InputStreamResource(inputStream);

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(partialContentResource);
        }
    }

    private long getStartByte(String rangeHeader, long fileSize) {
        String[] rangeValues = rangeHeader.substring("bytes=".length()).split("-");
        if (rangeValues.length == 1) {
            // Range header is like "bytes=500-"
            return Long.parseLong(rangeValues[0]);
        } else {
            // Range header is like "bytes=500-1000"
            long start = Long.parseLong(rangeValues[0]);
            long end = Long.parseLong(rangeValues[1]);
            if (end >= fileSize) {
                return start;
            } else {
                return start <= end ? start : fileSize - 1;
            }
        }
    }


    @PostMapping("/upload")
    ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file) {
        fileService.uploadFile(file);
        return ResponseEntity.ok().build();
    }

}
