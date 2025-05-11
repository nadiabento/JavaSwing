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

    public ArrayList<Livro> getLivros() {
        return livros;
    }

    public ArrayList<Membro> getMembros() { // Verifica o tipo de retorno
        return membros;
    }

    //remover os livos, exceto se este esteja atualmente emprestado.
    public boolean removerLivroPorId(int id) {
        Livro livroParaRemover = procurarLivroPorId(id);
        if (livroParaRemover == null) {
            System.out.println("BACKEND: Tentativa de remover livro ID " + id + " que não foi encontrado.");
            return false; // Livro não encontrado
        }
        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getIdLivro() == id && emprestimo.getDataDevolucaoEfetiva() == null) {
                System.out.println("BACKEND: Tentativa de remover livro (ID: " + id + ") que está emprestado. Remoção não permitida.");
                return false; // Não remove se estiver emprestado
            }
        }
        boolean removido = livros.remove(livroParaRemover); // remove(Object) retorna boolean
        if (removido) {
            System.out.println("BACKEND: Livro ID " + id + " removido com sucesso.");
        } else {
            // Isto não deveria acontecer se o livro foi encontrado e não estava emprestado,
            // mas é uma verificação extra.
            System.out.println("BACKEND: Falha ao remover livro ID " + id + " da lista (inesperado).");
        }
        return removido;
    }

    public boolean removerMembroPorId(int id) {
        Membro membroParaRemover = procurarMembroPorId(id);
        if (membroParaRemover == null) {
            System.out.println("Tentativa de remover membro ID " + id + " que não foi encontrado.");
            return false; // Membro não encontrado
        }

        // Verificar se o membro tem empréstimos ativos
        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getIdMembro() == id && emprestimo.getDataDevolucaoEfetiva() == null) {
                System.out.println("BACKEND: Membro ID " + id + " não pode ser removido pois tem empréstimos ativos.");
                return false; // Não remove se tiver empréstimos ativos
            }
        }
        boolean removido = membros.remove(membroParaRemover);
        if (removido) {
            System.out.println("BACKEND: Membro ID " + id + " removido com sucesso.");
        } else {
            System.out.println("BACKEND: Falha ao remover membro ID " + id + " da lista (inesperado).");
        }
        return removido;
    }// Procurar membro por ID

    public Membro procurarMembroPorId(int id) {
        for (int i = 0; i < membros.size(); i++) {
            Membro membro = membros.get(i);
            if (membro.getId() == id) {
                return membro;
            }
        }
        return null;
    }

    // Registrar empréstimo (verifica se o livro está disponível)
    public boolean registarEmprestimo(int idLivro, int idMembro, Date dataEmprestimo, Date dataDevolucaoPrevista) {

        Livro livro = procurarLivroPorId(idLivro);
        Membro membro = procurarMembroPorId(idMembro); // Importante verificar o membro

        if (livro != null && membro != null && livro.isDisponivel()) {
            Emprestimo emprestimo = new Emprestimo(idLivro, idMembro, dataEmprestimo, dataDevolucaoPrevista);
            emprestimos.add(emprestimo);
            livro.setDisponivel(false);
            return true;
        }
        return false;
    }

    public boolean registarDevolucao(int idEmprestimo, Date dataDevolucaoEfetiva) {
        for (Emprestimo e : emprestimos) {
            if (e.getId() == idEmprestimo && e.getDataDevolucaoEfetiva() == null) { // Só devolve se não estiver já devolvido
                e.registrarDevolucao(dataDevolucaoEfetiva);
                Livro livro = procurarLivroPorId(e.getIdLivro());
                if (livro != null) {
                    livro.setDisponivel(true);
                }
                return true; // DIZ QUE FOI UM SUCESSO
            }
        }
        return false; // DIZ QUE FALHOU (não encontrou o empréstimo ou já estava devolvido)
    }

    // Retorna todos os empréstimos
    public ArrayList<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    // Lista todos os livros
    public void listarLivros() {
        for (int i = 0; i < livros.size(); i++) {
            System.out.println(livros.get(i).mostrarInformacoes());
        }
    }

    // Lista todos os membros
    public void listarMembros() {
        for (int i = 0; i < membros.size(); i++) {
            System.out.println(membros.get(i).mostrarMembro());
        }
    }

    // Lista todos os empréstimos
    public void listarEmprestimos() {
        for (int i = 0; i < emprestimos.size(); i++) {
            System.out.println(emprestimos.get(i).mostrarEmprestimo());
        }
    }

}
