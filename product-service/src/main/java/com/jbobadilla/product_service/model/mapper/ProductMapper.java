package com.jbobadilla.product_service.model.mapper;

import com.jbobadilla.product_service.model.dtos.ProductRequest;
import com.jbobadilla.product_service.model.dtos.ProductResponse;
import com.jbobadilla.product_service.model.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product mapToProduct(ProductRequest productRequest);
    ProductResponse mapToProductResponse(Product product);
}
