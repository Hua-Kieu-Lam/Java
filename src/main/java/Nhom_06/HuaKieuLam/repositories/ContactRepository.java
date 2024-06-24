package Nhom_06.HuaKieuLam.repositories;

import Nhom_06.HuaKieuLam.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}

