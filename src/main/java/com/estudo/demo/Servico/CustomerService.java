package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.requestDTO.CustomerRequestDTO;
import com.estudo.demo.DTOs.response.CustomerResponseDTO;
import com.estudo.demo.model.Customer;
import com.estudo.demo.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerResponseDTO criarCliente(CustomerRequestDTO dto) {
        validarDadosUnicos(dto, null);

        Customer customer = new Customer();
        customer.setUsername(dto.username());
        customer.setCpf(dto.cpf());
        customer.setPhone(dto.phone());
        customer.setActive(true);

        Customer salvo = customerRepository.save(customer);
        return toResponseDTO(salvo);
    }

    public List<CustomerResponseDTO> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CustomerResponseDTO buscarPorId(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        return toResponseDTO(customer);
    }

    @Transactional
    public CustomerResponseDTO atualizar(Long id, CustomerRequestDTO dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        validarDadosUnicos(dto, id);

        customer.setUsername(dto.username());
        customer.setCpf(dto.cpf());
        customer.setPhone(dto.phone());

        Customer updated = customerRepository.save(customer);
        return toResponseDTO(updated);
    }

    @Transactional
    public void desativarCliente(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        customer.setActive(false);
        customerRepository.save(customer);
    }

    private void validarDadosUnicos(CustomerRequestDTO dto, Long id) {
        // Validar CPF único
        if (id == null) {
            if (customerRepository.findByCpf(dto.cpf()).isPresent()) {
                throw new IllegalArgumentException("CPF já cadastrado");
            }
            if (customerRepository.findByUsername(dto.username()).isPresent()) {
                throw new IllegalArgumentException("Username já cadastrado");
            }
        } else {
            if (customerRepository.existsByCpfAndIdNot(dto.cpf(), id)) {
                throw new IllegalArgumentException("CPF já cadastrado para outro cliente");
            }
            if (customerRepository.existsByUsernameAndIdNot(dto.username(), id)) {
                throw new IllegalArgumentException("Username já cadastrado para outro cliente");
            }
        }
    }

    @Transactional
    public List<CustomerResponseDTO> buscarPorTermo(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return findAll();
        }

        List<Customer> customers = customerRepository.searchCustomer(termo.trim());
        return customers.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    private CustomerResponseDTO toResponseDTO(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getUsername(),
                customer.getCpf(),
                customer.getPhone(),
                customer.isActive()
        );
    }
}