package br.pucminas.sac.automovel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "automoveis")
public class Automovel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String matricula;

    @NotNull
    private Integer ano;

    @NotBlank
    @Size(min = 2, max = 50)
    private String marca;

    @NotBlank
    @Size(min = 2, max = 50)
    private String modelo;

    @NotBlank
    @Pattern(regexp = "[A-Z]{3}[0-9]{4}|[A-Z]{3}[0-9][A-Z][0-9]{2}", message = "Placa deve seguir padrão ABC1234 ou ABC1D23")
    @Column(unique = true)
    private String placa;

    @Enumerated(EnumType.STRING)
    private TipoPropriedade tipoPropriedade;

    private Long proprietarioId; // ID do cliente, empresa ou banco proprietário

    @Enumerated(EnumType.STRING)
    private StatusAutomovel status;

    public enum TipoPropriedade {
        CLIENTE, EMPRESA, BANCO
    }

    public enum StatusAutomovel {
        DISPONIVEL, ALUGADO, MANUTENCAO, INDISPONIVEL
    }

    public Automovel() {
        this.status = StatusAutomovel.DISPONIVEL;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public TipoPropriedade getTipoPropriedade() { return tipoPropriedade; }
    public void setTipoPropriedade(TipoPropriedade tipoPropriedade) { this.tipoPropriedade = tipoPropriedade; }
    public Long getProprietarioId() { return proprietarioId; }
    public void setProprietarioId(Long proprietarioId) { this.proprietarioId = proprietarioId; }
    public StatusAutomovel getStatus() { return status; }
    public void setStatus(StatusAutomovel status) { this.status = status; }
}
