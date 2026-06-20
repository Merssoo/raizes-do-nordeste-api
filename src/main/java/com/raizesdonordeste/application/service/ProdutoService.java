package com.raizesdonordeste.application.service;

import com.querydsl.core.BooleanBuilder;
import com.raizesdonordeste.api.dto.DTO.ProdutoDTO;
import com.raizesdonordeste.domain.entity.Produto;
import com.raizesdonordeste.domain.entity.QEstoque;
import com.raizesdonordeste.domain.entity.QProduto;
import com.raizesdonordeste.infrastructure.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService extends BaseService<Produto, ProdutoDTO, Long> {

    public ProdutoService(ProdutoRepository repository) {
        super(repository, Produto.class);
    }

    @Override
    public Produto toEntity(ProdutoDTO dto) {
        return new Produto(dto.getId(), dto.getNome(), dto.getDescricao(), dto.getPreco(), dto.getAtivo());
    }

    @Override
    public ProdutoDTO toDto(Produto entity) {
        return new ProdutoDTO(entity.getId(), entity.getNome(), entity.getDescricao(), entity.getPreco(), entity.getAtivo());
    }

    @Transactional
    public void inativar(Long id) {
        ProdutoDTO dto = getByIdOrError(id);
        dto.setAtivo(false);
        save(dto);
    }

    public Page<ProdutoDTO> getProdutosPorUnidade(Long idUnidade, String filter, Pageable pageable) {
        QProduto qProduto = QProduto.produto;
        QEstoque qEstoque = QEstoque.estoque;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qEstoque.produto.id.eq(qProduto.id));
        builder.and(qEstoque.unidade.id.eq(idUnidade));

        return getPaged(filter, builder, pageable);
    }
}
