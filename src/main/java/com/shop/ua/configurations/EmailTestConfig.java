package com.shop.ua.configurations;

import com.shop.ua.component.RepositoryManager;
import com.shop.ua.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class EmailTestConfig extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private RepositoryManager repositoryManager;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public EmailTestConfig() {
        super(new AntPathRequestMatcher("/login", "POST"));
        setAuthenticationManager(authentication -> {
            String email = (String) authentication.getPrincipal();
            UserDetails userDetails = repositoryManager.getUserService().loadUserByUsername(email);
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        });
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // логіка отримання даних для аутентифікації
        String email = request.getParameter("username");
        String password = request.getParameter("password");

        // Створення Authentication для передачі AuthenticationManager
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);

        // виклик AuthenticationManager
        return getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);

        if (authResult.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authResult.getPrincipal();
            String email = userDetails.getUsername();
            User user = repositoryManager.getUserService().findByEmail(email);

            if (user != null && !user.isEmailVerified()) {
                redirectStrategy.sendRedirect(request, response, "/verify-email");
            } else {
                redirectStrategy.sendRedirect(request, response, "/");
            }
        }


    }

}

