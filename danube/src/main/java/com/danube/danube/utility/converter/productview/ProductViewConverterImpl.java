package com.danube.danube.utility.converter.productview;

import com.danube.danube.model.dto.order.CartItemShowDTO;
import com.danube.danube.model.dto.product.DetailDTO;
import com.danube.danube.model.dto.product.ProductItemDTO;
import com.danube.danube.model.dto.product.ProductShowSmallDTO;
import com.danube.danube.model.order.Order;
import com.danube.danube.model.product.Product;
import com.danube.danube.utility.converter.converterhelper.ConverterHelper;
import com.danube.danube.utility.converter.converterhelper.ConverterHelperImpl;
import org.hibernate.internal.util.collections.LinkedIdentityHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductViewConverterImpl implements ProductViewConverter{
    private final ConverterHelper converterHelper;

    public ProductViewConverterImpl() {
        this.converterHelper = new ConverterHelperImpl();
    }

    @Override
    public Set<ProductShowSmallDTO> convertProductToProductShowSmallDTORandomOrder(Page<Product> products){
        return products.stream()
                .map(product -> new ProductShowSmallDTO(
                        product.getProductName(),
                        product.getPrice(),
                        product.getShippingPrice(),
                        product.getDeliveryTimeInDay(),
                        product.getQuantity(),
                        product.getRating(),
                        product.getSold(),
                        product.getId(),
                        getProductImageName(product),
                        product.getSellerFullName()
                )).collect(Collectors.toSet());
    }

    @Override
    public ProductItemDTO convertProductToProductItemDTO(Product product) {
        List<DetailDTO> detailValues = product.getProductValues().stream()
                .map(productValue -> new DetailDTO(
                        productValue.getDetailName(),
                        productValue.getDetailId(),
                        productValue.getValueName()
                )).toList();


        return new ProductItemDTO(
                converterHelper.createProductInformation(product),
                converterHelper.getProductImages(product),
                detailValues
        );
    }

    @Override
    public List<ProductShowSmallDTO> convertProductsToProductShowSmallDTO(Collection<Product> products) {
        return products.stream()
                .map(product -> new ProductShowSmallDTO(
                        product.getProductName(),
                        product.getPrice(),
                        product.getShippingPrice(),
                        product.getDeliveryTimeInDay(),
                        product.getQuantity(),
                        product.getRating(),
                        product.getSold(),
                        product.getId(),
                        converterHelper.getProductImages(product),
                        product.getSellerFullName()
                )).toList();
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


    //Probably helpers
    private List<String> getProductImageName(Product product){
        return converterHelper.getProductImages(product);
    }

}
