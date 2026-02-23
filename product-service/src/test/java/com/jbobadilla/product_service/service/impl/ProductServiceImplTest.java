package com.jbobadilla.product_service.service.impl;

import com.jbobadilla.product_service.exception.NotFoundException;
import com.jbobadilla.product_service.model.dtos.ProductRequest;
import com.jbobadilla.product_service.model.dtos.ProductResponse;
import com.jbobadilla.product_service.model.entities.Product;
import com.jbobadilla.product_service.model.mapper.ProductMapper;
import com.jbobadilla.product_service.repository.ProductRepository;
import com.jbobadilla.product_service.util.ProductDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductRepository productRepository;

    private Product product;
    private ProductResponse productResponse;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        //inicializamos el objeto
        product = ProductDataFactory.getMockProduct().build();
        productResponse = ProductDataFactory.getMockProductResponse().build();
        productRequest = ProductDataFactory.getMockProductRequest().build();

    }

    @Test
    @DisplayName("Should return ProductResponse When ID correct")
    void testFindById() {

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.mapToProductResponse(product)).thenReturn(productResponse);

        ProductResponse response = productService.findById(1L);

        assertThat(response)
                .isNotNull()
                .isEqualTo(productResponse);

        assertThat(response.getId()).isEqualTo(1L);

        verify(productRepository).findById(1L);
        verify(productMapper).mapToProductResponse(product);
    }
    
    @Test
    @DisplayName("Should ThrowException When ID Not Found")
    void shouldThrowExceptionWhenIdNotFound() {

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Product not found");

        verify(productRepository).findById(99L);
        verify(productMapper, never()).mapToProductResponse(any());
    }
    

    @Test
    @DisplayName("Should return a list of ProductResponse When products exist")
    void shouldReturnProductResponseListWhenProductsExist() {

        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productMapper.mapToProductResponse(product)).thenReturn(productResponse);

        List<ProductResponse> productResponseList = productService.findAll();

        assertThat(productResponseList)
                .isNotNull()
                .hasSize(1)
                .isEqualTo(List.of(productResponse));

        verify(productRepository).findAll();
        verify(productMapper).mapToProductResponse(product);
    }
    
    @Test
    @DisplayName("Should return empty list When no products exist")
    void shouldReturnEmptyListWhenNoProductsExist() {
        when(productRepository.findAll()).thenReturn(List.of());

        List<ProductResponse> productResponseList = productService.findAll();

        assertThat(productResponseList)
                .isNotNull()
                .isEmpty();
    
        verify(productRepository).findAll();
        verify(productMapper, never()).mapToProductResponse(any());
    }
    

    @Test
    @DisplayName("Should return productResponse When save product")
    void shouldReturnProductResponseWhenSaveProduct() {
        when(productMapper.mapToProduct(productRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.mapToProductResponse(product)).thenReturn(productResponse);

        ProductResponse saved = productService.createProduct(productRequest);

        assertThat(saved)
                .isNotNull()
                .isEqualTo(productResponse);

        verify(productRepository).save(product);
        verify(productMapper).mapToProductResponse(product);
        verify(productMapper).mapToProduct(productRequest);
    }

    @Test
    @DisplayName("Should updateProduct When productRequest exists")
    void shouldUpdateProductWhenProductRequestExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.mapToProductResponse(product)).thenReturn(productResponse);

        ProductResponse updated = productService.updateProduct(1L, productRequest);

        assertThat(updated)
                .isNotNull()
                .isEqualTo(productResponse);
        assertThat(updated.getName()).isEqualTo(productRequest.getName());

        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
        verify(productMapper).mapToProductResponse(any(Product.class));
    }
    
    @Test
    @DisplayName("Should throw exception when Id product no exists")
    void shouldThrowExceptionWhenIdProductNoExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(1L, productRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Product not found");

        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any());
        verify(productMapper, never()).mapToProductResponse(any());
    }

    @Test
    @DisplayName("Should delete product when ID exist")
    void shouldDeleteProductWhenIdExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository).findById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should ThrowException When no product exist")
    void shouldThrowExceptionWhenNoProductExist() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.deleteProduct(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Product not found");

        verify(productRepository).findById(1L);
    }

}