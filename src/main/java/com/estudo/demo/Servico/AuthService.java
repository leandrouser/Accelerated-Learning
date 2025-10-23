package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.requestDTO.LoginRequestDTO;
import com.estudo.demo.DTOs.response.LoginResponseDTO;
import com.estudo.demo.model.Pessoas;
import com.estudo.demo.repositorio.PessoaRepositorio;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PessoaRepositorio pessoaRepositorio;
    private final long jwtExpirationMs = 86400000;

    @Value("${DLARA_JWT_SECRET}")
    private String jwtSecretString;

    private SecretKey jwtSecret;

    public AuthService(AuthenticationManager authenticationManager, PessoaRepositorio pessoaRepositorio) {
        this.authenticationManager = authenticationManager;
        this.pessoaRepositorio = pessoaRepositorio;
    }

    @PostConstruct
    public void init() {
        System.out.println("--- INICIALIZANDO CHAVE SECRETA JWT ---");
        if (jwtSecretString == null || jwtSecretString.isBlank()) {
            System.err.println("!!! ERRO CRÍTICO: Variável de ambiente 'DLARA_JWT_SECRET' não encontrada ou está vazia. !!!");
            System.err.println("!!! A autenticação JWT NÃO irá funcionar. !!!");
            // Lançar uma exceção aqui é uma boa prática para impedir que a aplicação
            // inicie em um estado inseguro.
            throw new IllegalStateException("A variável de ambiente DLARA_JWT_SECRET deve ser configurada.");
        } else {
            System.out.println("Variável de ambiente 'DLARA_JWT_SECRET' lida com sucesso.");
            this.jwtSecret = Keys.hmacShaKeyFor(jwtSecretString.getBytes(StandardCharsets.UTF_8));
        }
        System.out.println("------------------------------------");
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
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getCpf(),
                            loginRequestDTO.getSenha()
                    )
            );

            Pessoas pessoa = pessoaRepositorio.findByCpf(loginRequestDTO.getCpf())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            String token = generateToken(authentication, pessoa.getCpf());

            // Crie o objeto 'response' UMA SÓ VEZ ✅
            LoginResponseDTO response = new LoginResponseDTO();

            // Agora, adicione todas as informações a esse mesmo objeto
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
