package com.raizesdonordeste.application.service;

import com.raizesdonordeste.domain.entity.Unidade;
import com.raizesdonordeste.infrastructure.repository.UnidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnidadeService {

    private final UnidadeRepository repository;

    @Transactional
    public Unidade save(Unidade unidade) {
        return repository.save(unidade);
    }

    @Transactional(readOnly = true)
    public List<Unidade> getAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Unidade> getById(Long id) {
        return repository.findById(id);
    }
}
