package com.raizesdonordeste.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "unidades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unidades_seq")
    @SequenceGenerator(name = "unidades_seq", sequenceName = "unidades_id_seq", allocationSize = 1)
    private Long id;

    private String nome;

    private String cidade;

    private String estado;

    private Boolean ativo;
}
