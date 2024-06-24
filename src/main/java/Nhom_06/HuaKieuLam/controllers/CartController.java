package Nhom_06.HuaKieuLam.controllers;

import Nhom_06.HuaKieuLam.entities.Invoice;
import Nhom_06.HuaKieuLam.services.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

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

