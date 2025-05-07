package com.mycompany.gestaobiblioteca;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Exportar {

    public static void exportarEmprestimos(String nomeFicheiro, ArrayList<Emprestimo> emprestimos) {
        try {
            FileWriter escritor = new FileWriter(nomeFicheiro);

            // Cabeçalhos
            escritor.write("ID,ID Livro,ID Membro,Data Empréstimo,Data Devolução Prevista,Data Devolução Efetiva,Estado\n");

            // Dados com for tradicional
            for (int i = 0; i < emprestimos.size(); i++) {
                Emprestimo e = emprestimos.get(i);

                escritor.write(e.getId() + "," +
                               e.getIdLivro() + "," +
                               e.getIdMembro() + "," +
                               e.getDataEmprestimo() + "," +
                               e.getDataDevolucaoPrevista() + "," +
                               e.getDataDevolucaoEfetiva() + "," +
                               e.getEstado() + "\n");
            }

            escritor.close();
            System.out.println("Exportado com sucesso para " + nomeFicheiro);
        } catch (IOException e) {
            System.out.println("Erro ao exportar: " + e.getMessage());
        }
    }
}
