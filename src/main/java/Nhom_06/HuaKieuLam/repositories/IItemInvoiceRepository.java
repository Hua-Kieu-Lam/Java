package Nhom_06.HuaKieuLam.repositories;

import Nhom_06.HuaKieuLam.entities.ItemInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IItemInvoiceRepository extends
        JpaRepository<ItemInvoice, Long>{
}
