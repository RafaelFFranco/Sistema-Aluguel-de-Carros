package br.pucminas.sac.cliente.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 120)
    private String nomeCompleto;

    @NotBlank
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    @Column(unique = true, length = 11)
    private String cpf;

    @NotBlank
    @Pattern(regexp = "\\d{7,14}", message = "RG deve conter entre 7 e 14 dígitos")
    @Column(unique = true)
    private String rg;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    private String telefone;

    @NotBlank
    @Size(min = 5, max = 200)
    private String endereco;

    @NotBlank
    @Size(min = 2, max = 80)
    private String profissao;

    @Past
    private LocalDate dataNascimento;

    @Size(max = 200)
    private String empregadoresResumo;

    @Size(max = 200)
    private String rendimentosResumo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getProfissao() { return profissao; }
    public void setProfissao(String profissao) { this.profissao = profissao; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getEmpregadoresResumo() { return empregadoresResumo; }
    public void setEmpregadoresResumo(String empregadoresResumo) { this.empregadoresResumo = empregadoresResumo; }
    public String getRendimentosResumo() { return rendimentosResumo; }
    public void setRendimentosResumo(String rendimentosResumo) { this.rendimentosResumo = rendimentosResumo; }
}


