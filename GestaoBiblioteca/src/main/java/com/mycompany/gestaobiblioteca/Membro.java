package com.mycompany.gestaobiblioteca;

public class Membro {

    private int id;
    private String numeroSocio;
    private String primeiroNome;
    private String apelido;
    private String email;

    // Contador estático para gerar IDs únicos
    private static int contadorId = 1;

    public Membro(String numeroSocio, String primeiroNome, String apelido, String email) {
        this.id = contadorId++;
        this.numeroSocio = numeroSocio;
        this.primeiroNome = primeiroNome;
        this.apelido = apelido;
        this.email = email;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public String getNumeroSocio() {
        return numeroSocio;
    }

    public void setNumeroSocio(String numeroSocio) {
        this.numeroSocio = numeroSocio;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomeCompleto() {
        return primeiroNome + " " + apelido;
    }

    public String mostrarMembro() {
        return "ID: " + id + ", Nº Sócio: " + numeroSocio + ", Nome: "
                + primeiroNome + " " + apelido + ", Email: " + email;
    }
}
