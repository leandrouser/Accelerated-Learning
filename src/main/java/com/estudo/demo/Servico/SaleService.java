package com.estudo.demo.Servico;

import com.estudo.demo.DTOs.requestDTO.SaleItemRequestDTO;
import com.estudo.demo.DTOs.requestDTO.SaleRequestDTO;
import com.estudo.demo.DTOs.response.SaleItemResponseDTO;
import com.estudo.demo.DTOs.response.SaleResponseDTO;
import com.estudo.demo.model.*;
import com.estudo.demo.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import com.estudo.demo.enums.StatusPayment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final ProductRepository productRepository;
    private final PersonRepository personRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public SaleResponseDTO createSale(SaleRequestDTO saleRequest) {
        String principalCpf = SecurityContextHolder.getContext().getAuthentication().getName();
        People seller = personRepository.findByCpf(principalCpf)
                .orElseThrow(() -> new IllegalArgumentException("Vendedor não encontrado."));

        Customer customer = customerRepository.findById(saleRequest.customerId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

        // Criar venda
        Sales sale = new Sales();
        sale.setSeller(seller);
        sale.setCustomer(customer);
        sale.setDataSale(LocalDate.now());
        sale.setPayment(saleRequest.payment());
        sale.setDiscountTotal(saleRequest.discountTotal() != null ? saleRequest.discountTotal() : BigDecimal.ZERO);

        List<SaleItem> items = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        int totalAmount = 0;

        // Processar itens
        for (SaleItemRequestDTO itemDTO : saleRequest.itens()) {
            Product product = productRepository.findById(itemDTO.productId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + itemDTO.productId()));

            // Validar estoque se necessário
            if (product.getStock() < itemDTO.quantity()) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: " + product.getProductName());
            }

            SaleItem item = new SaleItem();
            item.setSale(sale);
            item.setProduct(product);
            item.setAmount(itemDTO.quantity());

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemDTO.quantity()));
            subtotal = subtotal.add(itemTotal);
            totalAmount += itemDTO.quantity();

            items.add(item);

            // Atualizar estoque (opcional)
            // product.setStock(product.getStock() - itemDTO.quantity());
            // productRepository.save(product);
        }

        sale.setSubTotal(subtotal);
        sale.setAmount(totalAmount);

        // Calcular total com desconto
        BigDecimal total = subtotal.subtract(sale.getDiscountTotal());
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Desconto não pode ser maior que o subtotal");
        }
        sale.setValue(total);
        sale.setItems(items);

        // Salvar venda e itens
        Sales savedSale = saleRepository.save(sale);
        saleItemRepository.saveAll(items);

        return toResponseDTO(savedSale);
    }

    public SaleResponseDTO findById(Long id) {
        Sales sale = saleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venda não encontrada: " + id));
        return toResponseDTO(sale);
    }

    public List<SaleResponseDTO> findByCustomer(Long customerId) {
        List<Sales> sales = saleRepository.findByCustomerId(customerId);
        return sales.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<SaleResponseDTO> findBySeller(Long sellerId) {
        List<Sales> sales = saleRepository.findBySellerId(sellerId);
        return sales.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private SaleResponseDTO toResponseDTO(Sales sale) {
        List<SaleItemResponseDTO> itens = sale.getItems().stream()
                .map(item -> new SaleItemResponseDTO(
                        item.getProduct().getId(),
                        item.getProduct().getProductName(),
                        item.getProduct().getPrice(),
                        item.getAmount()
                )).toList();

        return new SaleResponseDTO(
                sale.getId(),
                sale.getCustomer().getUsername(),
                sale.getAmount(),
                sale.getDataSale(),
                sale.getDiscountTotal(),
                sale.getSubTotal(),
                sale.getPayment(),
                sale.getValue(),
                itens
        );
    }

    public List<SaleItemResponseDTO> listarItensPorVenda(Long saleId) {
        List<SaleItem> items = saleItemRepository.findBySaleId(saleId);
        return items.stream()
                .map(item -> new SaleItemResponseDTO(
                        item.getProduct().getId(),
                        item.getProduct().getProductName(),
                        item.getProduct().getPrice(),
                        item.getAmount()
                )).toList();
    }
}