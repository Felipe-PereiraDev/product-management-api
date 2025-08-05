package dorotech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dorotech.domain.Product;
import dorotech.dto.ProductRequest;
import dorotech.dto.ProductResponse;
import dorotech.dto.ProductUpdateDTO;
import dorotech.exceptions.exception.EntityNotFoundException;
import dorotech.mocks.ProductMock;
import dorotech.service.ProductService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private ProductService service;

    private ProductRequest productRequest;
    private Product product;
    private ProductResponse productResponse;


    @BeforeEach
    void setUp() {
        product = ProductMock.mockProduct();
        productRequest = new ProductRequest(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getAmount()
        );
        productResponse = new ProductResponse(product);
    }

    @DisplayName("create Product with Valid Data should Return Return Saved Product")
    @Test
    void createProduct_withValidData_shouldReturnReturnSavedProduct() throws Exception {

        // given
        given(service.create(any(ProductRequest.class)))
                .willAnswer((invocation) -> {
                    ProductRequest request = invocation.getArgument(0);
                    return new ProductResponse(
                            1L,
                            request.name(),
                            request.description(),
                            request.price(),
                            request.amount()
                            );
                });

        // when
        ResultActions response = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productRequest))
        );

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", CoreMatchers.is(productRequest.name())))
                .andExpect(jsonPath("$.description", CoreMatchers.is(productRequest.description())));
    }

    @DisplayName("list All Products whenCalled should Return Product Response List")
    @Test
    void listAllProducts_whenCalled_shouldReturnProductResponseList() throws Exception {
        List<ProductResponse> productResponseList = new ArrayList<>();
        productResponseList.add(new ProductResponse(product));
        productResponseList.add(new ProductResponse(
                2L,
                "Iphone 15",
                "Iphone de 256gb",
                15000D,
                10L
        ));
        // given
        given(service.findAll()).willReturn(productResponseList);

        // when
        ResultActions response = mockMvc.perform(get("/products"));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(productResponseList.size())));
    }

    @DisplayName("find Product By Id when Id Exists should Return Product Response")
    @Test
    void findProductById_whenIdExists_shouldReturnProductResponse() throws Exception {
        // given
        long productId = 1L;
        given(service.findById(anyLong())).willReturn(productResponse);

        // when
        ResultActions response = mockMvc.perform(get("/products/{id}", productId));


        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", CoreMatchers.is(productRequest.name())))
                .andExpect(jsonPath("$.description", CoreMatchers.is(productRequest.description())));
    }

    @DisplayName("find Product By Id when Id Not Exists should Return Not Found")
    @Test
    void findProductById_whenIdNotExists_shouldReturnNotFound() throws Exception {
        // given
        long productId = 1L;
        given(service.findById(anyLong())).willThrow(EntityNotFoundException.class);

        // when
        ResultActions response = mockMvc.perform(get("/products/{id}", productId));


        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("update Product By Id when Id Exists should Return Updated Product Response")
    @Test
    void updateProductById_whenIdExists_shouldReturnUpdatedProductResponse() throws Exception {
        // given
        long productId = 1L;
        ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO(
                "Notebook",
                "Notebook 256gb",
                5000D,
                20L
        );
        given(service.updateProduct(any(ProductUpdateDTO.class), anyLong())).willAnswer(
                (invocation) -> {
                    ProductUpdateDTO dto = invocation.getArgument(0);
                    product.update(dto);
                    return new ProductResponse(product);
        });

        // when
        ResultActions response = mockMvc.perform(put("/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productUpdateDTO)));
        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", CoreMatchers.is(productUpdateDTO.name())))
                .andExpect(jsonPath("$.description", CoreMatchers.is(productUpdateDTO.description())));
    }


    @DisplayName("update Product By Id when Id Not Exists should Return Not Found")
    @Test
    void updateProductById_whenIdNotExists_shouldReturnNotFound() throws Exception {
        // given
        long productId = 1L;
        ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO(
                "Notebook",
                "Notebook 256gb",
                5000D,
                20L
        );
        given(service.updateProduct(any(ProductUpdateDTO.class), anyLong())).willThrow(EntityNotFoundException.class);

        // when
        ResultActions response = mockMvc.perform(put("/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productUpdateDTO)));
        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }


    @DisplayName("delete Product By Id when Id Exists should Return No Content")
    @Test
    void deleteProductById_whenIdExists_shouldReturnNoContent() throws Exception {
        // given
        long productId = 1L;
        willDoNothing().given(service).deleteProduct(anyLong());

        // when
        ResultActions response = mockMvc.perform(delete("/products/{id}", productId));

        //then
        response.andExpect(status().isNoContent())
                .andDo(print());
    }

    @DisplayName("delete Product By Id when Id Not Exists should Return Not Found")
    @Test
    void deleteProductById_whenIdNotExists_shouldReturnNotFound() throws Exception {
        // given
        long productId = 1L;
        willThrow(EntityNotFoundException.class).given(service).deleteProduct(anyLong());

        // when
        ResultActions response = mockMvc.perform(delete("/products/{id}", productId));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }
}