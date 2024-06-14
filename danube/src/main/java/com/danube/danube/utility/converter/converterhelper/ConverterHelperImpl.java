package com.danube.danube.utility.converter.converterhelper;

import com.danube.danube.model.dto.image.ImageShow;
import com.danube.danube.model.dto.product.ProductInformation;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.image.Image;
import com.danube.danube.utility.imageutility.ImageUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

public class ConverterHelperImpl implements ConverterHelper{
    @Override
    public ProductInformation createProductInformation(Product product) {
        return new ProductInformation(
                product.getProductName(),
                product.getPrice(),
                product.getDeliveryTimeInDay(),
                product.getQuantity(),
                product.getRating(),
                product.getShippingPrice(),
                product.getSold(),
                product.getBrand(),
                product.getDescription(),
                product.getSeller().getFullName(),
                product.getSubcategory().getId()
        );
    }

    @Override
    public List<ImageShow> getProductImages(Product product, ImageUtility imageUtility) throws DataFormatException, IOException {
        List<ImageShow> imageShows = new ArrayList<>();

        for(Image image : product.getImages()){
            ImageShow imageShow = new ImageShow(image.getFileName(), imageUtility.decompressImage(image.getImageFile()));
            imageShows.add(imageShow);
        }
        return imageShows;
    }
}
