package com.raizesdonordeste.application.service;

import com.raizesdonordeste.api.dto.DTO.UnidadeDTO;
import com.raizesdonordeste.domain.entity.Unidade;
import com.raizesdonordeste.infrastructure.repository.UnidadeRepository;
import org.springframework.stereotype.Service;

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
}
