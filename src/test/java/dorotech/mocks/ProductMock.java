package dorotech.mocks;

import dorotech.domain.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductMock {

    public static Product mockProduct() {
        return new Product(
                1L,
                "Product",
                "Description Product",
                10D,
                10L
        );
    }

    public static List<Product> mockProductList(long quantity) {
        List<Product> productList = new ArrayList<>(10);
        for (long i = 0; i < quantity; i++) {
            Product product = new Product(
                    i,
                    "Product" + i,
                    "Description Product" + i,
                    (double) i,
                    i
            );
            productList.add(product);
        }
        return productList;
    }
}
