package Nhom_06.HuaKieuLam.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CartItems")
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartItemId")
    private int cartItemId;

    @ManyToOne
    @JoinColumn(name = "cartId")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    @Column(name = "gram")
    private Integer gram;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "totalPrice")
    private Double totalPrice;

    @Column(name = "totalCalories")
    private BigDecimal totalCalories;

    @Column(name = "totalProtein")
    private BigDecimal totalProtein;

    @Column(name = "totalCarb")
    private BigDecimal totalCarb;

    @Column(name = "totalFat")
    private BigDecimal totalFat;

    @Column(name = "totalAlcohol")
    private BigDecimal totalAlcohol;

    public Long getProductId() {
        return product != null ? product.getId() : null;
    }
}