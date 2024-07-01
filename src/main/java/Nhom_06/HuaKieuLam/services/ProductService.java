package Nhom_06.HuaKieuLam.services;

import Nhom_06.HuaKieuLam.entities.Product;
import Nhom_06.HuaKieuLam.repositories.IProductRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class ProductService {
    private final IProductRepository productRepository;
    public List<Product> getAllProducts(Integer pageNo,
                                  Integer pageSize,
                                  String sortBy) {
        return productRepository.findAllProducts(pageNo, pageSize, sortBy);
    }
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void updateProduct(@NonNull Product product) {
        Product existingProduct = productRepository.findById(product.getId())
                .orElse(null);
        Objects.requireNonNull(existingProduct).setTitle(product.getTitle());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setImageUrl(product.getImageUrl());
        existingProduct.setCaloriesPerGram(product.getCaloriesPerGram());
        existingProduct.setProtein(product.getProtein());
        existingProduct.setCarbs(product.getCarbs());
        existingProduct.setFat(product.getFat());
        existingProduct.setAlcohol(product.getAlcohol());

        productRepository.save(existingProduct);
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> searchProduct(String keyword) {
        return productRepository.searchProduct(keyword);
    }
}

