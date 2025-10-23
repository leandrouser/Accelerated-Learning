package com.estudo.demo.Servico;

import com.estudo.demo.model.Pessoas;
import com.estudo.demo.repositorio.PessoaRepositorio;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PessoaRepositorio pessoaRepositorio;

    /**
     * O Spring Security chama este método.
     * O parâmetro 'username' aqui será o CPF que o usuário digitou na tela de login.
     */
    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {

        // 1. Buscamos a pessoa pelo CPF (que é o 'username' do login)
        Pessoas pessoa = pessoaRepositorio.findByCpf(cpf)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com este CPF: " + cpf));

        // 2. Criamos o UserDetails do Spring com os dados da NOSSA entidade
        return new User(
                pessoa.getCpf(),     // O "username" para o Spring (deve ser único)
                pessoa.getSenha(),   // O campo que guarda a senha (ex: "senha")
                Collections.singletonList(new SimpleGrantedAuthority(pessoa.getTipo().getRole()))
        );
    }
}