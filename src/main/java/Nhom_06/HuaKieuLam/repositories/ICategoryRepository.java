package Nhom_06.HuaKieuLam.repositories;

import Nhom_06.HuaKieuLam.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {}

