package br.com.deckmarket.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "TB_DECKMKT_CARD")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do card é obrigatório")
    private String nome;

    @NotBlank(message = "Edição do card é obrigatória")
    private String edicao;

    private Double preco;

    @NotBlank(message = "Raridade do card é obrigatória")
    private String raridade;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
