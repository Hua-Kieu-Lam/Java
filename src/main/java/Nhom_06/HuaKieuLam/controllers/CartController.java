package Nhom_06.HuaKieuLam.controllers;

import Nhom_06.HuaKieuLam.entities.Invoice;
import Nhom_06.HuaKieuLam.repositories.IInvoiceRepository;
import Nhom_06.HuaKieuLam.services.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final IInvoiceRepository invoiceRepository;

    @GetMapping
    public String showCart(HttpSession session, @NonNull Model model) {
        model.addAttribute("cart", cartService.getCart(session));
        model.addAttribute("totalPrice", cartService.getSumPrice(session));
        model.addAttribute("totalQuantity", cartService.getSumQuantity(session));
        return "cart/cart";
    }

    @GetMapping("/removeFromCart/{id}")
    public String removeFromCart(HttpSession session, @PathVariable Long id) {
        var cart = cartService.getCart(session);
        cart.removeItems(id);
        cartService.updateCart(session, cart);
        return "redirect:/cart";
    }

    @GetMapping("/updateCart/{id}/{quantity}")
    public String updateCart(HttpSession session, @PathVariable long id, @PathVariable int quantity) {
        var cart = cartService.getCart(session);
        cart.updateItems(id, quantity);
        cartService.updateCart(session, cart);
        return "redirect:/cart";
    }

    @GetMapping("/clearCart")
    public String clearCart(HttpSession session) {
        cartService.removeCart(session);
        return "redirect:/cart";
    }
    @GetMapping("/checkout")
    public String checkout() {
        return "cart/checkout";
    }
//    @PostMapping("/submit")
//    public String submitOrder(
//            @RequestParam String customerName,
//            @RequestParam String shippingAddress,
//            @RequestParam String phoneNumber,
//            @RequestParam String email,
//            @RequestParam String notes,
//            @RequestParam String paymentMethod,
//            HttpSession session) {
//
//        Invoice invoice = Invoice.builder()
//                .customerName(customerName)
//                .shippingAddress(shippingAddress)
//                .phoneNumber(phoneNumber)
//                .email(email)
//                .notes(notes)
//                .paymentMethod(paymentMethod)
//                .build();
//
//        cartService.saveCart(session, invoice);
//        return "redirect:/cart/confirmation";
//    }
@PostMapping("/submit")
public String submitOrder(
        @RequestParam String customerName,
        @RequestParam String shippingAddress,
        @RequestParam String phoneNumber,
        @RequestParam String email,
        @RequestParam String notes,
        @RequestParam String paymentMethod,
        @RequestParam Long userId, // Add userId parameter
        HttpSession session) {

    // Create an Invoice object with customer details
    Invoice invoice = Invoice.builder()
            .customerName(customerName)
            .shippingAddress(shippingAddress)
            .phoneNumber(phoneNumber)
            .email(email)
            .notes(notes)
            .paymentMethod(paymentMethod)
            .build();

    // Retrieve cart from session and save it as an invoice associated with userId
    cartService.saveCart(session, invoice, userId);

    // Redirect to order confirmation page
    return "redirect:/cart/confirmation";
}

    @PostMapping("/submit")
    public String submitOrder(
            @RequestParam String customerName,
            @RequestParam String shippingAddress,
            @RequestParam String phoneNumber,
            @RequestParam String email,
            @RequestParam String notes,
            @RequestParam String paymentMethod,
            HttpSession session) {

        // Create invoice from the form data
        Invoice invoice = Invoice.builder()
                .customerName(customerName)
                .shippingAddress(shippingAddress)
                .phoneNumber(phoneNumber)
                .email(email)
                .notes(notes)
                .paymentMethod(paymentMethod)
                .status("Pending")
                .build();

        // Save the order information in the session
        cartService.saveCart(session, invoice);

        // Save the invoice to session for confirmation display
        session.setAttribute("invoice", invoice);

        return "redirect:/cart/confirmation";
    }

    @GetMapping("/confirmation")
    public String orderConfirmation(HttpSession session, Model model) {
        // Get the order information from the session
        Invoice invoice = (Invoice) session.getAttribute("invoice");
        if (invoice == null) {
            // If no order information, redirect to the cart page
            return "redirect:/cart";
        }

        // Display order information on the order-confirmation page
        model.addAttribute("invoice", invoice);

        // Remove the order information from the session after displaying
        session.removeAttribute("invoice");

        return "cart/order-confirmation";
    }

    //tạo list order

    //tìm kiếm
    @GetMapping("/search")
    public String getListOrders(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Invoice> orders;
        if (keyword != null && !keyword.isEmpty()) {
            orders = cartService.searchOrders(keyword);
        } else {
            orders = cartService.getAllOrders();
        }
        model.addAttribute("orders", orders);
        model.addAttribute("keyword", keyword);
        return "cart/listOrder";
    }

    //danh sách mua hàng(admin, người dùng)
    @GetMapping("/listOrder")
    public String getListOrders(Model model, Authentication authentication) {
        // Lấy thông tin người dùng hiện tại từ Authentication
        String username = authentication.getName();

        // Kiểm tra vai trò của người dùng để hiển thị danh sách đơn hàng tương ứng
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            // Nếu là admin, lấy danh sách đơn hàng cho admin
            List<Invoice> orders = cartService.getAllOrders();
            model.addAttribute("orders", orders);
        } else {
            // Nếu là user, lấy danh sách đơn hàng của user hiện tại
            List<Invoice> orders = cartService.getOrdersByUsername(username);
            model.addAttribute("orders", orders);
        }
        return "cart/listOrder";
    }

    //chi tiết đơn hàng
    @GetMapping("/order/{id}")
    public String viewOrderDetails(@PathVariable("id") Long id, Model model) {
        Invoice order = cartService.getOrderById(id);
        model.addAttribute("order", order);
        return "cart/orderDetails";
    }

    //trạng thái đơn hàng
    @GetMapping("/confirm/{id}")
    public String confirmOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cartService.confirmOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order confirmed successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/cart/listOrder";
    }

    @GetMapping("/ship/{id}")
    public String shipOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cartService.shipOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order shipped successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/cart/listOrder";
    }

    @GetMapping("/deliver/{id}")
    public String deliverOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cartService.deliverOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order delivered successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/cart/listOrder";
    }

    @GetMapping("/complete/{id}")
    public String completeOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cartService.completeOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order completed successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/cart/listOrder";
    }

    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cartService.cancelOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order has been requested to be cancelled.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/cart/listOrder";
    }

    @PostMapping("/cancel-confirm")
    public String cancelConfirmOrder(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            cartService.cancelConfirmOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Confirm successful order cancellation.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/cart/listOrder";
    }

    private void confirmOrderStatus(Long id, String status) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        invoice.setStatus(status);
        invoiceRepository.save(invoice);
    }

=======
//    @GetMapping("/confirmation")
//    public String orderConfirmation(HttpSession session, Invoice invoice,Model model) {
//        cartService.saveCart(session, invoice);
//        model.addAttribute("message", "Your order has been successfully placed.");
//        cartService.removeCart(session);
//        return "cart/order-confirmation";
//    }
@GetMapping("/confirmation")
public String orderConfirmation(HttpSession session, Model model) {
    // Retrieve invoice from session (if needed) and show confirmation message
    model.addAttribute("message", "Your order has been successfully placed.");
    cartService.removeCart(session);
    return "cart/order-confirmation";
}
//
//    @PostMapping("/submit")
//    public String submitOrder(String customerName, HttpSession session) {
//        cartService.saveCart(session);
//        return "redirect:/cart/order-confirmation";
//    }

}

