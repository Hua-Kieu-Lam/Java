package Nhom_06.HuaKieuLam.repositories;

import Nhom_06.HuaKieuLam.entities.Invoice;
import Nhom_06.HuaKieuLam.entities.Product;
import Nhom_06.HuaKieuLam.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IInvoiceRepository extends JpaRepository<Invoice, Long>
{
    @Query("SELECT i FROM Invoice i WHERE i.customerName LIKE %:keyword%")
    List<Invoice> searchOrder(@Param("keyword") String keyword);

    List<Invoice> findByCustomerName(String username);

}
