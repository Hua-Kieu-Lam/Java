package Nhom_06.HuaKieuLam.entities;
import Nhom_06.HuaKieuLam.repositories.ValidationGroups;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
@Getter

@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="customer_name")
    private String customerName;

    @Column(name="Order_name")
    private String OrderName;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "phone_number")
    @Length(min = 10, max = 10, message = "Phone must be 10 characters", groups = ValidationGroups.OnCreate.class)
    @Pattern(regexp = "^[0-9]*$", message = "Phone must be number", groups = ValidationGroups.OnCreate.class)
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "notes")
    private String notes;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "invoice_date")
    private Date invoiceDate = new Date();

    @Column(name = "total")
    private Double price;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ItemInvoice> itemInvoices = new ArrayList<>();
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) !=
                Hibernate.getClass(o)) return false;
        Invoice invoice = (Invoice) o;
        return getId() != null && Objects.equals(getId(),
                invoice.getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<ItemInvoice> items;
}
