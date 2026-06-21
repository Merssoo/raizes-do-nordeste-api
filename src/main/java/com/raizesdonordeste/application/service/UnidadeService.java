package com.raizesdonordeste.application.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.raizesdonordeste.api.dto.UnidadeDTO;
import com.raizesdonordeste.api.exception.BusinessException;
import com.raizesdonordeste.domain.entity.QProduto;
import com.raizesdonordeste.domain.entity.QUnidade;
import com.raizesdonordeste.domain.entity.Unidade;
import com.raizesdonordeste.domain.enums.EstadoEnum;
import com.raizesdonordeste.infrastructure.repository.UnidadeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UnidadeService extends BaseService<Unidade, UnidadeDTO, Long> {

    public UnidadeService(UnidadeRepository repository) {
        super(repository, Unidade.class);
    }

    @Override
    public Unidade toEntity(UnidadeDTO dto) {
        return new Unidade(dto.getId(), dto.getNome(), dto.getCidade(), dto.getEstado(), dto.getAtivo());
    }

    @Override
    public UnidadeDTO toDto(Unidade entity) {
        return new UnidadeDTO(entity.getId(), entity.getNome(), entity.getCidade(), entity.getEstado(), entity.getAtivo());
    }

    private void validarUnidadeNome(String nome, EstadoEnum estado, String cidade, Long id) {
        QUnidade qUnidade = QUnidade.unidade;
        BooleanExpression query = qUnidade.nome.equalsIgnoreCase(nome).and(qUnidade.cidade.eq(cidade)).and(qUnidade.estado.eq(estado));
        query = Objects.nonNull(id) ? query.and(qUnidade.id.ne(id)) : query;
        if (repository.exists(query)) {
            throw new BusinessException("Já existe uma unidade com esse nome");
        }
    }

    @Transactional
    public UnidadeDTO criar(UnidadeDTO dto) {
        this.validarUnidadeNome(dto.getNome(), dto.getEstado(), dto.getCidade(), null);
        return this.save(dto);
    }

    @Transactional
    public UnidadeDTO atualizar(Long id, UnidadeDTO dto) {
        this.validarUnidadeNome(dto.getNome(), dto.getEstado(), dto.getCidade(), id);
        return this.update(id, dto);
    }
}
