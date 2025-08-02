package dorotech.dto;

import dorotech.domain.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductUpdateDTO(
        String name,
        String description,
        @Positive
        Double price,
        @Positive
        Long amount
) {
}
