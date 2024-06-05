package Nhom_06.HuaKieuLam.daos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Long productId;
    private String productName;
    private Double price;
    private int quantity;
}

