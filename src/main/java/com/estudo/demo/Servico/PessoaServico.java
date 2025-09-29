package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.requestDTO.PessoaRequestDTO;
import com.estudo.demo.DTOs.response.PessoaResponseDTO;
import com.estudo.demo.enums.TipoPessoa;
import com.estudo.demo.model.Pessoas;
import com.estudo.demo.repositorio.PessoaRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class PessoaServico {

    private final PessoaRepositorio pessoaRepositorio;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthService authService;

    public PessoaServico(PessoaRepositorio pessoaRepositorio,
                         BCryptPasswordEncoder passwordEncoder,
                         AuthService authService) {
        this.pessoaRepositorio = pessoaRepositorio;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }


    @Transactional
    public PessoaResponseDTO criarPessoa(PessoaRequestDTO dto){

        if (!pessoaRepositorio.existsById(dto.getId())) {
            throw new IllegalArgumentException("Apenas administradores podem criar outros administradores");
        }

        validarCpfDuplicado(dto.getCpf(), null);

        Pessoas pessoas = new Pessoas();
        pessoas.setNome(dto.getNome());
        pessoas.setEndereco(dto.getEndereco());
        pessoas.setCpf(dto.getCpf());
        pessoas.setTelefone(dto.getTelefone());
        pessoas.setTipo(dto.getTipo());

        if ((dto.getTipo() == TipoPessoa.USUARIO || dto.getTipo() == TipoPessoa.ADMINISTRADOR)) {
            if (dto.getSenha() == null) {
                throw new IllegalArgumentException("Senha obrigatória para USUARIO e ADMINISTRADOR");
            }
            pessoas.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        pessoas.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);

        Pessoas salvo = pessoaRepositorio.save(pessoas);
        return toResponseDTO(salvo);
    }

    public List<PessoaResponseDTO> findAll(){
        return pessoaRepositorio.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PessoaResponseDTO buscarPorId(Long id){
        Pessoas pessoas = pessoaRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrado"));
        return toResponseDTO(pessoas);
    }

    @Transactional
    public PessoaResponseDTO atualizar(Long id, PessoaRequestDTO dto){
        Pessoas pessoas = pessoaRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrado"));

        validarCpfDuplicado(dto.getCpf(), id);

        pessoas.setNome(dto.getNome());
        pessoas.setEndereco(dto.getEndereco());
        pessoas.setCpf(dto.getCpf());
        pessoas.setTelefone(dto.getTelefone());
        pessoas.setTipo(dto.getTipo());
        pessoas.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : pessoas.isAtivo());

        if ((dto.getTipo() == TipoPessoa.USUARIO || dto.getTipo() == TipoPessoa.ADMINISTRADOR) && dto.getSenha() != null) {
            pessoas.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        Pessoas atualizado = pessoaRepositorio.save(pessoas);
        return toResponseDTO(atualizado);

    }

    private void validarCpfDuplicado(String cpf, Long id){
        pessoaRepositorio.findByCpf(cpf).ifPresent(c -> {
            if (id == null || !c.getId().equals(id)){
                throw new IllegalArgumentException("CPF já cadastrado para outro cliente");
            }
        });

    }

    private PessoaResponseDTO toResponseDTO(Pessoas pessoas){
        PessoaResponseDTO dto = new PessoaResponseDTO();
        dto.setNome(pessoas.getNome());
        dto.setEndereco(pessoas.getEndereco());
        dto.setCpf(pessoas.getCpf());
        dto.setTelefone(pessoas.getTelefone());
        dto.setTipo(pessoas.getTipo());
        dto.setAtivo(pessoas.isAtivo());
        return dto;
    }
}
