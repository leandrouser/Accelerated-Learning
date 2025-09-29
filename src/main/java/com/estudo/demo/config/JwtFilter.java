package com.estudo.demo.config;

import com.estudo.demo.Servico.AuthService;
import com.estudo.demo.model.Pessoas;
import com.estudo.demo.repositorio.PessoaRepositorio;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final PessoaRepositorio pessoaRepositorio;

    public JwtFilter(@Lazy AuthService authService, PessoaRepositorio pessoaRepositorio) {
        this.authService = authService;
        this.pessoaRepositorio = pessoaRepositorio;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            System.out.println("Token recebido: " + token);

            if (authService.validateToken(token)) {
                String cpf = authService.getCpfFromToken(token); // <- CPF extraído do token
                System.out.println("CPF extraído do token: " + cpf);

                Optional<Pessoas> pessoaOpt = pessoaRepositorio.findByCpf(cpf);
                if (pessoaOpt.isPresent()) {
                    Pessoas pessoa = pessoaOpt.get();
                    System.out.println("Pessoa encontrada no banco: " + pessoa.getNome());

                    List<SimpleGrantedAuthority> authorities = List.of(
                            new SimpleGrantedAuthority("ROLE_" + pessoa.getTipo().name())
                    );
                    System.out.println("Authorities atribuídas: " + authorities);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(cpf, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    System.out.println("Pessoa não encontrada para o CPF: " + cpf);
                }
            } else {
                System.out.println("Token inválido");
            }
        } else {
            System.out.println("Header Authorization não enviado ou inválido");
        }

        chain.doFilter(request, response);
    }
}
