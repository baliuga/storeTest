package com.task.store.service;

import com.task.store.model.Product;
import com.task.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product with id = " + id + " is not found."));
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        Product existingProduct = productRepository.findByEan(product.getEan()).orElseGet(Product::new);
        existingProduct.setPrice(product.getPrice());
        existingProduct.setName(product.getName());

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProductBuId(Long id) {
        productRepository.deleteById(id);
    }
}
