package Nhom_06.HuaKieuLam.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import java.util.Objects;
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "item_invoice")
public class ItemInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "quantity")
    private int quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ToString.Exclude
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    @ToString.Exclude
    private Invoice invoice;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) !=
                Hibernate.getClass(o)) return false;
        ItemInvoice that = (ItemInvoice) o;
        return getId() != null && Objects.equals(getId(),
                that.getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}


