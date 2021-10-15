package com.example.bankingapp.filter;

import com.example.bankingapp.dto.UserCredentialsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUsernameAndPasswordFilter extends UsernamePasswordAuthenticationFilter {

    //What does this do
    //use to verify if credentials correct?
    private final AuthenticationManager authenticationManager;

    private final ObjectMapper objectMapper;

    public JwtUsernameAndPasswordFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {


//        if (request.getServletPath().equals("/register")){
//            return new AnonymousAuthenticationToken("anon","anon", List.of(new SimpleGrantedAuthority("ROLE_ANON")));
//        }

        try {
            UserCredentialsDTO userCredentialsDTO = objectMapper.readValue(request.getInputStream(), UserCredentialsDTO.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userCredentialsDTO.getUsername(),
                    userCredentialsDTO.getPassword()
            );
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {


//        if (request.getServletPath().equals("/register")) {
//            chain.doFilter(request, response);
//        }

        List<String> collect = authResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", collect)
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(1)))
                .signWith(SignatureAlgorithm.HS512, "secret".getBytes(StandardCharsets.UTF_8))
                .compact();

        //check
        response.getWriter().write(token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("unsuccessful");
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
