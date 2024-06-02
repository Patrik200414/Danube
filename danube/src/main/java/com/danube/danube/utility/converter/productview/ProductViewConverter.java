package com.danube.danube.utility.converter.productview;

import com.danube.danube.model.dto.order.CartItemShowDTO;
import com.danube.danube.model.dto.product.ProductItemDTO;
import com.danube.danube.model.dto.product.ProductShowSmallDTO;
import com.danube.danube.model.order.Order;
import com.danube.danube.model.product.Product;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductViewConverter {
    Set<ProductShowSmallDTO> convertProductToProductShowSmallDTORandomOrder(Page<Product> products);
    ProductItemDTO convertProductToProductItemDTO(Product product);

    List<ProductShowSmallDTO> convertProductsToProductShowSmallDTO(Collection<Product> products);
    Map<String, String> convertProductToMyProductInformation(Product product);
    CartItemShowDTO convertOrderToCarItemShowDTO(Order order);
}
