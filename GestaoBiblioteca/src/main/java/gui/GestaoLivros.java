package gui;

import com.mycompany.gestaobiblioteca.Biblioteca;
import com.mycompany.gestaobiblioteca.Livro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.stream.Collectors; 

public class GestaoLivros extends javax.swing.JFrame {

    private Biblioteca biblioteca; // Instância da biblioteca principal
    private DefaultTableModel tableModelLivros;

    public GestaoLivros(Biblioteca biblioteca) { // Modificado para receber Biblioteca
        this.biblioteca = biblioteca;
        initComponents();
        setTitle("Gestão de Livros");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Importante!
        setLocationRelativeTo(null); // Centrar

        configurarTabela();
        configurarFiltro();
        atualizarTabelaLivros(this.biblioteca.getLivros()); // Popula a tabela inicialmente
    }

    private void configurarTabela() {
        tableModelLivros = (DefaultTableModel) jTableLivros.getModel(); // Usar jTableLivros

    }

    private void configurarFiltro() {

        jComboBoxFiltroCampo.setModel(new DefaultComboBoxModel<>(new String[]{"Título", "Autor", "ISBN"}));
        // Definir um valor padrão para o campo de filtro
        if (jComboBoxFiltroCampo.getItemCount() > 0) {
            jComboBoxFiltroCampo.setSelectedIndex(0);
        }
    }

    public void atualizarTabelaLivros(ArrayList<Livro> lista) {
        tableModelLivros.setRowCount(0); // Limpa a tabela

        if (lista == null) {
            return; // Segurança
        }
        for (Livro livro : lista) {
            tableModelLivros.addRow(new Object[]{
                livro.getTitulo(),
                livro.getIsbn(),
                livro.getAutor(),
                livro.getEstado() // Ou livro.isDisponivel() ? "Disponível" : "Emprestado"
            });
        }
    }

    private void procurarLivros() {
        String termoFiltro = jTextFieldFiltro.getText().toLowerCase().trim();
        String campoSelecionado = (String) jComboBoxFiltroCampo.getSelectedItem();

        if (termoFiltro.isEmpty()) {
            atualizarTabelaLivros(this.biblioteca.getLivros()); // Mostra todos se o filtro estiver vazio
            return;
        }

        ArrayList<Livro> livrosFiltrados = new ArrayList<>();
        for (Livro livro : this.biblioteca.getLivros()) {
            boolean match = false;
            if (campoSelecionado != null) {
                switch (campoSelecionado) {
                    case "Título":
                        if (livro.getTitulo() != null) {
                            match = livro.getTitulo().toLowerCase().contains(termoFiltro);
                        }
                        break;
                    case "Autor":
                        if (livro.getAutor() != null) {
                            match = livro.getAutor().toLowerCase().contains(termoFiltro);
                        }
                        break;
                    case "ISBN":
                        if (livro.getIsbn() != null) {
                            match = livro.getIsbn().toLowerCase().contains(termoFiltro);
                        }
                        break;
                }
            }
            if (match) {
                livrosFiltrados.add(livro);
            }
        }
        atualizarTabelaLivros(livrosFiltrados);
    }

    private void adicionarLivro() {
        // Passa 'this' como parent, 'true' para modal, e a instância da biblioteca
        DetalhesLivro detalhesJanela = new DetalhesLivro(this, true, this.biblioteca);
        detalhesJanela.setVisible(true);

        // Após a janela DetalhesLivro ser fechada, atualiza a tabela
        // A lógica de adicionar o livro à 'this.biblioteca' será feita dentro de DetalhesLivro
        atualizarTabelaLivros(this.biblioteca.getLivros());
    }

