    package Nhom_06.HuaKieuLam.services;
    import Nhom_06.HuaKieuLam.daos.Cart;
    import Nhom_06.HuaKieuLam.daos.Item;
    import Nhom_06.HuaKieuLam.entities.Invoice;
    import Nhom_06.HuaKieuLam.entities.ItemInvoice;
    import Nhom_06.HuaKieuLam.repositories.IInvoiceRepository;
    import Nhom_06.HuaKieuLam.repositories.IItemInvoiceRepository;
    import Nhom_06.HuaKieuLam.repositories.IProductRepository;
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
    @Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = {Exception.class, Throwable.class})

    public class CartService {
        private static final String CART_SESSION_KEY = "cart";
        private final IInvoiceRepository invoiceRepository;
        private final IItemInvoiceRepository itemInvoiceRepository;
        private final IProductRepository productRepository;

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
    //    public void saveCart(@NonNull HttpSession session) {
    //        var cart = getCart(session);
    //        if (cart.getCartItems().isEmpty()) return;
    //        var invoice = new Invoice();
    //        invoice.setInvoiceDate(new Date(new Date().getTime()));
    //        invoice.setPrice(getSumPrice(session));
    //        invoiceRepository.save(invoice);
    //        cart.getCartItems().forEach(item -> {
    //            var items = new ItemInvoice();
    //            items.setInvoice(invoice);
    //            items.setQuantity(item.getQuantity());
    //            items.setProduct(productRepository.findById(item.getProductId())
    //
    //                    .orElseThrow());
    //            itemInvoiceRepository.save(items);
    //        });
    //        removeCart(session);
    //    }
//    public void saveCart(@NonNull HttpSession session) {
//        var cart = getCart(session);
//        if (cart.getCartItems().isEmpty()) return;
//        var invoice = new Invoice();
//        invoice.setInvoiceDate(new Date(new Date().getTime()));
//        invoice.setPrice(getSumPrice(session));
//        invoiceRepository.save(invoice);
//        cart.getCartItems().forEach(item -> {
//            var items = new ItemInvoice();
//            items.setInvoice(invoice);
//            items.setQuantity(item.getQuantity());
//            items.setProduct(productRepository.findById(item.getProductId())
//                    .orElseThrow());
//            itemInvoiceRepository.save(items);
//        });
//        removeCart(session);
//    }
    public void saveCart(@NonNull HttpSession session, Invoice invoice) {
        var cart = getCart(session);
        if (cart.getCartItems().isEmpty()) return;

        invoice.setInvoiceDate(new Date());
        invoice.setPrice(getSumPrice(session));
        invoiceRepository.save(invoice);

        cart.getCartItems().forEach(item -> {
            var itemInvoice = new ItemInvoice();
            itemInvoice.setInvoice(invoice);
            itemInvoice.setQuantity(item.getQuantity());
            itemInvoice.setProduct(productRepository.findById(item.getProductId())
                    .orElseThrow());
            itemInvoiceRepository.save(itemInvoice);
        });
        removeCart(session);
    }

    }