package br.pucminas.sac.agente.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "agentes")
public class Agente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 120)
    private String nome;

    @NotBlank
    @Pattern(regexp = "\\d{14}", message = "CNPJ deve conter 14 dígitos")
    @Column(unique = true, length = 14)
    private String cnpj;

    @NotBlank
    @Size(min = 5, max = 200)
    private String endereco;

    @NotBlank
    @Size(min = 8, max = 20)
    private String telefone;

    @NotBlank
    @Size(min = 5, max = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    private TipoAgente tipo;

    @NotNull
    private Double capitalSocial;

    @Enumerated(EnumType.STRING)
    private StatusAgente status;

    public enum TipoAgente {
        EMPRESA, BANCO
    }

    public enum StatusAgente {
        ATIVO, INATIVO, SUSPENSO
    }

    public Agente() {
        this.status = StatusAgente.ATIVO;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public TipoAgente getTipo() { return tipo; }
    public void setTipo(TipoAgente tipo) { this.tipo = tipo; }
    public Double getCapitalSocial() { return capitalSocial; }
    public void setCapitalSocial(Double capitalSocial) { this.capitalSocial = capitalSocial; }
    public StatusAgente getStatus() { return status; }
    public void setStatus(StatusAgente status) { this.status = status; }
}

