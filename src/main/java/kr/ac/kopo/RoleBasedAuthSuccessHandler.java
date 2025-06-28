package kr.ac.kopo;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RoleBasedAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication auth) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
        } else {
            response.sendRedirect(request.getContextPath() + "/rooms");
        }
    }
}
