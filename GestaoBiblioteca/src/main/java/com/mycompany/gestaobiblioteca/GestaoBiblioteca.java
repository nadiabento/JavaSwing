package com.mycompany.gestaobiblioteca;

import java.util.Date;

public class GestaoBiblioteca {

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();

        Livro l1 = new Livro("978-0321765723", "Effective Java", "Joshua Bloch");
        Membro maria = new Membro("S001", "Maria", "Santos", "maria@email.com");

        biblioteca.adicionarLivro(l1);
        biblioteca.adicionarMembro(maria);

        // Criar empréstimo
        Date hoje = new Date();
        Date devolucaoPrevista = new Date(hoje.getTime() + 604800000); // 7 dias em milissegundos

        biblioteca.registarEmprestimo(l1.getId(), maria.getId(), hoje, devolucaoPrevista);

        biblioteca.listarLivros();
        biblioteca.listarMembros();
        biblioteca.listarEmprestimos();
    }
}
