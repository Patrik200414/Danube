package com.danube.danube.utility.converter.converterhelper;

import com.danube.danube.model.dto.image.ImageShow;
import com.danube.danube.model.dto.product.ProductInformation;
import com.danube.danube.model.product.Product;
import com.danube.danube.utility.imageutility.ImageUtility;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

public interface ConverterHelper {
    ProductInformation createProductInformation(Product product);
    List<ImageShow> getProductImages(Product product, ImageUtility imageUtility) throws DataFormatException, IOException;
}
