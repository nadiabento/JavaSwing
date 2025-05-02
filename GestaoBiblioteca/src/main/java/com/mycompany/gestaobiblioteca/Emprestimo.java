package com.mycompany.gestaobiblioteca;

import java.util.Date;

public class Emprestimo {

    // Atributos principais de um empréstimo
    private int id;
    private int idLivro;
    private int idMembro;
    private Date dataEmprestimo;
    private Date dataDevolucaoPrevista;
    private Date dataDevolucaoEfetiva;

    // Contador estático para gerar IDs únicos automaticamente
    private static int contadorId = 1;

    // Construtor: cria um novo empréstimo
    public Emprestimo(int idLivro, int idMembro, Date dataEmprestimo, Date dataDevolucaoPrevista) {
        this.id = contadorId++;  // Atribui um ID único e incrementa o contador
        this.idLivro = idLivro;
        this.idMembro = idMembro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucaoEfetiva = null;  // Ainda não devolvido no momento da criação
    }

    // Getters: permitem acesso aos dados do empréstimo
    public int getId() {
        return id;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public int getIdMembro() {
        return idMembro;
    }

    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    public Date getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public Date getDataDevolucaoEfetiva() {
        return dataDevolucaoEfetiva;
    }

    // Método para registar a devolução do livro
    public void registrarDevolucao(Date dataDevolucaoEfetiva) {
        this.dataDevolucaoEfetiva = dataDevolucaoEfetiva;
    }

    // Método que calcula o estado atual do empréstimo com base nas datas
    public String getEstado() {
        if (dataDevolucaoEfetiva != null) {
            return "Devolvido";  // Já foi devolvido
        } else if (new Date().after(dataDevolucaoPrevista)) {
            return "Atrasado";  //
        } else {
            return "Ativo";  // Ainda está dentro do prazo
        }
    }

    // Método utilitário para mostrar as informações do empréstimo como texto
    public String mostrarEmprestimo() {
        return "ID: " + id
                + ", Livro ID: " + idLivro
                + ", Membro ID: " + idMembro
                + ", Data Empréstimo: " + dataEmprestimo
                + ", Devolução Prevista: " + dataDevolucaoPrevista
                + ", Estado: " + getEstado();
    }
}
