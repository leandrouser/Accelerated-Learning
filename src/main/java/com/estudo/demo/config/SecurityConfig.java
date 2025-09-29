package com.estudo.demo.config;

import com.estudo.demo.enums.TipoPessoa;
import com.estudo.demo.model.Pessoas;
import com.estudo.demo.repositorio.PessoaRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtFilter jwtFilter, UserDetailsService userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login**").permitAll()
                        .requestMatchers("/api/produtos/**").hasAnyRole("USUARIO", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/pessoas").hasAnyRole("USUARIO", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/pessoas/**").hasAnyRole("USUARIO", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/pessoas/**").hasRole("ADMINISTRADOR")
                        // --- Regra Final ---
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public CommandLineRunner initAdmin(PessoaRepositorio pessoaRepositorio, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            if (pessoaRepositorio.findByCpf("08277765436").isEmpty()) {
                Pessoas admin = new Pessoas();
                admin.setNome("admin");
                admin.setCpf("08277765436");
                admin.setTelefone("81999549958");
                admin.setEndereco("Administração");
                admin.setTipo(TipoPessoa.ADMINISTRADOR);
                admin.setAtivo(true);
                admin.setSenha(passwordEncoder.encode("Leo352019"));
                pessoaRepositorio.save(admin);
                System.out.println("Usuário administrador criado automaticamente: admin / Leo352019 / CPF: 08277765436");
            }
        };
    }

}

