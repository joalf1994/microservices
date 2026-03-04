package com.jbobadilla.product_service.repository;

import com.jbobadilla.product_service.model.entities.Product;
import com.jbobadilla.product_service.util.ProductDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        product = ProductDataFactory.getMockProduct().build();
    }

    @Test
    @DisplayName("Junit test for save product")
    void shouldReturnSavedProductWhenSaveObjectProduct() {
        Product productSaved = productRepository.save(product);

        assertThat(productSaved).isNotNull();
        assertThat(productSaved.getId()).isPositive();
        assertThat(productSaved.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Junit test for findall products")
    void shouldReturnListProduct() {
        Product prod2 = Product.builder()
                .sku("sku2")
                .name("Test Product 02")
                .description("Test Product 02")
                .price(120.0)
                .status(true).build();

        productRepository.save(product);
        productRepository.save(prod2);

        List<Product> products = productRepository.findAll();

        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(2);
    }
    
    @Test
    @DisplayName("junit test for get product by ID")
    void shouldReturnProductWhenIdProductExists() {
        productRepository.save(product);
        
        Product productdb = productRepository.findById(product.getId()).get();
    
        assertThat(productdb).isNotNull();
    }
    
    @Test
    @DisplayName("Junit test for update product test")
    void shouldReturnProductWhenUpdateProduct() {
        productRepository.save(product);

        Product productdb = productRepository.findById(product.getId()).get();
        productdb.setName("Test Product 02");
        productdb.setDescription("Test Product 02");
        productdb.setPrice(120.0);
        productdb.setStatus(true);
        Product updated = productRepository.save(productdb);

        assertThat(updated.getName()).isEqualTo("Test Product 02");
        assertThat(updated.getDescription()).isEqualTo("Test Product 02");
    }
    
    @Test
    @DisplayName("Junit test for delete product")
    void junitTestForDeleteProduct() {
        productRepository.save(product);

        productRepository.deleteById(product.getId());

        assertThat(productRepository.findById(product.getId())).isEmpty();
    }
    
    
    
}