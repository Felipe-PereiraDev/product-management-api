package dorotech.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hello Swagger OpenAPI")
                        .version("v1")
                        .description("Description")
                        .termsOfService("")
                        .license(new License().name("Apache 2.0").url("apache.com.br")));
    }
}
