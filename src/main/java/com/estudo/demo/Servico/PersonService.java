package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.requestDTO.PersonRequestDTO;
import com.estudo.demo.DTOs.response.PersonResponseDTO;
import com.estudo.demo.enums.TypePerson;
import com.estudo.demo.model.People;
import com.estudo.demo.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public PersonService(PersonRepository personRepository,
                         BCryptPasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public PersonResponseDTO createPerson(PersonRequestDTO dto) {
        validarCpfDuplicado(dto.cpf(), null);
        validarSenhaParaTipo(dto.typePerson(), dto.password());

        People people = new People();
        people.setName(dto.name());
        people.setCpf(dto.cpf());
        people.setPhone(dto.phone());
        people.setType(dto.typePerson());

        if (dto.password() != null) {
            people.setPassword(passwordEncoder.encode(dto.password()));
        }

        people.setActive(dto.active() != null ? dto.active() : true);

        People salvo = personRepository.save(people);
        return toResponseDTO(salvo);
    }

    public List<PersonResponseDTO> findAll() {
        return personRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PersonResponseDTO buscarPorId(Long id) {
        People people = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));
        return toResponseDTO(people);
    }

    @Transactional
    public PersonResponseDTO atualizar(Long id, PersonRequestDTO dto) {
        People people = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        validarCpfDuplicado(dto.cpf(), id);
        validarAtualizacaoSenha(people, dto);

        people.setName(dto.name());
        people.setCpf(dto.cpf());
        people.setPhone(dto.phone());
        people.setType(dto.typePerson());
        people.setActive(dto.active() != null ? dto.active() : people.isActive());

        if (dto.password() != null && !dto.password().trim().isEmpty()) {
            people.setPassword(passwordEncoder.encode(dto.password()));
        }

        People atualizado = personRepository.save(people);
        return toResponseDTO(atualizado);
    }

    private void validarSenhaParaTipo(TypePerson tipo, String senha) {
        if ((tipo == TypePerson.USER || tipo == TypePerson.ADMINISTRATOR) &&
                (senha == null || senha.trim().isEmpty())) {
            throw new IllegalArgumentException("Senha obrigatória para USUARIO e ADMINISTRADOR");
        }
    }

    private void validarAtualizacaoSenha(People pessoaExistente, PersonRequestDTO dto) {
        // Se está mudando para um tipo que requer senha e não tem senha atual nem nova
        if ((dto.typePerson() == TypePerson.USER || dto.typePerson() == TypePerson.ADMINISTRATOR) &&
                pessoaExistente.getPassword() == null &&
                (dto.password() == null || dto.password().trim().isEmpty())) {
            throw new IllegalArgumentException("Senha obrigatória para USUARIO e ADMINISTRADOR");
        }
    }

    private void validarCpfDuplicado(String cpf, Long id) {
        if (id == null) {
            // Criando nova pessoa
            if (personRepository.findByCpf(cpf).isPresent()) {
                throw new IllegalArgumentException("CPF já cadastrado");
            }
        } else {
            // Atualizando pessoa existente
            if (personRepository.existsByCpfAndIdNot(cpf, id)) {
                throw new IllegalArgumentException("CPF já cadastrado para outra pessoa");
            }
        }
    }

    private PersonResponseDTO toResponseDTO(People people) {
        return new PersonResponseDTO(
                people.getId(),
                people.getName(),
                people.getCpf(),
                people.getPhone(),
                people.getType(),
                people.isActive()
        );
    }

}