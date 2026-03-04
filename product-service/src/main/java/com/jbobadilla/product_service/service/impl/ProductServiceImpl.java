package com.jbobadilla.product_service.service.impl;

import com.jbobadilla.product_service.exception.ProductNotFoundException;
import com.jbobadilla.product_service.model.dtos.ProductRequest;
import com.jbobadilla.product_service.model.dtos.ProductResponse;
import com.jbobadilla.product_service.model.entities.Product;
import com.jbobadilla.product_service.model.mapper.ProductMapper;
import com.jbobadilla.product_service.repository.ProductRepository;
import com.jbobadilla.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Override
    public ProductResponse findById(Long id) {
        Product  product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return productMapper.mapToProductResponse(product);
    }

    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::mapToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = productMapper.mapToProduct(productRequest);
        return productMapper.mapToProductResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        product.setSku(productRequest.getSku());
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStatus(productRequest.getStatus());
        return productMapper.mapToProductResponse(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }
}
