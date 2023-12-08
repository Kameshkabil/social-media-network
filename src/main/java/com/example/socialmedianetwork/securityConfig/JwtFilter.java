package com.example.socialmedianetwork.securityConfig;

import com.example.socialmedianetwork.entity.User;
import com.example.socialmedianetwork.repo.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;


@Component
public class JwtFilter  extends OncePerRequestFilter {

    @Autowired
    UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String token;
        String email;
        Long userId;
        if(authorization!=null && authorization.startsWith("Bearer ")){
            token = authorization.substring(7);
            email =getEmailIdFromJwt(token);
            User user =userRepository.findByEmail(email).get();


                userId = user.getId();
                request.setAttribute("userID",userId);


            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    user.getEmail(),null, Collections.emptyList()
            );

            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            System.out.println(token);
        }
        filterChain.doFilter(request , response);
    }



    public String getEmailIdFromJwt(String token){
        Claims claims =Jwts.parser().setSigningKey("kamesh-secret-key").parseClaimsJws(token).getBody();
        return (String)claims.get("email");
    }
}
