package dorotech.dto;

import dorotech.domain.Product;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Double price,
        Long amount
) {
    public ProductResponse(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getAmount()
        );
    }
}
