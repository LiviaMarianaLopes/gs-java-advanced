package br.com.fiap.gs.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GS_BICICLETA")
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "numero_serie")
    private String numeroSerie;
    @Column(name = "latitude")
    private String latitude;
    @Column(name = "longitude")
    private String londitude;
    @Enumerated(EnumType.STRING)  // Define que o valor do enum será salvo como 'B' ou 'P' no banco
    @Column(name = "TIPO_VEICULO", nullable = false)
    private TipoVeiculo tipoVeiculo;

}
