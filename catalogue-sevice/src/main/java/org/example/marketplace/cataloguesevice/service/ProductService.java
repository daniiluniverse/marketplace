package org.example.marketplace.cataloguesevice.service;

import org.example.marketplace.cataloguesevice.entity.Product;

import java.util.List;

public interface ProductService {

    public Product saveProduct(Product product);

    public Product getProduct(Long id);

    public List<Product> getAllProducts();

    public Product updateProduct(Product product);

    public void deleteProduct(Long id);
}
