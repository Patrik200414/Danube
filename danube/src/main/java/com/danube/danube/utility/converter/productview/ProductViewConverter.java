package com.danube.danube.utility.converter.productview;

import com.danube.danube.model.dto.order.CartItemShowDTO;
import com.danube.danube.model.dto.product.MyProductInformationDTO;
import com.danube.danube.model.dto.product.ProductItemDTO;
import com.danube.danube.model.dto.product.ProductShowSmallDTO;
import com.danube.danube.model.order.Order;
import com.danube.danube.model.product.Product;
import com.danube.danube.utility.converter.converterhelper.ConverterHelper;
import com.danube.danube.utility.imageutility.ImageUtility;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.DataFormatException;

public interface ProductViewConverter {
    Set<ProductShowSmallDTO> convertProductToProductShowSmallDTORandomOrder(Page<Product> products, ImageUtility imageUtility, ConverterHelper converterHelper) throws DataFormatException, IOException;
    ProductItemDTO convertProductToProductItemDTO(Product product, ImageUtility imageUtility, ConverterHelper converterHelper) throws DataFormatException, IOException;

    List<ProductShowSmallDTO> convertProductsToProductShowSmallDTO(Collection<Product> products, ImageUtility imageUtility, ConverterHelper converterHelper) throws DataFormatException, IOException;
    MyProductInformationDTO convertProductToMyProductInformation(Product product, ImageUtility imageUtility) throws DataFormatException, IOException;
    CartItemShowDTO convertOrderToCarItemShowDTO(Order order);
}
