//package Nhom_06.HuaKieuLam;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Collection;
//
//@Component
//public class CustomSavedRequestAwareAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
//        Collection authorities = authentication.getAuthorities();
//
//        if (authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
//            getRedirectStrategy().sendRedirect(request, response, "/admin");
//        } else {
//            getRedirectStrategy().sendRedirect(request, response, "/");
//        }
//    }
//}
