package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.ClienteDTO;
import com.estudo.demo.DTOs.ItensVendasDTO;
import com.estudo.demo.DTOs.VendaRequestDTO;
import com.estudo.demo.DTOs.VendaResponseDTO;
import com.estudo.demo.model.ItensVendas;
import com.estudo.demo.model.Pessoas;
import com.estudo.demo.model.Produtos;
import com.estudo.demo.model.Vendas;
import com.estudo.demo.repositorio.PessoaRepositorio;
import com.estudo.demo.repositorio.ProdutoRepositorio;
import com.estudo.demo.repositorio.VendaRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class VendaServico {

    private final VendaRepositorio vendaRepositorio;
    private final ProdutoRepositorio produtoRepositorio;
    private final PessoaRepositorio pessoaRepositorio;

    public VendaServico(VendaRepositorio vendaRepositorio,
                        ProdutoRepositorio produtoRepositorio,
                        PessoaRepositorio pessoaRepositorio) {
        this.vendaRepositorio = vendaRepositorio;
        this.produtoRepositorio = produtoRepositorio;
        this.pessoaRepositorio = pessoaRepositorio;
    }

    @Transactional
    public VendaResponseDTO criarVenda(VendaRequestDTO dto) {
        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new IllegalArgumentException("A venda deve conter pelo menos um item");
        }

        Pessoas cliente = pessoaRepositorio.findById(dto.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        Vendas venda = new Vendas();
        venda.setCliente(cliente);
        venda.setDataVenda(LocalDate.now());
        venda.setPagamento("PENDENTE");
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

            ItensVendas item = new ItensVendas();
            item.setVenda(venda);
            item.setProduto(produto);
            item.setQuantidade(itemDto.getQuantidade());
            item.setSubTotal(itemDto.getValor());
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

        if ("PAGO".equalsIgnoreCase(venda.getPagamento())) {
            throw new IllegalArgumentException("Venda já está finalizada");
        }

        venda.setPagamento("PAGO");
        Vendas atualizado = vendaRepositorio.save(venda);

        return mapToDTO(atualizado);
    }

    public List<VendaResponseDTO> listarVendas() {
        return vendaRepositorio.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public VendaResponseDTO buscarPorId(Long id) {
        Vendas venda = vendaRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada"));

        return mapToDTO(venda);
    }

    private VendaResponseDTO mapToDTO(Vendas venda) {
        ClienteDTO clienteDTO = new ClienteDTO(
                venda.getCliente().getId(),
                venda.getCliente().getNome(),
                venda.getCliente().getCpf(),
                venda.getCliente().getTelefone(),
                venda.getCliente().getEndereco()
        );

        List<ItensVendasDTO> itensResponse = venda.getItens().stream()
                .map(i -> new ItensVendasDTO(
                        i.getProduto().getId(),
                        i.getProduto().getNome(),
                        i.getProduto().getCodigoBarras(),
                        i.getProduto().getCategorias().getNome(),
                        i.getQuantidade(),
                        i.getSubTotal(),
                        i.getDesconto(),
                        i.getTotal()
                )).toList();

        return new VendaResponseDTO(
                venda.getId(),
                clienteDTO,
                venda.getQuantidade(),
                venda.getDataVenda(),
                venda.getDescontoTotal(),
                venda.getValor(),
                venda.getPagamento(),
                itensResponse
        );
    }

}
