package com.raizesdonordeste.api.dto;

import com.raizesdonordeste.application.shared.Identificavel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO implements Identificavel<Long> {

    private Long id;
    private String nome;
}
