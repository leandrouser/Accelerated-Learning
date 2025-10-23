package com.estudo.demo.config;

import com.estudo.demo.enums.TipoPessoa;
import com.estudo.demo.model.Pessoas;
import com.estudo.demo.repositorio.PessoaRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfig {

    private final UserDetailsService userDetailsService;

    public ApplicationConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
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