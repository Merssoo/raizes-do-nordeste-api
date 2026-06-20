package com.raizesdonordeste.application.service;

import com.raizesdonordeste.api.dto.EstoqueDTO;
import com.raizesdonordeste.domain.entity.Estoque;
import com.raizesdonordeste.domain.entity.Produto;
import com.raizesdonordeste.domain.entity.Unidade;
import com.raizesdonordeste.infrastructure.repository.EstoqueRepository;
import com.raizesdonordeste.infrastructure.repository.ProdutoRepository;
import com.raizesdonordeste.infrastructure.repository.UnidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstoqueService extends BaseService<Estoque, EstoqueDTO, Long> {

    private final ProdutoRepository produtoRepository;
    private final UnidadeRepository unidadeRepository;

    public EstoqueService(EstoqueRepository repository, 
                          ProdutoRepository produtoRepository, 
                          UnidadeRepository unidadeRepository) {
        super(repository, Estoque.class);
        this.produtoRepository = produtoRepository;
        this.unidadeRepository = unidadeRepository;
    }

    @Override
    public Estoque toEntity(EstoqueDTO dto) {
        Produto produto = dto.getProdutoId() != null ? produtoRepository.findById(dto.getProdutoId()).orElse(null) : null;
        Unidade unidade = dto.getUnidadeId() != null ? unidadeRepository.findById(dto.getUnidadeId()).orElse(null) : null;
        return new Estoque(dto.getId(), dto.getQuantidade(), produto, unidade);
    }

    @Override
    public EstoqueDTO toDto(Estoque entity) {
        return new EstoqueDTO(
            entity.getId(), 
            entity.getQuantidade(), 
            entity.getProduto() != null ? entity.getProduto().getId() : null, 
            entity.getUnidade() != null ? entity.getUnidade().getId() : null
        );
    }

    public List<EstoqueDTO> listarPorUnidade(Long unidadeId) {
        return ((EstoqueRepository) repository).findByUnidadeId(unidadeId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
