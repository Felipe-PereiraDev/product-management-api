package dorotech.integrationtests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dorotech.config.TestConfigs;
import dorotech.domain.Product;
import dorotech.dto.ProductRequest;
import dorotech.dto.ProductResponse;
import dorotech.dto.ProductUpdateDTO;
import dorotech.integrationtests.testcontainers.AbstractIntegrationTest;
import dorotech.mocks.ProductMock;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class ProductControllerIntegrationTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    public static ObjectMapper mapper;
    private static ProductRequest productRequest;
    private static ProductResponse productResponse;

    @BeforeAll
    static void setUp() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/products")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
        productRequest = new ProductRequest(
                "Notebook",
                "Notebook da Apple com 256bg",
                20000D,
                200L
        );
        productResponse = new ProductResponse(null, null, null, null, null);
    }

    @Order(1)
    @DisplayName("JUnit integration given Product Request when create one Product should return a Product Response")
    @Test
    void integrationTestGivenPersonRequest_when_CreateOneProduct_ShouldReturnAProductResponse() throws JsonProcessingException {
        // Given  / Arrange
        String content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(productRequest)
                .when()
                    .post()
                .then()
                    .statusCode(201)
                        .extract()
                             .body()
                                 .asString();

        productResponse = mapper.readValue(content, ProductResponse.class);

        assertNotNull(productResponse);
        assertNotNull(productResponse.id());
        assertNotNull(productResponse.name());
        assertNotNull(productResponse.description());
        assertNotNull(productResponse.amount());
        assertNotNull(productResponse.price());

        assertTrue(productResponse.id() > 0);
        assertEquals(productRequest.name(), productResponse.name());
        assertEquals(productRequest.description(), productResponse.description());
        assertEquals(productRequest.price(), productResponse.price());
    }

    @Order(2)
    @DisplayName("JUnit integration Test Given Person Update DTO when update One Product Should Return A Updated Product Response")
    @Test
    void integrationTestGivenPersonUpdateDTO_when_updateOneProduct_ShouldReturnAUpdatedProductResponse() throws JsonProcessingException {
        // Given  / Arrange
        ProductUpdateDTO dto = new ProductUpdateDTO(
                "Notebook Lenovo Ideapad 3",
                "Notebook Lenovo IdeaPad 3 com tela Full HD, SSD de 256GB, 8GB de RAM e ótimo desempenho para tarefas diárias.",
                null,
                null);
        String content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(dto)
                .when()
                    .put("/{id}", productResponse.id())
                .then()
                    .statusCode(200)
                        .extract()
                            .body()
                                .asString();

        productResponse = mapper.readValue(content, ProductResponse.class);

        assertNotNull(productResponse);
        assertNotNull(productResponse.id());
        assertNotNull(productResponse.name());
        assertNotNull(productResponse.description());
        assertNotNull(productResponse.amount());
        assertNotNull(productResponse.price());

        assertTrue(productResponse.id() > 0);
        assertEquals(dto.name(), productResponse.name());
        assertEquals(dto.description(), productResponse.description());
        assertEquals(productRequest.price(), productResponse.price());
    }

    @Order(3)
    @DisplayName("JUnit integration Test Given PersonId when find By Id Should Return A Product Response")
    @Test
    void integrationTestGivenPersonId_when_findById_ShouldReturnAProductResponse() throws JsonProcessingException {
        // Given  / Arrange
        String content = given().spec(specification)
                .pathParams("id", productResponse.id())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                        .extract()
                            .body()
                                .asString();

        ProductResponse foundProduct = mapper.readValue(content, ProductResponse.class);

        assertNotNull(foundProduct);
        assertNotNull(foundProduct.id());
        assertNotNull(foundProduct.name());
        assertNotNull(foundProduct.description());
        assertNotNull(foundProduct.amount());
        assertNotNull(foundProduct.price());

        assertTrue(foundProduct.id() > 0);
        assertEquals(productResponse.name(), foundProduct.name());
        assertEquals(productResponse.description(), foundProduct.description());
        assertEquals(productResponse.price(), foundProduct.price());
    }

    @Order(4)
    @DisplayName("JUnit integration when find All Should Return A Product Response List")
    @Test
    void integrationTest_when_findAll_ShouldReturnAProductResponseList() throws JsonProcessingException {
        // Given  / Arrange
        ProductRequest AnotherProduct = new ProductRequest(
                "Iphone 16",
                "Iphone 16 com 256bg",
                17000D,
                200L
        );
        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(AnotherProduct)
                .when()
                    .post()
                .then()
                    .statusCode(201);

        String content = given().spec(specification)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                        .extract()
                            .body()
                                .asString();

        List<ProductResponse> productResponseList = mapper.readValue(content, new TypeReference<List<ProductResponse>>() {});
        assertNotNull(productResponseList);
        assertEquals(2, productResponseList.size());
        assertTrue(productResponseList.contains(productResponse));

        ProductResponse foundProduct = productResponseList.get(1);
        assertNotNull(foundProduct);
        assertNotNull(foundProduct.name());
        assertNotNull(foundProduct.description());
    }


    @Order(5)
    @DisplayName("JUnit integration Given Product Id  when delete by id Should Return Not Content")
    @Test
    void integrationTestGivenPersonId_when_deleteById_ShouldReturnNoContent() throws JsonProcessingException {
        given().spec(specification)
                .pathParam("id", productResponse.id())
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);
    }
}
