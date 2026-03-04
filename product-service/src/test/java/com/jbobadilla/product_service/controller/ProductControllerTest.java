package com.jbobadilla.product_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbobadilla.product_service.exception.ProductNotFoundException;
import com.jbobadilla.product_service.model.dtos.ProductRequest;
import com.jbobadilla.product_service.model.dtos.ProductResponse;
import com.jbobadilla.product_service.service.ProductService;
import com.jbobadilla.product_service.util.ProductDataFactory;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @MockitoBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductResponse productResponse;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        productResponse = ProductDataFactory.getMockProductResponse().build();
        productRequest = ProductDataFactory.getMockProductRequest().build();
    }

    @Test
    @DisplayName("Should return 201 created then save product")
    void shouldReturnCreatedThenSaveProduct() throws Exception {
        given(productService.createProduct(any(ProductRequest.class))).willReturn(productResponse);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/products/" + productResponse.getId()))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(productResponse.getName()));

        verify(productService).createProduct(any(ProductRequest.class));
    }

    @Test
    @DisplayName("Should return 200 then product exists")
    void shouldReturn200ThenProductExists() throws Exception {
        given(productService.findById(1L)).willReturn(productResponse);

        mockMvc.perform(get("/api/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(productResponse.getId()))
                .andExpect(jsonPath("$.name").value(productResponse.getName()));

        verify(productService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should Not found exception(404) when product id not exists")
    void shouldNotFoundExceptionWhenProductIdNotExists() throws Exception {

        given(productService.findById(99L))
                .willThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(get("/api/products/{id}", 99L))
                .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Product not found"));

        verify(productService).findById(any());
    }

    @Test
    @DisplayName("Should return 500 when an unexpected error occurs")
    void shouldReturn500WhenUnexpectedErrorOccurs() throws Exception {
        given(productService.findById(1L))
                .willThrow(new RuntimeException("Database is down or connection lost"));

        mockMvc.perform(get("/api/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Database is down or connection lost"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(productService).findById(1L);
    }

    @Test
    @DisplayName("Should return 200 all products when products exists")
    void shouldReturn200AllProductsWhenProductsExists() throws Exception {
        ProductResponse productResponse2 = ProductResponse.builder()
                .id(2L)
                .sku("sku2")
                .name("Product 2")
                .description("Product 2")
                .price(1500.0)
                .status(true)
                .build();
        List<ProductResponse> productResponseList = List.of(productResponse,  productResponse2);
        given(productService.findAll()).willReturn(productResponseList);

        ResultActions result =  mockMvc.perform(get("/api/products"));

        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(productResponseList.size())));

    }

    @Test
    @DisplayName("Should return 200 when update product succeeds")
    void shouldReturn200WhenUpdateProductSucceeds() throws Exception {
        given(productService.updateProduct(eq(1L), any(ProductRequest.class)))
                .willReturn(productResponse);

        mockMvc.perform(put("/api/products/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(productResponse.getId()))
                .andExpect(jsonPath("$.name").value(productResponse.getName()));

        verify(productService).updateProduct(eq(1L), any(ProductRequest.class));
    }

    @Test
    @DisplayName("Should return 204 when delete product succeeds")
    void shouldReturn204WhenDeleteProductSucceeds() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent product")
    void shouldReturn404WhenDeletingNonExistentProduct() throws Exception {

        doThrow(new ProductNotFoundException("Product not found"))
                .when(productService).deleteProduct(99L);

        mockMvc.perform(delete("/api/products/{id}", 99L))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Product not found"));
    }
}
