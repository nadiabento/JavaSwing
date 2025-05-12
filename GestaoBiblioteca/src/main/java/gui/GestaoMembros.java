package gui;

import com.mycompany.gestaobiblioteca.Biblioteca;
import com.mycompany.gestaobiblioteca.Membro; // Importa a classe Membro
import com.mycompany.gestaobiblioteca.Emprestimo; // Para verificar empréstimos

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class GestaoMembros extends javax.swing.JFrame {

    private Biblioteca biblioteca;
    private DefaultTableModel tableModelMembros;

    // Construtor modificado para receber a instância da Biblioteca
    public GestaoMembros(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        initComponents(); // Inicializa os componentes da UI desenhados
        setTitle("Gestão de Membros");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Para não fechar a aplicação toda
        setLocationRelativeTo(null); // Centrar a janela

        configurarTabelaMembros();
        configurarFiltroMembros();
        atualizarTabelaMembros(this.biblioteca.getMembros()); // Popula a tabela inicialmente
    }

    
    
    public GestaoMembros() {        
       
        this(new Biblioteca()); 
        System.out.println("AVISO: GestaoMembros chamado sem instância de Biblioteca. Usando uma nova.");
        
    }

    private void configurarTabelaMembros() {
        
        tableModelMembros = (DefaultTableModel) jTableMembros.getModel();
        
    }

    private void configurarFiltroMembros() {
        
        jComboBoxFiltroCampoMembro.setModel(new DefaultComboBoxModel<>(new String[]{"Nome", "Número Sócio", "Email"}));
        if (jComboBoxFiltroCampoMembro.getItemCount() > 0) {
            jComboBoxFiltroCampoMembro.setSelectedIndex(0); // Padrão para "Nome"
        }
    }

    public void atualizarTabelaMembros(ArrayList<Membro> lista) {
        tableModelMembros.setRowCount(0); // Limpa a tabela

        if (lista == null) {
            return;
        }

        for (Membro membro : lista) {
            tableModelMembros.addRow(new Object[]{
                membro.getNumeroSocio(),
                membro.getNomeCompleto(),
                membro.getEmail()
            
            });
        }
    }

    private void procurarMembros() {
        String termoFiltro = jTextFieldFiltroMembro.getText().toLowerCase().trim(); 
        String campoSelecionado = (String) jComboBoxFiltroCampoMembro.getSelectedItem();

        if (termoFiltro.isEmpty()) {
            atualizarTabelaMembros(this.biblioteca.getMembros());
            return;
        }

        ArrayList<Membro> membrosFiltrados = new ArrayList<>();
        for (Membro membro : this.biblioteca.getMembros()) {
            boolean match = false;
            if (campoSelecionado != null) {
                switch (campoSelecionado) {
                    case "Nome":
                        if (membro.getNomeCompleto() != null) {
                            match = membro.getNomeCompleto().toLowerCase().contains(termoFiltro);
                        }
                        break;
                    case "Número Sócio":
                        if (membro.getNumeroSocio() != null) {
                            match = membro.getNumeroSocio().toLowerCase().contains(termoFiltro);
                        }
                        break;
                    case "Email":
                        if (membro.getEmail() != null) {
                            match = membro.getEmail().toLowerCase().contains(termoFiltro);
                        }
                        break;
                }
            }
            if (match) {
                membrosFiltrados.add(membro);
            }
        }
        atualizarTabelaMembros(membrosFiltrados);
    }

    private void adicionarMembro() {
        DetalhesMembro detalhesJanela = new DetalhesMembro(this, true, this.biblioteca);
        detalhesJanela.setVisible(true);
        atualizarTabelaMembros(this.biblioteca.getMembros());
    }

    private void editarMembro() {
        int selectedRow = jTableMembros.getSelectedRow();
        if (selectedRow >= 0) {
            // Obter o membro pela informação única na tabela (ex: Número de Sócio)
            String numeroSocioSelecionado = (String) tableModelMembros.getValueAt(selectedRow, 0); // Coluna 0: Número Sócio
            Membro membroParaEditar = null;
            for (Membro m : this.biblioteca.getMembros()) {
                if (m.getNumeroSocio().equals(numeroSocioSelecionado)) {
                    membroParaEditar = m;
                    break;
                }
            }

            if (membroParaEditar != null) {
                DetalhesMembro detalhesJanela = new DetalhesMembro(this, true, this.biblioteca, membroParaEditar);
                detalhesJanela.setVisible(true);
                atualizarTabelaMembros(this.biblioteca.getMembros());
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível encontrar o membro selecionado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um membro para editar.", "Nenhum Membro Selecionado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removerMembro() {
        int selectedRow = jTableMembros.getSelectedRow();
        if (selectedRow >= 0) {
            String numeroSocioSelecionado = (String) tableModelMembros.getValueAt(selectedRow, 0);
            Membro membroParaRemover = null;

            for (Membro m : this.biblioteca.getMembros()) {
                if (m.getNumeroSocio().equals(numeroSocioSelecionado)) {
                    membroParaRemover = m;
                    break;
                }
            }

            if (membroParaRemover != null) {
                // Verificar se o membro tem empréstimos ativos
                boolean temEmprestimosAtivos = false;
                for (Emprestimo emp : this.biblioteca.getEmprestimos()) {
                    if (emp.getIdMembro() == membroParaRemover.getId() && emp.getDataDevolucaoEfetiva() == null) {
                        temEmprestimosAtivos = true;
                        break;
                    }
                }

                if (temEmprestimosAtivos) {
                    JOptionPane.showMessageDialog(this, "Este membro não pode ser removido pois possui empréstimos ativos.", "Erro ao Remover", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Tem a certeza que deseja remover o membro: " + membroParaRemover.getNomeCompleto() + "?",
                        "Confirmar Remoção",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean removidoComSucesso = this.biblioteca.removerMembroPorId(membroParaRemover.getId()); // Usa o ID do membro

                    if (removidoComSucesso) {
                        atualizarTabelaMembros(this.biblioteca.getMembros());
                        JOptionPane.showMessageDialog(this, "Membro removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // A mensagem de "membro com empréstimos" já é mostrada antes.
                        // Esta mensagem seria para outros casos de falha.
                        JOptionPane.showMessageDialog(this, "Não foi possível remover o membro.\nVerifique se o membro ainda existe ou se há outras restrições.", "Falha ao Remover", JOptionPane.ERROR_MESSAGE);
                    }
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableMembros = new javax.swing.JTable();
        jButtonAddMembro = new javax.swing.JButton();
        jButtonEditMembro = new javax.swing.JButton();
        jButtonRemoveMembro = new javax.swing.JButton();
        jComboBoxFiltroCampoMembro = new javax.swing.JComboBox<>();
        jTextFieldFiltroMembro = new javax.swing.JTextField();
        jButtonProcurarMembro = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTableMembros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Número", "Nome", "Contacto"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableMembros.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(jTableMembros);

        jButtonAddMembro.setText("Adicionar Membro");
        jButtonAddMembro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddMembroActionPerformed(evt);
            }
        });

        jButtonEditMembro.setText("Editar Membro");
        jButtonEditMembro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditMembroActionPerformed(evt);
            }
        });

        jButtonRemoveMembro.setText("Remover Membro");
        jButtonRemoveMembro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveMembroActionPerformed(evt);
            }
        });

        jComboBoxFiltroCampoMembro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextFieldFiltroMembro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldFiltroMembroActionPerformed(evt);
            }
        });

        jButtonProcurarMembro.setText("Procurar");
        jButtonProcurarMembro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonProcurarMembroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jTextFieldFiltroMembro)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxFiltroCampoMembro, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonProcurarMembro, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonAddMembro)
                        .addGap(15, 15, 15)
                        .addComponent(jButtonEditMembro, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonRemoveMembro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxFiltroCampoMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldFiltroMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonProcurarMembro))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAddMembro)
                    .addComponent(jButtonEditMembro)
                    .addComponent(jButtonRemoveMembro))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldFiltroMembroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFiltroMembroActionPerformed
        jButtonProcurarMembroActionPerformed(evt);
    }//GEN-LAST:event_jTextFieldFiltroMembroActionPerformed

    private void jButtonRemoveMembroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveMembroActionPerformed
        removerMembro();
    }//GEN-LAST:event_jButtonRemoveMembroActionPerformed

    private void jButtonProcurarMembroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonProcurarMembroActionPerformed
        procurarMembros();
    }//GEN-LAST:event_jButtonProcurarMembroActionPerformed

    private void jButtonAddMembroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddMembroActionPerformed
        adicionarMembro();
    }//GEN-LAST:event_jButtonAddMembroActionPerformed

    private void jButtonEditMembroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditMembroActionPerformed
        editarMembro();
    }//GEN-LAST:event_jButtonEditMembroActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddMembro;
    private javax.swing.JButton jButtonEditMembro;
    private javax.swing.JButton jButtonProcurarMembro;
    private javax.swing.JButton jButtonRemoveMembro;
    private javax.swing.JComboBox<String> jComboBoxFiltroCampoMembro;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableMembros;
    private javax.swing.JTextField jTextFieldFiltroMembro;
    // End of variables declaration//GEN-END:variables
}
