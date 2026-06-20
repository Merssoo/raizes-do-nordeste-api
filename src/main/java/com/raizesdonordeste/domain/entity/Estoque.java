package com.raizesdonordeste.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estoques")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estoques_seq")
    @SequenceGenerator(name = "estoques_seq", sequenceName = "estoques_id_seq", allocationSize = 1)
    private Long id;

    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;
}
