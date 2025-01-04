package Nhom_06.HuaKieuLam.controllers;
import Nhom_06.HuaKieuLam.entities.Invoice;
import Nhom_06.HuaKieuLam.entities.User;
import Nhom_06.HuaKieuLam.repositories.IInvoiceRepository;
import Nhom_06.HuaKieuLam.services.CartService;
import Nhom_06.HuaKieuLam.services.UserService;
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
import java.util.Optional;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final UserService userService;
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
    public String checkout(HttpSession session, Model model, Authentication authentication) {
        double totalAmount = cartService.getSumPrice(session);
        model.addAttribute("totalAmount", totalAmount);

        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            model.addAttribute("customerName", user.getUsername());
        } else {
            model.addAttribute("customerName", "");
        }

        return "cart/checkout";
    }


    @PostMapping("/submit")
    public String submitOrder(
            @RequestParam String customerName,
            @RequestParam String OrderName,
            @RequestParam String shippingAddress,
            @RequestParam String phoneNumber,
            @RequestParam String email,
            @RequestParam String notes,
            @RequestParam String paymentMethod,
            HttpSession session) {

        Invoice invoice = Invoice.builder()
                .customerName(customerName)
                .OrderName(OrderName)
                .shippingAddress(shippingAddress)
                .phoneNumber(phoneNumber)
                .email(email)
                .notes(notes)
                .paymentMethod(paymentMethod)
                .status("Pending")
                .build();

        cartService.saveCart(session, invoice);

        session.setAttribute("invoice", invoice);

        return "redirect:/cart/confirmation";
    }

    @GetMapping("/confirmation")
    public String orderConfirmation(HttpSession session, Model model) {
        Invoice invoice = (Invoice) session.getAttribute("invoice");
        if (invoice == null) {
            return "redirect:/cart";
        }

        model.addAttribute("invoice", invoice);

        session.removeAttribute("invoice");

        return "cart/order-confirmation";
    }

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

    @GetMapping("/listOrder")
    public String getListOrders(Model model, Authentication authentication) {
        String username = authentication.getName();

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            List<Invoice> orders = cartService.getAllOrders();
            model.addAttribute("orders", orders);
        } else {
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

}

