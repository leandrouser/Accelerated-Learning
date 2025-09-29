package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.requestDTO.LoginRequestDTO;
import com.estudo.demo.DTOs.response.LoginResponseDTO;
import com.estudo.demo.model.Pessoas;
import com.estudo.demo.repositorio.PessoaRepositorio;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PessoaRepositorio pessoaRepositorio;
    private final SecretKey jwtSecret;
    private final long jwtExpirationMs = 86400000;

    public AuthService(AuthenticationManager authenticationManager, PessoaRepositorio pessoaRepositorio) {
        this.authenticationManager = authenticationManager;
        this.pessoaRepositorio = pessoaRepositorio;
        this.jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));
    }

    public String getCurrentUserCpf() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        try {
            // Autentica pelo CPF + senha
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getNome(),
                            loginRequestDTO.getSenha()
                    )
            );

            // Busca a pessoa pelo CPF
            Pessoas pessoa = pessoaRepositorio.findByNome(loginRequestDTO.getNome())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

// Passa authentication e cpf
            String token = generateToken(authentication, pessoa.getCpf());

            LoginResponseDTO response = new LoginResponseDTO();
            response.setToken(token);
            response.setTipo(pessoa.getTipo().name());
            response.setNome(pessoa.getNome());
            response.setCpf(pessoa.getCpf());

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Erro no login: " + e.getMessage());
        }
    }

    private String generateToken(Authentication authentication, String cpf) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(cpf) // CPF no subject
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(jwtSecret)
                .compact();
    }


    public String getCpfFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }


    public String getAuthoritiesFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("authorities", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
