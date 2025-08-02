package dorotech.dto;

import dorotech.domain.Product;
import jakarta.validation.constraints.*;

public record ProductRequest(
        @NotBlank @Size(max = 100)
        String name,
        @NotBlank
        String description,
        @NotNull @Positive()
        Double price,
        @NotNull @Positive
        Long amount
) {
    public Product toEntity() {
        return new Product(name, description, price, amount);
    }
}
