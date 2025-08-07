package dorotech.service;

import dorotech.domain.Product;
import dorotech.dto.ProductRequest;
import dorotech.dto.ProductResponse;
import dorotech.dto.ProductUpdateDTO;
import dorotech.exceptions.exception.EntityExistsException;
import dorotech.exceptions.exception.EntityNotFoundException;
import dorotech.mocks.ProductMock;
import dorotech.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = ProductMock.mockProduct();
    }

    @DisplayName("Test Create Should Return Created Product Response")
    @Test
    void create_ShouldReturnCreatedProductResponse() {
        // Arrange
        ProductRequest productRequest = new ProductRequest(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getAmount()
        );
        String expectedName = "Product";
        String expectedDescription = "Description Product";
        Double expectedPrice = 10D;
        Long expectedAmount = 10L;
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        ProductResponse result = productService.create(productRequest);

        // Assert
        assertNotNull(result);
        assertEquals(expectedName, result.name());
        assertEquals(expectedDescription, result.description());
        assertEquals(expectedPrice, result.price());
        assertEquals(expectedAmount, result.amount());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @DisplayName("Test Create When Name Already Exists Should Throw Return Entity Exists Exception")
    @Test
    void create_whenNameAlreadyExists_ShouldThrowEntityExistsException() {
        // Arrange
        ProductRequest productRequest = new ProductRequest(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getAmount()
        );
        when(productRepository.save(any(Product.class))).thenThrow(DataIntegrityViolationException.class);

        // Act

        // Assert
        assertThrowsExactly(EntityExistsException.class,
                () -> productService.create(productRequest));
        verify(productRepository, times(1)).save(any(Product.class));
    }


    @DisplayName("Test FindById  When Id Existing  Should Return Product Response")
    @Test
    void findById_WhenIdExisting_ShouldReturnProductResponse() {
        // Arrange
        String expectedName = "Product";
        String expectedDescription = "Description Product";
        Double expectedPrice = 10D;
        Long expectedAmount = 10L;
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // Act
        var result = productService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(expectedName, result.name());
        assertEquals(expectedDescription, result.description());
        assertEquals(expectedPrice, result.price());
        assertEquals(expectedAmount, result.amount());
    }

    @DisplayName("Test FindById When Id Not Existing Should Throw EntityNotFoundException")
    @Test
    void findById_WhenIdNotExisting_ShouldThrowEntityNotFoundException() {
        // Arrange
        String expectedMessage = "Product Not Found";
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act / Assert
        EntityNotFoundException exception = assertThrowsExactly(EntityNotFoundException.class,
                () -> productService.findById(1L)
        );
        assertEquals(expectedMessage, exception.getMessage(), () -> "The Expected Message is " + expectedMessage);
    }

    @DisplayName("FindAll Should Return List Of Product Response")
    @Test
    void findAll_ShouldReturnListOfProductResponse() {
        // Arrange
        long size = 10L;
        List<Product> mockProducList = ProductMock.mockProductList(size);
        when(productRepository.findAll()).thenReturn(mockProducList);

        // Act
        List<ProductResponse> resultList = productService.findAll();

        // Assert
        assertNotNull(resultList);
        assertEquals(mockProducList.size(), resultList.size());

        for (int i = 0; i < size; i++) {
            Product originalProduct = mockProducList.get(i);
            ProductResponse resultProduct = resultList.get(i);

            assertNotNull(resultProduct);
            assertEquals(originalProduct.getName(), resultProduct.name(),
                    () -> "The Expected Name is " + originalProduct.getName());

            assertEquals(originalProduct.getDescription(), resultProduct.description(),
                    () -> "The Expected Description is " + originalProduct.getDescription());

            assertEquals(originalProduct.getPrice(), resultProduct.price(),
                    () -> "The Expected Price is " + originalProduct.getPrice());

            assertEquals(originalProduct.getAmount(), resultProduct.amount(),
                    () -> "The Expected Amount is " + originalProduct.getAmount());
        }
        verify(productRepository, times(1)).findAll();
    }

    @DisplayName("DeleteProduct When IdExists Should Call Repository Delete Method")
    @Test
    void deleteProduct_When_IdExists_ShouldCallRepositoryDeleteMethod() {
        // Arrange
        when(productRepository.existsById(anyLong())).thenReturn(true);
        // Act
        productService.deleteProduct(1L);
        // Assert
        verify(productRepository, times(1)).deleteById(anyLong());

    }

    @DisplayName("DeleteProduct  When Id Not Exists Should Throw EntityNotFoundException")
    @Test
    void deleteProduct_When_IdNotExists_ShouldThrowEntityNotFoundException() {
        // Arrange
        String expectedMessage = "Product Not Found";
        when(productRepository.existsById(anyLong())).thenReturn(false);
        // Act & Assert
        EntityNotFoundException exception = assertThrowsExactly(EntityNotFoundException.class,
                () -> productService.deleteProduct(1L));
        assertEquals(expectedMessage, exception.getMessage());
        verify(productRepository, never()).deleteById(anyLong());

    }

    @DisplayName("UpdateProduct When Id Exists Should Return Updated Product")
    @Test
    void updateProduct_When_IdExists_ShouldReturnUpdatedProduct() {
        // Arrange
        ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO(
                "Iphone 15",
                null,
                null,
                20L
        );
        Product updatedProduct = new Product(
                product.getId(),
                "Iphone 15",
                product.getDescription(),
                product.getPrice(),
                20L
        );

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        updatedProduct.update(productUpdateDTO);
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // Act
        var result = productService.updateProduct(productUpdateDTO, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(updatedProduct.getName(), result.name());
        assertEquals(updatedProduct.getDescription(), result.description());
        assertEquals(updatedProduct.getPrice(), result.price());
        assertEquals(updatedProduct.getAmount(), result.amount());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @DisplayName("UpdateProduct When Id Not Exits Should Throw EntityNotFoundException")
    @Test
    void updateProduct_When_IdNotExists_ShouldThrowEntityNotFoundException() {
        // Arrange
        String expectedMessage = "Product Not Found";
        ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO(null, null, null, null);
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrowsExactly(EntityNotFoundException.class,
                () -> productService.updateProduct(productUpdateDTO, anyLong())
        );

        assertEquals(expectedMessage, exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }
}