package Nhom_06.HuaKieuLam.repositories;

import Nhom_06.HuaKieuLam.entities.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends PagingAndSortingRepository<Product, Long>, JpaRepository<Product, Long> {
    default List<Product> findAllProducts(Integer pageNo,
                                       Integer pageSize,
                                       String sortBy) {
        return findAll(PageRequest.of(pageNo,
                pageSize,
                Sort.by(sortBy)))
                .getContent();
    }
    @Query("""
 SELECT p FROM Product p
 WHERE p.title LIKE %?1%
 OR p.category.name LIKE %?1%
 """)
    List<Product> searchProduct(String keyword);
    @Modifying
    @Query("UPDATE Product b SET b.imageUrl = :imageUrl WHERE b.id = :id")
    void save(@Param("id") Long id, @Param("image") String imageUrl);

    Optional<Product> findByTitle(String title);
}
