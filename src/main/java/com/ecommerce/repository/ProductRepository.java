package com.ecommerce.repository;
import com.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByActiveTrue();
    List<Product> findByActiveTrueAndNameContainingIgnoreCase(String name);
    @org.springframework.data.jpa.repository.Query(
            "SELECT p FROM Product p WHERE p.active = true AND (" +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%')))"
        )
        List<Product> searchByKeyword(@org.springframework.data.repository.query.Param("keyword") String keyword);
}