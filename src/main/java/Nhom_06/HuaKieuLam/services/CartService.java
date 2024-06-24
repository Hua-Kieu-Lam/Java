
package Nhom_06.HuaKieuLam.services;

import Nhom_06.HuaKieuLam.daos.Cart;
import Nhom_06.HuaKieuLam.daos.Item;
import Nhom_06.HuaKieuLam.entities.*;
import Nhom_06.HuaKieuLam.repositories.*;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {Exception.class, Throwable.class})
public class CartService {
    private static final String CART_SESSION_KEY = "cart";
    private final IInvoiceRepository invoiceRepository;
    private final IItemInvoiceRepository itemInvoiceRepository;
    private final IProductRepository productRepository;
    private final CartRepository cartRepository;

    public Cart getCart(@NonNull HttpSession session) {
        return Optional.ofNullable((Cart) session.getAttribute(CART_SESSION_KEY))
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    session.setAttribute(CART_SESSION_KEY, cart);
                    return cart;
                });
    }

    public void updateCart(@NonNull HttpSession session, Cart cart) {
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void removeCart(@NonNull HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    public int getSumQuantity(@NonNull HttpSession session) {
        return getCart(session).getCartItems().stream()
                .mapToInt(Item::getQuantity)
                .sum();
    }

    public double getSumPrice(@NonNull HttpSession session) {
        return getCart(session).getCartItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

//    public void saveCart(@NonNull HttpSession session, @NonNull Invoice invoice) {
//        Cart cart = getCart(session);
//        if (cart.getCartItems().isEmpty()) return;
//
//        invoice.setInvoiceDate(new Date());
//        invoice.setPrice(getSumPrice(session));
//        invoiceRepository.save(invoice);
//
//        cart.getCartItems().forEach(item -> {
//            ItemInvoice itemInvoice = new ItemInvoice();
//            itemInvoice.setInvoice(invoice);
//            itemInvoice.setQuantity(item.getQuantity());
//            itemInvoice.setProduct(productRepository.findById(item.getProductId())
//                    .orElseThrow(() -> new IllegalArgumentException("Product not found")));
//            itemInvoiceRepository.save(itemInvoice);
//        });
//
//        removeCart(session);
//    }
    private final IUserRepository userRepository;  // Để truy vấn và lưu thông tin người dùng
    public void saveCart(@NonNull HttpSession session, Invoice invoice, @NonNull Long userId) {
        User user = userRepository.findById(String.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Cart cart = getCart(session);
        if (cart.getCartItems().isEmpty()) return;
        invoice.setInvoiceDate(new Date());
        invoice.setPrice(getSumPrice(session));
        invoice.setUser(user);

        invoiceRepository.save(invoice);

        cart.getCartItems().forEach(item -> {
            ItemInvoice itemInvoice = new ItemInvoice();
            itemInvoice.setInvoice(invoice);
            itemInvoice.setQuantity(item.getQuantity());
            itemInvoice.setProduct(productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found")));
            itemInvoiceRepository.save(itemInvoice);
        });

        removeCart(session);
    }

//    public void saveCart(@NonNull HttpSession session, @NonNull Invoice invoice,@NonNull Long userId) {
//        User user = userRepository.findById(String.valueOf(userId))
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//        Cart cart = getCart(session);
//        if (cart.getCartItems().isEmpty()) return;
//
//        invoice.setInvoiceDate(new Date());
//        invoice.setPrice(getSumPrice(session));
//        invoiceRepository.save(invoice);
//
//        cart.getCartItems().forEach(item -> {
//            ItemInvoice itemInvoice = new ItemInvoice();
//            itemInvoice.setInvoice(invoice);
//            itemInvoice.setQuantity(item.getQuantity());
//            itemInvoice.setProduct(productRepository.findById(item.getProductId())
//                    .orElseThrow(() -> new IllegalArgumentException("Product not found")));
//            itemInvoiceRepository.save(itemInvoice);
//        });
//
//        removeCart(session);
//    }
}
