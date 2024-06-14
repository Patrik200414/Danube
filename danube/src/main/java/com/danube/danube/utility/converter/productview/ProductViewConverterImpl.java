package com.danube.danube.utility.converter.productview;

import com.danube.danube.model.dto.image.ImageShow;
import com.danube.danube.model.dto.order.CartItemShowDTO;
import com.danube.danube.model.dto.product.DetailDTO;
import com.danube.danube.model.dto.product.ProductItemDTO;
import com.danube.danube.model.dto.product.ProductShowSmallDTO;
import com.danube.danube.model.order.Order;
import com.danube.danube.model.product.Product;
import com.danube.danube.utility.converter.converterhelper.ConverterHelper;
import com.danube.danube.utility.converter.converterhelper.ConverterHelperImpl;
import com.danube.danube.utility.imageutility.ImageUtility;
import org.hibernate.internal.util.collections.LinkedIdentityHashMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@Component
public class ProductViewConverterImpl implements ProductViewConverter{

    @Override
    public Set<ProductShowSmallDTO> convertProductToProductShowSmallDTORandomOrder(Page<Product> products, ImageUtility imageUtility, ConverterHelper converterHelper) throws DataFormatException, IOException {
        Set<ProductShowSmallDTO> productShowSmallDTOs = new HashSet<>();

        convertProductToSpecificCollection(products.stream().toList(), imageUtility, productShowSmallDTOs, converterHelper);
        return productShowSmallDTOs;
    }



    @Override
    public ProductItemDTO convertProductToProductItemDTO(Product product, ImageUtility imageUtility, ConverterHelper converterHelper) throws DataFormatException, IOException {
        List<DetailDTO> detailValues = product.getProductValues().stream()
                .map(productValue -> new DetailDTO(
                        productValue.getDetailName(),
                        productValue.getDetailId(),
                        productValue.getValueName()
                )).toList();

        return new ProductItemDTO(
                converterHelper.createProductInformation(product),
                converterHelper.getProductImages(product, imageUtility),
                detailValues
        );
    }

    @Override
    public List<ProductShowSmallDTO> convertProductsToProductShowSmallDTO(Collection<Product> products, ImageUtility imageUtility, ConverterHelper converterHelper) throws DataFormatException, IOException {
        List<ProductShowSmallDTO> productShowSmallDTOs = new ArrayList<>();
        convertProductToSpecificCollection(products, imageUtility, productShowSmallDTOs, converterHelper);
        return productShowSmallDTOs;
    }

    @Override
    public Map<String, String> convertProductToMyProductInformation(Product product) {
        Map<String, String> myProductInformation = new LinkedIdentityHashMap<>();
        myProductInformation.put("Product image", !product.getImages().isEmpty() ? product.getFirstProductImage().getFileName() : "defaultProduct.jpg");
        myProductInformation.put("Product name", product.getProductName());
        myProductInformation.put("Owner", product.getSellerFullName());
        myProductInformation.put("id", String.valueOf(product.getId()));
        return myProductInformation;
    }


    @Override
    public CartItemShowDTO convertOrderToCarItemShowDTO(Order order) {
        return new CartItemShowDTO(
                order.getId(),
                order.getProduct().getProductName(),
                order.getProduct().getPrice(),
                order.getProduct().getImages().get(0).getFileName(),
                order.getQuantity(),
                order.getProduct().getRating()
        );
    }



    private void convertProductToSpecificCollection(Collection<Product> products, ImageUtility imageUtility, Collection<ProductShowSmallDTO> productShowSmallDTOs, ConverterHelper converterHelper) throws DataFormatException, IOException {
        for(Product product : products){
            ProductShowSmallDTO productShowSmallDTO = new ProductShowSmallDTO(
                    product.getProductName(),
                    product.getPrice(),
                    product.getShippingPrice(),
                    product.getDeliveryTimeInDay(),
                    product.getQuantity(),
                    product.getRating(),
                    product.getSold(),
                    product.getId(),
                    converterHelper.getProductImages(product, imageUtility),
                    product.getSellerFullName()
            );

            productShowSmallDTOs.add(productShowSmallDTO);
        }
    }

}
