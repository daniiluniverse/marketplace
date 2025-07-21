package org.example.marketplace.cataloguesevice.repo;

import org.example.marketplace.cataloguesevice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
