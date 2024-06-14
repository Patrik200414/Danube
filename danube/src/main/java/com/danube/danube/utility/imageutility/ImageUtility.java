package com.danube.danube.utility.imageutility;

import java.io.IOException;
import java.util.zip.DataFormatException;

public interface ImageUtility {
    byte[] compressImage(byte[] data) throws IOException;
    byte[] decompressImage(byte[] data) throws DataFormatException, IOException;
}
