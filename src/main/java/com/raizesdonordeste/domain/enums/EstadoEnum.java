package com.raizesdonordeste.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

public enum EstadoEnum {
    AC("AC", "Acre"), AL("AL", "Alagoas"), AP("AP", "Amapá"), AM("AM", "Amazonas"),
    BA("BA", "Bahia"), CE("CE", "Ceará"), DF("DF", "Distrito Federal"), ES("ES", "Espírito Santo"),
    GO("GO", "Goiás"), MA("MA", "Maranhão"), MT("MT", "Mato Grosso"), MS("MS", "Mato Grosso do Sul"),
    MG("MG", "Minas Gerais"), PA("PA", "Pará"), PB("PB", "Paraíba"), PR("PR", "Paraná"),
    PE("PE", "Pernambuco"), PI("PI", "Piauí"), RJ("RJ", "Rio de Janeiro"), RN("RN", "Rio Grande do Norte"),
    RS("RS", "Rio Grande do Sul"), RO("RO", "Rondônia"), RR("RR", "Roraima"), SC("SC", "Santa Catarina"),
    SP("SP", "São Paulo"), SE("SE", "Sergipe"), TO("TO", "Tocantins");

    private final String sigla;
    private final String nome;

    EstadoEnum(String sigla, String nome) {
        this.sigla = sigla;
        this.nome = nome;
    }

    public String getSigla() { return sigla; }
    public String getNome() { return nome; }

    @JsonCreator
    public static EstadoEnum fromValue(String value) {
        return Arrays.stream(values())
                .filter(e -> e.sigla.equalsIgnoreCase(value) || e.nome.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Estado inválido: " + value));
    }
}
