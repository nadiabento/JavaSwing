package com.mycompany.gestaobiblioteca;

import java.util.ArrayList;
import java.util.Date;

public class Biblioteca {

    ArrayList<Livro> livros = new ArrayList<>();
    ArrayList<Membro> membros = new ArrayList<>();
    ArrayList<Emprestimo> emprestimos = new ArrayList<>();

    // Adicionar livro
    public void adicionarLivro(Livro livro) {
        livros.add(livro);
    }

    // Adicionar membro
    public void adicionarMembro(Membro membro) {
        membros.add(membro);
    }

    // Procurar livro por ID
    public Livro procurarLivroPorId(int id) {
        for (int i = 0; i < livros.size(); i++) {
            Livro livro = livros.get(i);
            if (livro.getId() == id) {
                return livro;
            }
        }
        return null;
    }

    // Procurar membro por ID
    public Membro procurarMembroPorId(int id) {
        for (int i = 0; i < membros.size(); i++) {
            Membro membro = membros.get(i);
            if (membro.getId() == id) {
                return membro;
            }
        }
        return null;
    }

    // Registrar empréstimo
    public void registarEmprestimo(int idLivro, int idMembro, Date dataEmprestimo, Date dataDevolucaoPrevista) {
        Livro livro = procurarLivroPorId(idLivro);
        if (livro != null && livro.isDisponivel()) {
            Emprestimo emprestimo = new Emprestimo(idLivro, idMembro, dataEmprestimo, dataDevolucaoPrevista);
            emprestimos.add(emprestimo);
            livro.setDisponivel(false);  // Marca livro como emprestado
        }
    }
    //Registrar devolução
    public void registarDevolucao(int idEmprestimo, Date dataDevolucaoEfetiva) {
        for (Emprestimo e : emprestimos) {
            if (e.getId() == idEmprestimo) {
                e.registrarDevolucao(dataDevolucaoEfetiva);
                Livro livro = procurarLivroPorId(e.getIdLivro());
                if (livro != null) {
                    livro.setDisponivel(true);
                }
                break;
            }
        }
    }

    // Retorna todos os empréstimos
    public ArrayList<Emprestimo> getEmprestimos() {
        return emprestimos;
    }
}
