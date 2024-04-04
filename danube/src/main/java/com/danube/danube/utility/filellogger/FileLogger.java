package com.danube.danube.utility.filellogger;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;

abstract public class FileLogger {
    private static final String BASE_PATH = String.format("%s\\src\\main\\resources\\images\\", System.getProperty("user.dir"));
    public static void saveFile(MultipartFile[] multipartFile) throws IOException {

        for(MultipartFile file : multipartFile){
            System.out.println(file.getContentType());

            writeToFile(file.getBytes(), file.getOriginalFilename());
        }
    }

    private static void writeToFile(byte[] image, String path) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(BASE_PATH + path);
        fileOutputStream.write(image);
    }
}
