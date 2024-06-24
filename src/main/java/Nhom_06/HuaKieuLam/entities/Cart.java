package Nhom_06.HuaKieuLam.entities;

import Nhom_06.HuaKieuLam.daos.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Entity
@Table(name = "Cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartId")
    private int cartId;

    @Column(name = "totalGram")
    private Integer totalGram;

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

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    public void setUser(User user) {
        this.user = user;
    }
    public Cart() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItems> cartItems = new ArrayList<>();
}
