package Nhom_06.HuaKieuLam.repositories;

import Nhom_06.HuaKieuLam.entities.Cart;
import Nhom_06.HuaKieuLam.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
