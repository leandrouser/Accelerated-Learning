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

    public PessoaServico(PessoaRepositorio pessoaRepositorio,
                         BCryptPasswordEncoder passwordEncoder) {
        this.pessoaRepositorio = pessoaRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public PessoaResponseDTO criarPessoa(PessoaRequestDTO dto) {
        validarCpfDuplicado(dto.getCpf(), null);
        validarSenhaParaTipo(dto.getTipo(), dto.getSenha());

        Pessoas pessoas = new Pessoas();
        pessoas.setNome(dto.getNome());
        pessoas.setEndereco(dto.getEndereco());
        pessoas.setCpf(dto.getCpf());
        pessoas.setTelefone(dto.getTelefone());
        pessoas.setTipo(dto.getTipo());

        if (dto.getSenha() != null) {
            pessoas.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        pessoas.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);

        Pessoas salvo = pessoaRepositorio.save(pessoas);
        return toResponseDTO(salvo);
    }

    public List<PessoaResponseDTO> findAll() {
        return pessoaRepositorio.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PessoaResponseDTO buscarPorId(Long id) {
        Pessoas pessoas = pessoaRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));
        return toResponseDTO(pessoas);
    }

    @Transactional
    public PessoaResponseDTO atualizar(Long id, PessoaRequestDTO dto) {
        Pessoas pessoas = pessoaRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        validarCpfDuplicado(dto.getCpf(), id);
        validarAtualizacaoSenha(pessoas, dto);

        pessoas.setNome(dto.getNome());
        pessoas.setEndereco(dto.getEndereco());
        pessoas.setCpf(dto.getCpf());
        pessoas.setTelefone(dto.getTelefone());
        pessoas.setTipo(dto.getTipo());
        pessoas.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : pessoas.isAtivo());

        if (dto.getSenha() != null && !dto.getSenha().trim().isEmpty()) {
            pessoas.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        Pessoas atualizado = pessoaRepositorio.save(pessoas);
        return toResponseDTO(atualizado);
    }

    private void validarSenhaParaTipo(TipoPessoa tipo, String senha) {
        if ((tipo == TipoPessoa.USUARIO || tipo == TipoPessoa.ADMINISTRADOR) &&
                (senha == null || senha.trim().isEmpty())) {
            throw new IllegalArgumentException("Senha obrigatória para USUARIO e ADMINISTRADOR");
        }
    }

    private void validarAtualizacaoSenha(Pessoas pessoaExistente, PessoaRequestDTO dto) {
        // Se está mudando para um tipo que requer senha e não tem senha atual nem nova
        if ((dto.getTipo() == TipoPessoa.USUARIO || dto.getTipo() == TipoPessoa.ADMINISTRADOR) &&
                pessoaExistente.getSenha() == null &&
                (dto.getSenha() == null || dto.getSenha().trim().isEmpty())) {
            throw new IllegalArgumentException("Senha obrigatória para USUARIO e ADMINISTRADOR");
        }
    }

    private void validarCpfDuplicado(String cpf, Long id) {
        if (id == null) {
            // Criando nova pessoa
            if (pessoaRepositorio.findByCpf(cpf).isPresent()) {
                throw new IllegalArgumentException("CPF já cadastrado");
            }
        } else {
            // Atualizando pessoa existente
            if (pessoaRepositorio.existsByCpfAndIdNot(cpf, id)) {
                throw new IllegalArgumentException("CPF já cadastrado para outra pessoa");
            }
        }
    }

    private PessoaResponseDTO toResponseDTO(Pessoas pessoas) {
        return new PessoaResponseDTO(
                pessoas.getId(),
                pessoas.getNome(),
                pessoas.getCpf(),
                pessoas.getTelefone(),
                pessoas.getEndereco(),
                pessoas.getTipo(),
                pessoas.isAtivo()
        );
    }

}