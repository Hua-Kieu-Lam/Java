package Nhom_06.HuaKieuLam.repositories;

import Nhom_06.HuaKieuLam.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);


}
