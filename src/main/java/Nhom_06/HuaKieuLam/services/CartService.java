
package Nhom_06.HuaKieuLam.services;

import Nhom_06.HuaKieuLam.daos.Cart;
import Nhom_06.HuaKieuLam.daos.Item;
import Nhom_06.HuaKieuLam.entities.Invoice;
import Nhom_06.HuaKieuLam.entities.ItemInvoice;
import Nhom_06.HuaKieuLam.entities.Product;
import Nhom_06.HuaKieuLam.entities.User;
import Nhom_06.HuaKieuLam.repositories.IInvoiceRepository;
import Nhom_06.HuaKieuLam.repositories.IItemInvoiceRepository;
import Nhom_06.HuaKieuLam.repositories.IProductRepository;
import Nhom_06.HuaKieuLam.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {Exception.class, Throwable.class})
public class CartService {
    private static final String CART_SESSION_KEY = "cart";
    private final IInvoiceRepository invoiceRepository;
    private final IItemInvoiceRepository itemInvoiceRepository;
    private final IProductRepository productRepository;
    private final UserRepository userRepository;

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

    public void saveCart(@NonNull HttpSession session, @NonNull Invoice invoice) {
        Cart cart = getCart(session);
        if (cart.getCartItems().isEmpty()) return;

        invoice.setInvoiceDate(new Date());
        invoice.setPrice(getSumPrice(session));
        invoice.setStatus("Pending"); // Đặt trạng thái ban đầu là Pending
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

    //tạo list order

    // Phương thức để lấy danh sách đơn hàng theo username
    public List<Invoice> getOrdersByUsername(String username) {
        return invoiceRepository.findByCustomerName(username);
    }

    // Phương thức để lấy tất cả đơn hàng (cho admin)
    public List<Invoice> getAllOrders() {
        return invoiceRepository.findAll();
    }

    //tim kiếm theo tên người mua hàng
    public List<Invoice> searchOrders(String keyword) {
        return invoiceRepository.searchOrder(keyword);
    }

    //chi tiết đơn hàng
    public Invoice getOrderById(Long id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    //sự kiện xác nhận đơn hàng
    // Các phương thức khác để cập nhật trạng thái đơn hàng
    public void confirmOrder(Long orderId) {
        Invoice invoice = invoiceRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        invoice.setStatus("Confirmed");
        invoiceRepository.save(invoice);
    }

    public void shipOrder(Long orderId) {
        Invoice invoice = invoiceRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        invoice.setStatus("Shipped");
        invoiceRepository.save(invoice);
    }

    public void deliverOrder(Long orderId) {
        Invoice invoice = invoiceRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        invoice.setStatus("Delivered");
        invoiceRepository.save(invoice);
    }

    public void completeOrder(Long orderId) {
        Invoice invoice = invoiceRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        invoice.setStatus("Completed");
        invoiceRepository.save(invoice);
    }

    public void cancelOrder(Long orderId) {
        Invoice invoice = invoiceRepository.findById(orderId).
                orElseThrow(() -> new IllegalArgumentException("Order not found"));
        invoice.setStatus("Cancel Order");
        invoiceRepository.save(invoice);
    }

    public void cancelConfirmOrder(Long orderId) {
        Invoice invoice = invoiceRepository.findById(orderId).
                orElseThrow(() -> new IllegalArgumentException("Order not found"));
        invoice.setStatus("Cancel Success");
        invoiceRepository.save(invoice);
    }

}

