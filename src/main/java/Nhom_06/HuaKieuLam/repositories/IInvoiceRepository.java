package Nhom_06.HuaKieuLam.repositories;

import Nhom_06.HuaKieuLam.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IInvoiceRepository extends JpaRepository<Invoice,
        Long>{
}
