package com.estudo.demo.Servico;

import com.estudo.demo.enums.TipoPessoa;
import com.estudo.demo.model.Pessoas;
import com.estudo.demo.repositorio.PessoaRepositorio;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService { // Implementar a interface

    private final PessoaRepositorio pessoaRepositorio;

    public UserDetailsServiceImpl(PessoaRepositorio pessoaRepositorio) {
        this.pessoaRepositorio = pessoaRepositorio;
    }

    @Override
    public UserDetails loadUserByUsername(String nome) throws UsernameNotFoundException {
        Pessoas pessoa = pessoaRepositorio.findByNome(nome)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + nome));

        // Verificar se é cliente (não pode fazer login)
        if (pessoa.getTipo() == TipoPessoa.CLIENTE) {
            throw new UsernameNotFoundException("Clientes não podem fazer login");
        }

        // Verificar se usuário está ativo
        if (!pessoa.isAtivo()) {
            throw new UsernameNotFoundException("Usuário inativo");
        }

        // Verificar se tem senha (usuários e administradores devem ter senha)
        if (pessoa.getSenha() == null || pessoa.getSenha().isEmpty()) {
            throw new UsernameNotFoundException("Usuário não possui senha configurada");
        }

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + pessoa.getTipo().name())
        );

        // Usar o User do Spring Security (org.springframework.security.core.userdetails.User)
        return new User(
                pessoa.getNome(),
                pessoa.getSenha(),
                authorities
        );
    }
}