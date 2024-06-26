package com.danube.danube.utility.imageutility;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Component
public class ImageUtilityImpl implements ImageUtility{

    public static final int BYTE_ARRAY_DEFAULT_SIZE = 4 * 1024;

    @Override
    public byte[] compressImage(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] template = new byte[BYTE_ARRAY_DEFAULT_SIZE];
        while (!deflater.finished()){
            int size = deflater.deflate(template);
            outputStream.write(template, 0, size);
        }

        outputStream.close();
        return outputStream.toByteArray();
    }

    @Override
    public byte[] decompressImage(byte[] data) throws DataFormatException, IOException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] template = new byte[BYTE_ARRAY_DEFAULT_SIZE];

        while (!inflater.finished()){
            int size = inflater.inflate(template);
            outputStream.write(template, 0, size);
        }

        outputStream.close();
        return outputStream.toByteArray();
    }
}
