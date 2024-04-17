package com.danube.danube.utility.filellogger;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileLogger {
    void saveFile(MultipartFile[] multipartFile, String basePath) throws IOException;
}
