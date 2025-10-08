package com.example.sigmastore;

public class Usuario {
    private int id;
    private String nome;
    private String cpf;
    private String senha;
    private boolean isAdmin;

    public Usuario(int id, String nome, String cpf, String senha, boolean isAdmin) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.senha = senha;
        this.isAdmin = isAdmin;
    }

    public Usuario() {}

    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isAdmin() {
        return isAdmin;
    }
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}