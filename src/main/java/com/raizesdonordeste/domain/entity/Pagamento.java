package com.raizesdonordeste.domain.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pagamentos_seq")
    @SequenceGenerator(name = "pagamentos_seq", sequenceName = "pagamentos_id_seq", allocationSize = 1)
    private Long id;

    private BigDecimal valor;

    private String status;

    private String formaPagamento;

    LocalDateTime dataPagamento;

    @OneToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
}
