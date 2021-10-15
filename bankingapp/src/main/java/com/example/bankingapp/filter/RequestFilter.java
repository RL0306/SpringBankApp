package com.example.bankingapp.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/login")){
            //continues the filter chain-
             filterChain.doFilter(request, response);
             return;
        }

        System.out.println(request.getServletPath());

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authorizationHeader != null){

            //unsigns the token in the header, claims is key value pairs representing the data
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey("secret".getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(authorizationHeader);

//            System.out.println(claimsJws.getBody().get("authorities"));
            ArrayList<GrantedAuthority> authorityArrayList = new ArrayList<>();
            List<String> claims = (ArrayList<String>) claimsJws.getBody().get("authorities");

            authorityArrayList.add(new SimpleGrantedAuthority(claims.get(0)));


            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(claimsJws.getBody().getSubject(),null, authorityArrayList);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request,response);
        } else{
            filterChain.doFilter(request, response);
        }

    }
}
