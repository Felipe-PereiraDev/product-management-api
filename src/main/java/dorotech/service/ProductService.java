package dorotech.service;

import dorotech.domain.Product;
import dorotech.dto.ProductRequest;
import dorotech.dto.ProductResponse;
import dorotech.dto.ProductUpdateDTO;
import dorotech.exceptions.exception.EntityExistsException;
import dorotech.exceptions.exception.EntityNotFoundException;
import dorotech.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(ProductRequest dto) {
        try {
            Product product = dto.toEntity();
            productRepository.save(product);
            return new ProductResponse(product);
        } catch (DataIntegrityViolationException ex) {
            throw  new EntityExistsException("There is already a product with that name");
        }
    }

    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Product Not Found")
        );
        return new ProductResponse(product);
    }


    public List<ProductResponse> findAll() {
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(ProductResponse::new).toList();
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) throw new EntityNotFoundException("Product Not Found");
        productRepository.deleteById(id);
    }

    public ProductResponse updateProduct(@Valid ProductUpdateDTO dto, Long id) {
        var product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product Not Found"));
        try {
            product.update(dto);
            productRepository.save(product);
            return new ProductResponse(product);
        } catch (DataIntegrityViolationException ex) {
            throw  new EntityExistsException("There is already a product with that name");
        }
    }

}
