package dorotech.integrationtests.swagger;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

import dorotech.config.TestConfigs;
import dorotech.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

    // test[SystemUnderTest]_When_[Condition or State Change]_[ExpectedResult]
    @DisplayName("test Should Display Swagger UI Page")
    @Test
    void testShouldDisplaySwaggerUIPage() {
        // Given  / Arrange
        String content = given()
                .basePath("/swagger-ui/index.html")
                .port(TestConfigs.SERVER_PORT)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();
        // Then / Assert

        assertTrue(content.contains("Swagger UI"));

    }
}
