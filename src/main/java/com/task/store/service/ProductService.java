package com.task.store.service;

import com.task.store.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAllProducts();
    Product findProductById(Long id);
    Product createProduct(Product product);
    Product updateProduct(Product product);
    void deleteProductBuId(Long id);
}
