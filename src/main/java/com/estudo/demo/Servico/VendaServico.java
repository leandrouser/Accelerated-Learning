package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.response.ClienteResponseDTO;
import com.estudo.demo.DTOs.ItensVendasDTO;
import com.estudo.demo.DTOs.requestDTO.VendaRequestDTO;
import com.estudo.demo.DTOs.response.VendaResponseDTO;
import com.estudo.demo.model.ItensVendas;
import com.estudo.demo.model.Pessoas;
import com.estudo.demo.model.Produtos;
import com.estudo.demo.model.Vendas;
import com.estudo.demo.repositorio.PessoaRepositorio;
import com.estudo.demo.repositorio.ProdutoRepositorio;
import com.estudo.demo.repositorio.VendaRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.estudo.demo.model.Cliente;
import com.estudo.demo.repositorio.ClienteRepositorio;
import org.springframework.security.core.context.SecurityContextHolder;
import com.estudo.demo.enums.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class VendaServico {

    private final VendaRepositorio vendaRepositorio;
    private final ProdutoRepositorio produtoRepositorio;
    private final PessoaRepositorio pessoaRepositorio;
    private final ClienteRepositorio clienteRepositorio;

    public VendaServico(VendaRepositorio vendaRepositorio,
                        ProdutoRepositorio produtoRepositorio,
                        PessoaRepositorio pessoaRepositorio,
                        ClienteRepositorio clienteRepositorio) {
        this.vendaRepositorio = vendaRepositorio;
        this.produtoRepositorio = produtoRepositorio;
        this.pessoaRepositorio = pessoaRepositorio;
        this.clienteRepositorio = clienteRepositorio;
    }

    @Transactional
    public VendaResponseDTO criarVenda(VendaRequestDTO dto) {
        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new IllegalArgumentException("A venda deve conter pelo menos um item");
        }

        String principalName = SecurityContextHolder.getContext().getAuthentication().getName();
        Pessoas usuario = pessoaRepositorio.findByCpf(principalName) // Use findByCpf
                .orElseThrow(() -> new IllegalArgumentException("Vendedor (usuário) não encontrado"));

        // B. Buscar o CLIENTE (vindo do DTO)
        Cliente cliente = clienteRepositorio.findById(dto.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        Vendas venda = new Vendas();
        venda.setVendedor(usuario);
        venda.setCliente(cliente);
        venda.setDataVenda(LocalDate.now());
        venda.setPagamento(StatusPagamento.PENDENTE);
        venda.setDescontoTotal(dto.getDescontoTotal() != null ? dto.getDescontoTotal() : BigDecimal.ZERO);

        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal totalVenda = BigDecimal.ZERO;
        int quantidadeTotal = 0;

        for (ItensVendasDTO itemDto : dto.getItens()) {
            if (itemDto.getQuantidade() <= 0 || itemDto.getValor().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Quantidade e valor devem ser positivos");
            }

            Produtos produto = produtoRepositorio.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
            if (produto.getEstoque() < itemDto.getQuantidade()) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            int novoEstoque = produto.getEstoque() - itemDto.getQuantidade();
            produto.setEstoque(novoEstoque);
            produtoRepositorio.save(produto);

            ItensVendas item = new ItensVendas();
            item.setVenda(venda);
            item.setProduto(produto);
            item.setQuantidade(itemDto.getQuantidade());
            item.setSubTotal(produto.getPreco());
            item.setDesconto(itemDto.getDesconto() != null ? itemDto.getDesconto() : BigDecimal.ZERO);
            item.setTotal(item.getSubTotal()
                    .multiply(BigDecimal.valueOf(item.getQuantidade()))
                    .subtract(item.getDesconto()));

            subTotal = subTotal.add(item.getSubTotal().multiply(BigDecimal.valueOf(item.getQuantidade())));
            totalVenda = totalVenda.add(item.getTotal());
            quantidadeTotal += item.getQuantidade();

            venda.getItens().add(item);
        }

        venda.setSubTotal(subTotal);
        venda.setValor(totalVenda.subtract(venda.getDescontoTotal()));
        venda.setQuantidade(quantidadeTotal);

        Vendas salvo = vendaRepositorio.save(venda);

        return mapToDTO(salvo);
    }

    @Transactional
    public VendaResponseDTO finalizarVenda(Long id) {
        Vendas venda = vendaRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada"));

        if (venda.getPagamento() == StatusPagamento.PAGO) {
            throw new IllegalArgumentException("Venda já está finalizada");
        }

        venda.setPagamento(StatusPagamento.PAGO);
        Vendas atualizado = vendaRepositorio.save(venda);

        return mapToDTO(atualizado);
    }

    public List<VendaResponseDTO> listarVendas() {
        return vendaRepositorio.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public VendaResponseDTO buscarPorId(Long vendaId) {
        return vendaRepositorio.findById(vendaId)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Venda não encontrada"));
    }


    public List<VendaResponseDTO> buscarVendasPorCliente(Long clienteId) {
        if (!clienteRepositorio.existsById(clienteId)) {
            throw new IllegalArgumentException("Cliente não encontrado com ID: " + clienteId);
        }

        return vendaRepositorio.findByClienteId(clienteId).stream()
                .map(this::mapToDTO)
                .toList();
    }

    private VendaResponseDTO mapToDTO(Vendas venda) {

        // Mapear a entidade Cliente para ClienteResponseDTO
        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO(
                venda.getCliente().getId(),
                venda.getCliente().getUsername(),
                venda.getCliente().getCpf(),
                venda.getCliente().getTelefone(),
                venda.getCliente().getEndereco(),
                venda.getCliente().isAtivo()
        );

        List<ItensVendasDTO> itensResponse = venda.getItens().stream()
                .map(i -> {
                    String nomeCategoria = i.getProduto().getCategoria() != null ?
                            i.getProduto().getCategoria().getNome() : null;

                    return new ItensVendasDTO(
                            i.getProduto().getId(),
                            i.getProduto().getNome(),
                            i.getProduto().getCodigoBarras(),
                            nomeCategoria,
                            i.getQuantidade(),
                            i.getSubTotal(),
                            i.getDesconto(),
                            i.getTotal()
                    );
                }).toList();

        return new VendaResponseDTO(
                venda.getId(),
                clienteResponseDTO,
                venda.getQuantidade(),
                venda.getDataVenda(),
                venda.getDescontoTotal(),
                venda.getSubTotal(),
                venda.getPagamento().name(),
                venda.getValor(),
                itensResponse
        );
    }
}