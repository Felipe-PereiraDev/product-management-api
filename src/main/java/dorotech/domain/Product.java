package dorotech.domain;

import dorotech.dto.ProductRequest;
import dorotech.dto.ProductUpdateDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long amount;

    public Product(String name, String description, Double price, Long amount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }

    public Product(Long id, String name, String description, Double price, Long amount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }
    public Product() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public Long getAmount() {
        return amount;
    }

    public void update(ProductUpdateDTO dto) {
        if (dto.description() != null && !dto.description().isBlank()) {
            this.description = dto.description();
        }
        if (dto.price() != null && dto.price() > 0) {
            this.price = dto.price();
        }
        if (dto.name() != null && !dto.name().isBlank()) {
            this.name = dto.name();
        }
        if (dto.amount() != null && dto.amount() > 0) {
            this.amount = dto.amount();
        }
    }
}
