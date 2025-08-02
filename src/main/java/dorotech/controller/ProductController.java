package dorotech.controller;

import dorotech.dto.ProductRequest;
import dorotech.dto.ProductResponse;
import dorotech.dto.ProductUpdateDTO;
import dorotech.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest dto) {
        ProductResponse createdProduct = productService.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(createdProduct.id()).toUri();
        return ResponseEntity.created(uri).body(createdProduct);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> listAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable("id")Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductResponse> updateProductById(@PathVariable("id")Long id,
                                                             @RequestBody @Valid ProductUpdateDTO dto) {
        return ResponseEntity.ok(productService.updateProduct(dto, id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ProductResponse> deleteProductById(@PathVariable("id")Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