    private void editarLivro() {
        int selectedRow = jTableLivros.getSelectedRow();
        if (selectedRow >= 0) {
            // IMPORTANTE: Obter o livro da lista original da biblioteca,
            // especialmente se a tabela estiver filtrada ou ordenada.
            // Uma forma mais robusta é obter o ISBN/ID da linha selecionada e procurar na biblioteca.
            // Se a tabela for filtrada, este método de get(selectedRow) pode dar o livro errado.
            // Vamos pegar o ISBN da tabela e procurar o livro.
            String isbnSelecionado = (String) tableModelLivros.getValueAt(selectedRow, 1); // Coluna ISBN é a 1
            Livro livroParaEditar = null;
            for (Livro l : this.biblioteca.getLivros()) {
                if (l.getIsbn().equals(isbnSelecionado)) {
                    livroParaEditar = l;
                    break;
                }
            }

            if (livroParaEditar != null) {
                DetalhesLivro detalhesJanela = new DetalhesLivro(this, true, this.biblioteca, livroParaEditar);
                detalhesJanela.setVisible(true);
                atualizarTabelaLivros(this.biblioteca.getLivros());
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível encontrar o livro selecionado na biblioteca.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um livro para editar.", "Nenhum Livro Selecionado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removerLivro() {
        int selectedRow = jTableLivros.getSelectedRow();
        if (selectedRow >= 0) {
            String isbnSelecionado = (String) tableModelLivros.getValueAt(selectedRow, 1); // Coluna ISBN
            Livro livroParaRemover = null;
            int idLivroParaRemover = -1;

            // Encontrar o livro na lista principal da biblioteca pelo ISBN
            for (Livro livro : this.biblioteca.getLivros()) {
                if (livro.getIsbn().equals(isbnSelecionado)) {
                    livroParaRemover = livro;
                    idLivroParaRemover = livro.getId(); 
                    break;
                }
            }

            if (livroParaRemover != null) {
                // Verificar se o livro está emprestado
                boolean emprestado = false;
                for (com.mycompany.gestaobiblioteca.Emprestimo emp : this.biblioteca.getEmprestimos()) {
                    if (emp.getIdLivro() == livroParaRemover.getId() && emp.getDataDevolucaoEfetiva() == null) {
                        emprestado = true;
                        break;
                    }
                }

                if (emprestado) {
                    JOptionPane.showMessageDialog(this, "Este livro não pode ser removido porque está atualmente emprestado.", "Erro ao Remover", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Tem a certeza que deseja remover o livro: " + livroParaRemover.getTitulo() + "?",
                        "Confirmar Remoção",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {

                    boolean removidoComSucesso = this.biblioteca.removerLivroPorId(livroParaRemover.getId()); // Usa o ID do livro

                    if (removidoComSucesso) {
                        atualizarTabelaLivros(this.biblioteca.getLivros());
                        JOptionPane.showMessageDialog(this, "Livro removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Não foi possível remover o livro.\nVerifique se o livro ainda existe ou se há outras restrições.", "Falha ao Remover", JOptionPane.ERROR_MESSAGE);
                }
            }

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableLivros = new javax.swing.JTable();
        jButtonAddLivro = new javax.swing.JButton();
        jButtonEditarLivro = new javax.swing.JButton();
        jButtonRemoverLivro = new javax.swing.JButton();
        jComboBoxFiltroCampo = new javax.swing.JComboBox<>();
        jTextFieldFiltro = new javax.swing.JTextField();
        jButtonProcurar = new javax.swing.JButton();

        jButton1.setText("jButton1");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTableLivros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Título", "ISBN", "Autor", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableLivros);

        jButtonAddLivro.setText("Adicionar livro");
        jButtonAddLivro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddLivroActionPerformed(evt);
            }
        });

        jButtonEditarLivro.setText("Editar Livro");
        jButtonEditarLivro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditarLivroActionPerformed(evt);
            }
        });

        jButtonRemoverLivro.setText("Remover Livro");
        jButtonRemoverLivro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoverLivroActionPerformed(evt);
            }
        });

        jComboBoxFiltroCampo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextFieldFiltro.setText("Filtro");
        jTextFieldFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldFiltroActionPerformed(evt);
            }
        });

        jButtonProcurar.setText("Procurar");
        jButtonProcurar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonProcurarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jButtonAddLivro, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonEditarLivro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonRemoverLivro, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextFieldFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBoxFiltroCampo, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jButtonProcurar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxFiltroCampo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonProcurar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAddLivro)
                    .addComponent(jButtonEditarLivro)
                    .addComponent(jButtonRemoverLivro))
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFiltroActionPerformed

    }//GEN-LAST:event_jTextFieldFiltroActionPerformed

    private void jButtonProcurarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonProcurarActionPerformed
        procurarLivros();
    }//GEN-LAST:event_jButtonProcurarActionPerformed

    private void jButtonEditarLivroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditarLivroActionPerformed
        editarLivro();
    }//GEN-LAST:event_jButtonEditarLivroActionPerformed

    private void jButtonAddLivroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddLivroActionPerformed
        adicionarLivro();
    }//GEN-LAST:event_jButtonAddLivroActionPerformed

    private void jButtonRemoverLivroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoverLivroActionPerformed
        removerLivro();
    }//GEN-LAST:event_jButtonRemoverLivroActionPerformed
    private void ligarEventos() {
        jButtonAddLivro.addActionListener(e -> adicionarLivro());
        jButtonEditarLivro.addActionListener(e -> editarLivro());
        jButtonRemoverLivro.addActionListener(e -> removerLivro());
        jButtonProcurar.addActionListener(e -> procurarLivros());
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAddLivro;
    private javax.swing.JButton jButtonEditarLivro;
    private javax.swing.JButton jButtonProcurar;
    private javax.swing.JButton jButtonRemoverLivro;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBoxFiltroCampo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableLivros;
    private javax.swing.JTextField jTextFieldFiltro;
    // End of variables declaration//GEN-END:variables
}
