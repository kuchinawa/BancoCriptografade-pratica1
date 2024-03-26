package model;

import java.io.Serializable;

public class Usuario implements Serializable{
    private String nome;
    private String cpf;
    private String senha;
    private String email;
    private String saldo;

    public Usuario(String nome, String cpf, String senha, String email, String saldo) {
        this.nome = nome;
        this.cpf = cpf;
        this.senha = senha;
        this.email = email;
        this.saldo = saldo = "0.0";
    }

    public Usuario(String usuario){
        String[] dados = usuario.split("-");
        this.nome = dados[0];
        this.cpf = dados[1];
        this.senha = dados[2];
        this.email = dados[3];
        this.saldo = dados[4];
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String toString() {
        return (nome + "-" + cpf +  "-" + senha + "-" + email + "-" + saldo);
    }
}