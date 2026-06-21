package com.raizesdonordeste.application.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.raizesdonordeste.api.dto.ProdutoDTO;
import com.raizesdonordeste.api.exception.BusinessException;
import com.raizesdonordeste.domain.entity.Produto;
import com.raizesdonordeste.domain.entity.QEstoque;
import com.raizesdonordeste.domain.entity.QProduto;
import com.raizesdonordeste.infrastructure.repository.ProdutoRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService extends BaseService<Produto, ProdutoDTO, Long> {

    private final JPAQueryFactory queryFactory;

    public ProdutoService(ProdutoRepository repository, EntityManager entityManager) {
        super(repository, Produto.class);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Produto toEntity(ProdutoDTO dto) {
        return new Produto(dto.getId(), dto.getNome(), dto.getDescricao(), dto.getPreco(), dto.getAtivo());
    }

    @Override
    public ProdutoDTO toDto(Produto entity) {
        return new ProdutoDTO(entity.getId(), entity.getNome(), entity.getDescricao(), entity.getPreco(), entity.getAtivo());
    }

    private void validarProdutoNome(String nome, String descricao) {
        QProduto qProduto = QProduto.produto;
        BooleanExpression query = qProduto.nome.equalsIgnoreCase(nome).and(qProduto.descricao.eq(descricao));
        if (repository.exists(query)) {
            throw new BusinessException("Já existe um produto com esse nome");
        }
    }

    @Transactional
    public void inativar(Long id) {
        Produto entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado"));
        entity.setAtivo(false);
        repository.save(entity);
    }

    public Page<ProdutoDTO> getProdutosPorUnidadeComSaldo(Long idUnidade, String filter, Pageable pageable) {
        QProduto qProduto = QProduto.produto;
        QEstoque qEstoque = QEstoque.estoque;

        JPAQuery<Produto> query = queryFactory.select(qProduto)
                .from(qProduto)
                .innerJoin(qEstoque).on(qEstoque.produto.eq(qProduto))
                .where(qEstoque.unidade.id.eq(idUnidade)
                        .and(qEstoque.quantidade.gt(0)));

        return applyFilterAndPagination(query, filter, pageable);
    }

    @Transactional
    public ProdutoDTO criar(ProdutoDTO produtoDTO) {
        this.validarProdutoNome(produtoDTO.getNome(), produtoDTO.getDescricao());
        return save(produtoDTO);
    }

    @Transactional
    public ProdutoDTO atualizar(Long id, ProdutoDTO produtoDTO) {
        this.validarProdutoNome(produtoDTO.getNome(), produtoDTO.getDescricao());
        return this.update(id, produtoDTO);
    }
}
