package com.mycompany.gestaobiblioteca;

public class Livro {

    private int id;
    private String isbn;
    private String titulo;
    private String autor;
    private boolean disponivel;

    // Contador estático para gerar IDs únicos
    private static int contadorId = 1;

    public Livro(String isbn, String titulo, String autor) {
        this.id = contadorId++;
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.disponivel = true; // Novo livro sempre disponível
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public String getEstado() {
        if (disponivel) {
            return "Disponível";
        } else {
            return "Emprestado";
        }
    }

    public String mostrarInformacoes() {
        return "ID: " + id + ", ISBN: " + isbn + ", Título: " + titulo
                + ", Autor: " + autor + ", Estado: " + getEstado();
    }

    public boolean isDisponivel() {
        return disponivel;
    }

}
