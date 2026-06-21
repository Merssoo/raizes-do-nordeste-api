package com.raizesdonordeste.application.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.raizesdonordeste.api.dto.EstoqueDTO;
import com.raizesdonordeste.api.exception.BusinessException;
import com.raizesdonordeste.domain.entity.Estoque;
import com.raizesdonordeste.domain.entity.Produto;
import com.raizesdonordeste.domain.entity.QEstoque;
import com.raizesdonordeste.domain.entity.Unidade;
import com.raizesdonordeste.infrastructure.repository.EstoqueRepository;
import com.raizesdonordeste.infrastructure.repository.ProdutoRepository;
import com.raizesdonordeste.infrastructure.repository.UnidadeRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
public class EstoqueService extends BaseService<Estoque, EstoqueDTO, Long> {

    private final ProdutoRepository produtoRepository;
    private final UnidadeRepository unidadeRepository;
    private final JPAQueryFactory queryFactory;

    public EstoqueService(EstoqueRepository repository, 
                          ProdutoRepository produtoRepository, 
                          UnidadeRepository unidadeRepository,
                          EntityManager entityManager) {
        super(repository, Estoque.class);
        this.produtoRepository = produtoRepository;
        this.unidadeRepository = unidadeRepository;
        this.queryFactory = new JPAQueryFactory(entityManager);
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

    private void validarEstoqueProdutoUnidade(Long idProduto, Long idUnidade, Long id) {
        QEstoque qEstoque = QEstoque.estoque;
        BooleanExpression query = qEstoque.produto.id.eq(idProduto).and(qEstoque.unidade.id.eq(idUnidade));
        query = Objects.nonNull(id) ? query.and(qEstoque.id.ne(id)) : query;
        if (this.exist(query)) {
            throw new BusinessException("Já existe um estoque para o produto e unidade informados.");
        }
    }

    @Transactional
    public Page<EstoqueDTO> listarPorUnidade(Long unidadeId, Pageable pageable) {
        QEstoque qEstoque = QEstoque.estoque;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qEstoque.unidade.id.eq(unidadeId));

        return this.getPaged(builder, pageable);
    }

    @Transactional
    public EstoqueDTO criar(EstoqueDTO estoqueDTO) {
        this.validarEstoqueProdutoUnidade(estoqueDTO.getProdutoId(), estoqueDTO.getUnidadeId(), null);
        return this.save(estoqueDTO);
    }

    @Transactional
    public EstoqueDTO atualizar(Long id, EstoqueDTO estoqueDTO) {
        this.validarEstoqueProdutoUnidade(estoqueDTO.getProdutoId(), estoqueDTO.getUnidadeId(), id);
        return this.update(id, estoqueDTO);
    }
}
