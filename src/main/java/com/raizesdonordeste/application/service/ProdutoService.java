package com.raizesdonordeste.application.service;

import com.raizesdonordeste.api.dto.ProdutoDTO;
import com.raizesdonordeste.domain.entity.Produto;
import com.raizesdonordeste.infrastructure.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

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
}
