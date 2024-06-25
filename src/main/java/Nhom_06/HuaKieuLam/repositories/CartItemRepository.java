package Nhom_06.HuaKieuLam.repositories;

import Nhom_06.HuaKieuLam.entities.Cart;
import Nhom_06.HuaKieuLam.entities.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems, Long> {
    List<CartItems> findByCart(Cart cart);
}
