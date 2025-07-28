package org.example.marketplace.cataloguesevice.service;

import org.example.marketplace.cataloguesevice.dto.ProductUpdateRequest;
import org.example.marketplace.cataloguesevice.dto.RequestProduct;
import org.example.marketplace.cataloguesevice.entity.Product;

import java.util.List;

public interface ProductService {

    public Product saveProduct(RequestProduct request);

    public Product getProduct(Long id);

    public List<Product> getAllProducts();

    public Product updateProduct(Long id, ProductUpdateRequest request);

    public void deleteProduct(Long id);
}
