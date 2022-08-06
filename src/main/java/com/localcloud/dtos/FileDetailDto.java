package com.localcloud.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileDetailDto {

    private String fileName;
    private String filePath;
    private long fileSize;
    private String fileSizeWithUnit;

    public void setFilePath(String filePath) {
        this.filePath = filePath.replaceAll("\\\\", "/");
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
        String[] units = {"KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        double size = fileSize;
        int count = 0;
        while (true) {
            if (size >= 1024) {
                size = size / 1024;
                setFileSizeWithUnit(String.format("%.2f", size) + " " + units[count]);
                count++;
            } else {
                break;
            }
        }
    }
}
