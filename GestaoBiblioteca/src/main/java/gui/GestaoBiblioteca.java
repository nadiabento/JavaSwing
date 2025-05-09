package gui;

import com.mycompany.gestaobiblioteca.Biblioteca;
import com.mycompany.gestaobiblioteca.Emprestimo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent; // Para os mouseClicked dos JMenu
import java.util.Date;
import java.text.SimpleDateFormat; // Para formatar datas

public class GestaoBiblioteca extends javax.swing.JFrame {

    private Biblioteca biblioteca;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Formato para datas

    /**
     * Creates new form GestaoBiblioteca
     * @param biblioteca
     */
    public GestaoBiblioteca(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        initComponents();
        setLocationRelativeTo(null); // Centra a janela
        setTitle("Sistema de Gestão de Biblioteca"); // Adiciona um título à janela principal
        atualizarEmprestimos();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaEmprestimosRecentes = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaDevolucoesProximas = new javax.swing.JTextArea();
        jLabelEmprestimosRecentes = new javax.swing.JLabel();
        jLabelDevolucoesProximas = new javax.swing.JLabel();
        jMenuBarPrincipal = new javax.swing.JMenuBar();
        jMenuMembros = new javax.swing.JMenu();
        jMenuLivros = new javax.swing.JMenu();
        jMenuEmprestimos = new javax.swing.JMenu();
        jMenuExit = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextAreaEmprestimosRecentes.setEditable(false);
        jTextAreaEmprestimosRecentes.setColumns(20);
        jTextAreaEmprestimosRecentes.setRows(5);
        jScrollPane1.setViewportView(jTextAreaEmprestimosRecentes);

        jTextAreaDevolucoesProximas.setEditable(false);
        jTextAreaDevolucoesProximas.setColumns(20);
        jTextAreaDevolucoesProximas.setRows(5);
        jScrollPane2.setViewportView(jTextAreaDevolucoesProximas);

        jLabelEmprestimosRecentes.setText("Emprestimos Recentes");

        jLabelDevolucoesProximas.setText("Devoluções Próximas");

        jMenuMembros.setText("Membros");
        jMenuMembros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuMembrosMouseClicked(evt);
            }
        });
        jMenuBarPrincipal.add(jMenuMembros);

        jMenuLivros.setText("Livros");
        jMenuLivros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuLivrosMouseClicked(evt);
            }
        });
        jMenuBarPrincipal.add(jMenuLivros);

        jMenuEmprestimos.setText("Emprestimos");
        jMenuEmprestimos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuEmprestimosMouseClicked(evt);
            }
        });
        jMenuBarPrincipal.add(jMenuEmprestimos);

        jMenuExit.setText("Exit");
        jMenuExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuExitMouseClicked(evt);
            }
        });
        jMenuBarPrincipal.add(jMenuExit);

        setJMenuBar(jMenuBarPrincipal);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelDevolucoesProximas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelEmprestimosRecentes, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabelEmprestimosRecentes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(jLabelDevolucoesProximas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuMembrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuMembrosMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) { // Garante que foi clique esquerdo
            abrirGestaoMembros();
        }
    }//GEN-LAST:event_jMenuMembrosMouseClicked

    private void jMenuLivrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuLivrosMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) {
            abrirGestaoLivros();
        }
    }//GEN-LAST:event_jMenuLivrosMouseClicked

    private void jMenuExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuExitMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) {
            fecharAplicacao();
        }
    }//GEN-LAST:event_jMenuExitMouseClicked

    private void jMenuEmprestimosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuEmprestimosMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) { // Garante que foi clique esquerdo
            abrirGestaoEmprestimos();
        }
    }//GEN-LAST:event_jMenuEmprestimosMouseClicked

    private void abrirGestaoMembros() {
        // Passa a instância da biblioteca para a janela GestaoMembros
        GestaoMembros membrosUI = new GestaoMembros(this.biblioteca);
        membrosUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Para não fechar a app toda
        membrosUI.setLocationRelativeTo(this); // Centra em relação à janela principal
        membrosUI.setVisible(true);
    }

    private void abrirGestaoLivros() {

        // Passa a instância da biblioteca para a janela GestaoLivros
        GestaoLivros livrosUI = new GestaoLivros(this.biblioteca);
        livrosUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Para não fechar a app toda
        livrosUI.setLocationRelativeTo(this); // Centra em relação à janela principal
        livrosUI.setVisible(true);
    }

    private void abrirGestaoEmprestimos() {
        GestaoEmprestimos emprestimosUI = new GestaoEmprestimos(this.biblioteca);
        emprestimosUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Para não fechar a app toda
        emprestimosUI.setLocationRelativeTo(this); // Centra em relação à janela principal
        emprestimosUI.setVisible(true);
    }

    private void fecharAplicacao() {
        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Tem a certeza que deseja sair?",
                "Confirmar Saída",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (resposta == JOptionPane.YES_OPTION) {
            dispose(); // Fecha esta janela
            System.exit(0); // Termina a aplicação
        }
    }

    public void atualizarEmprestimos() {
        StringBuilder emprestimosRecentesStr = new StringBuilder();
        StringBuilder devolucoesProximasStr = new StringBuilder();

        Date hoje = new Date();
        // Para testar, vamos adicionar um empréstimo de exemplo se a lista estiver vazia
        // REMOVER ISTO DEPOIS DE TESTAR A LIGAÇÃO COM O BACKEND
        if (biblioteca.getEmprestimos().isEmpty() && biblioteca.getLivros().isEmpty() && biblioteca.getMembros().isEmpty()) {
            System.out.println("Adicionando dados de teste para GestaoBiblioteca UI...");
            com.mycompany.gestaobiblioteca.Livro lTeste = new com.mycompany.gestaobiblioteca.Livro("111-TESTE", "Livro de Teste UI", "Autor Teste");
            com.mycompany.gestaobiblioteca.Membro mTeste = new com.mycompany.gestaobiblioteca.Membro("S999", "Utilizador", "Teste", "ui@teste.com");
            biblioteca.adicionarLivro(lTeste);
            biblioteca.adicionarMembro(mTeste);
            
            Date dataEmp = new Date(hoje.getTime() - (3 * 24 * 60 * 60 * 1000)); // Emprestado há 3 dias
            Date dataDevPrev = new Date(hoje.getTime() + (2 * 24 * 60 * 60 * 1000)); // Devolução em 2 dias
            biblioteca.registarEmprestimo(lTeste.getId(), mTeste.getId(), dataEmp, dataDevPrev);
        }
        // FIM DO BLOCO DE TESTE

        for (Emprestimo e : biblioteca.getEmprestimos()) {
            // Para Empréstimos Recentes, podemos mostrar todos os ativos ou os últimos X
            // Aqui, vamos mostrar todos os empréstimos para simplificar
            String infoEmprestimo = String.format("ID: %d, Livro ID: %d, Membro ID: %d, Data Emp: %s, Prev. Dev: %s, Estado: %s",
                e.getId(),
                e.getIdLivro(),
                e.getIdMembro(),
                sdf.format(e.getDataEmprestimo()),
                sdf.format(e.getDataDevolucaoPrevista()),
                e.getEstado()
            );
            emprestimosRecentesStr.append(infoEmprestimo).append("\n");

            // Para Devoluções Próximas
            if (e.getDataDevolucaoEfetiva() == null) { // Se ainda não foi devolvido
                long diffMillis = e.getDataDevolucaoPrevista().getTime() - hoje.getTime();
                long diasRestantes = diffMillis / (1000 * 60 * 60 * 24);

                if (diasRestantes >= 0 && diasRestantes <= 3) { // Devolução nos próximos 0 a 3 dias
                    devolucoesProximasStr.append(infoEmprestimo).append("\n");
                }
            }
        }

        jTextAreaEmprestimosRecentes.setText(emprestimosRecentesStr.toString());
        jTextAreaDevolucoesProximas.setText(devolucoesProximasStr.toString());

    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GestaoBiblioteca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(() -> {
            Biblioteca bibliotecaCentral = new Biblioteca(); // Instância ÚNICA da biblioteca
            new GestaoBiblioteca(bibliotecaCentral).setVisible(true);
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelDevolucoesProximas;
    private javax.swing.JLabel jLabelEmprestimosRecentes;
    private javax.swing.JMenuBar jMenuBarPrincipal;
    private javax.swing.JMenu jMenuEmprestimos;
    private javax.swing.JMenu jMenuExit;
    private javax.swing.JMenu jMenuLivros;
    private javax.swing.JMenu jMenuMembros;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextAreaDevolucoesProximas;
    private javax.swing.JTextArea jTextAreaEmprestimosRecentes;
    // End of variables declaration//GEN-END:variables
}
