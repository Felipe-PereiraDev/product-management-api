package dorotech.repository;

import dorotech.domain.Product;
import dorotech.mocks.ProductMock;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @DisplayName("test Given Product Object When Save Then Return Saved Product")
    @Test
    void testGivenProductObject_WhenSave_ThenReturnSavedProduct() {
        // Given  / Arrange
        Product product = new Product("Iphone 15",
                "Iphone da última geração",
                15000D,
                10L);
        // When / Act

        Product savedProduct = repository.save(product);

        // Then / Assert
        assertNotNull(savedProduct);
        assertTrue(savedProduct.getId() > 0);
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getPrice(), savedProduct.getPrice());
        assertEquals(product.getAmount(), savedProduct.getAmount());
        assertEquals(product.getDescription(), savedProduct.getDescription());
    }

    @DisplayName("test Given Product List When Find All Then Return Product List")
    @Test
    void testGivenProductList_WhenFindAll_ThenReturnProductList() {
        // Given  / Arrange
        Product product0 = new Product("Iphone 15",
                "Iphone da última geração",
                15000D,
                10L);
        Product product1 = new Product("Notebook",
                "Notebook Lenovo Ideapad 3 256gb e 8gb de memória RAM",
                4000D,
                20L);
        repository.save(product0);
        repository.save(product1);
        // When / Act

        List<Product> productList = repository.findAll();

        // Then / Assert
        assertNotNull(productList);
        assertEquals(2, productList.size());

    }

    @DisplayName("test Given Product Object When Save Then Return Saved Product")
    @Test
    void testGivenProductObject_WhenFindById_ThenReturnProductObject() {
        // Given  / Arrange
        Product product = new Product("Iphone 15",
                "Iphone da última geração",
                15000D,
                10L);
        // When / Act

        Product savedProduct = repository.save(product);

        // Then / Assert
        assertNotNull(savedProduct);
        assertEquals(product.getId(), savedProduct.getId());
    }

}