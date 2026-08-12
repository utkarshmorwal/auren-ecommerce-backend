package com.ecommerce.controller;
import com.ecommerce.model.Product;
import com.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/admin/all")
    public List<Product> getAllProductsAdmin() {
        return productService.getAllProductsAdmin();
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String name) {
        return productService.searchProducts(name);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @PostMapping
    public Product addProduct(@Valid @RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        Product existingProduct = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setOriginalPrice(product.getOriginalPrice());
        existingProduct.setImageUrl(product.getImageUrl());
        existingProduct.setImageUrls(product.getImageUrls());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setColor(product.getColor());
        existingProduct.setStock(product.getStock());
        existingProduct.setHighlights(product.getHighlights());
        existingProduct.setSizes(product.getSizes());
        existingProduct.setSpecifications(product.getSpecifications());
        existingProduct.setRating(product.getRating());
        existingProduct.setReviewCount(product.getReviewCount());
        existingProduct.setDeliveryDays(product.getDeliveryDays());
        return productService.saveProduct(existingProduct);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deactivateProduct(id);
        return "Product deactivated successfully";
    }

    @PutMapping("/{id}/restore")
    public String restoreProduct(@PathVariable Long id) {
        productService.reactivateProduct(id);
        return "Product restored successfully";
    }
}