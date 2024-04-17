package com.danube.danube.utility.filellogger;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileLoggerImpl implements FileLogger{
    public void saveFile(MultipartFile[] multipartFile, String basePath) throws IOException {

        for(MultipartFile file : multipartFile){
            String filePath = basePath + file.getOriginalFilename();
            writeToFile(file.getBytes(), filePath);
        }
    }

    private void writeToFile(byte[] image, String path) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        fileOutputStream.write(image);
    }
}
