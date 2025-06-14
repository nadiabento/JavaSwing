package gui;

import com.mycompany.gestaobiblioteca.Biblioteca;
import com.mycompany.gestaobiblioteca.Livro;
import javax.swing.*;

public class DetalhesLivro extends javax.swing.JDialog {

    private Biblioteca biblioteca;
    private Livro livroParaOperacao;
    private boolean edicao; // Flag para saber se é adição ou edição

    // Construtor para ADICIONAR novo livro
    public DetalhesLivro(java.awt.Frame parent, boolean modal, Biblioteca biblioteca) {
        super(parent, modal);
        this.biblioteca = biblioteca;
        this.livroParaOperacao = null; // Indica que é um novo livro
        this.edicao = false;
        initComponents();
        setTitle("Adicionar Novo Livro");
        setLocationRelativeTo(parent); // Centrar em relação à janela pai
        // Limpar campos para garantir que estão vazios para um novo livro
        jTextField_ISBNLivro.setText("");
        jTextFieldTituloLivro.setText("");
        jTextFieldAutorLivro.setText("");
        jButtonAdicionar.setText("Adicionar");
    }

    // Construtor para EDITAR livro existente
    public DetalhesLivro(java.awt.Frame parent, boolean modal, Biblioteca biblioteca, Livro livroExistente) {
        super(parent, modal);
        this.biblioteca = biblioteca;
        this.livroParaOperacao = livroExistente;
        this.edicao = true;
        initComponents();
        setTitle("Editar Livro");
        setLocationRelativeTo(parent);
        carregarDadosLivro();
        jButtonAdicionar.setText("Guardar Alterações"); // Mudar texto do botão para edição
    }

    private void carregarDadosLivro() {
        if (livroParaOperacao != null) {
            jTextField_ISBNLivro.setText(livroParaOperacao.getIsbn());
            jTextFieldTituloLivro.setText(livroParaOperacao.getTitulo());
            jTextFieldAutorLivro.setText(livroParaOperacao.getAutor());
        }
    }

    
    private void onConfirmar() {
        String isbn = jTextField_ISBNLivro.getText().trim();
        String titulo = jTextFieldTituloLivro.getText().trim();
        String autor = jTextFieldAutorLivro.getText().trim();

        if (isbn.isEmpty() || titulo.isEmpty() || autor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Campos Vazios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificar se o ISBN já existe (APENAS SE FOR UM NOVO LIVRO ou SE O ISBN FOI ALTERADO NA EDIÇÃO)
        if (!edicao || (edicao && !livroParaOperacao.getIsbn().equals(isbn))) {
            for (Livro l : biblioteca.getLivros()) {
                if (l.getIsbn().equalsIgnoreCase(isbn)) {
                    // Se estivermos a editar e o ISBN encontrado pertencer ao próprio livro que estamos a editar, permite.
                    if (edicao && l.getId() == livroParaOperacao.getId()) {
                        // É o mesmo livro, não há conflito de ISBN.
                    } else {
                        JOptionPane.showMessageDialog(this, "Já existe um livro com este ISBN.", "ISBN Duplicado", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
        }

        if (!edicao) { // Adicionar novo livro
            // O construtor do backend é: Livro(String isbn, String titulo, String autor)
            Livro novoLivro = new Livro(isbn, titulo, autor);
            biblioteca.adicionarLivro(novoLivro);
            JOptionPane.showMessageDialog(this, "Livro adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else { // Editar livro existente
            livroParaOperacao.setIsbn(isbn);
            livroParaOperacao.setTitulo(titulo);
            livroParaOperacao.setAutor(autor);
            JOptionPane.showMessageDialog(this, "Livro atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }

        dispose(); // Fecha a janela de detalhes
    }

    private void onCancelar() {
        dispose(); // Apenas fecha a janela
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonAdicionar = new javax.swing.JButton();
        jButtoncancelar = new javax.swing.JButton();
        jTextField_ISBNLivro = new javax.swing.JTextField();
        jTextFieldTituloLivro = new javax.swing.JTextField();
        jTextFieldAutorLivro = new javax.swing.JTextField();
        jLabel_ISBN = new javax.swing.JLabel();
        jLabelTitulo = new javax.swing.JLabel();
        jLabelAutor = new javax.swing.JLabel();
        jLabelDetalhesLivro = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButtonAdicionar.setText("Adicionar");
        jButtonAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAdicionarActionPerformed(evt);
            }
        });

        jButtoncancelar.setBackground(new java.awt.Color(204, 0, 0));
        jButtoncancelar.setForeground(new java.awt.Color(255, 255, 255));
        jButtoncancelar.setText("Cancelar");
        jButtoncancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtoncancelarActionPerformed(evt);
            }
        });

        jTextField_ISBNLivro.setText("ISBN do Livro");
        jTextField_ISBNLivro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_ISBNLivroActionPerformed(evt);
            }
        });

        jTextFieldTituloLivro.setText("Título do Livro");
        jTextFieldTituloLivro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTituloLivroActionPerformed(evt);
            }
        });

        jTextFieldAutorLivro.setText("Autor do Livro");

        jLabel_ISBN.setText("ISBN");

        jLabelTitulo.setText("Título");

        jLabelAutor.setText("Autor");

        jLabelDetalhesLivro.setText("Detalhes do Livro");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel_ISBN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField_ISBNLivro)
                            .addComponent(jTextFieldTituloLivro, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                            .addComponent(jTextFieldAutorLivro)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtoncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabelDetalhesLivro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelDetalhesLivro, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_ISBNLivro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_ISBN))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldTituloLivro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTitulo))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldAutorLivro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelAutor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAdicionar)
                    .addComponent(jButtoncancelar))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField_ISBNLivroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_ISBNLivroActionPerformed
        
    }//GEN-LAST:event_jTextField_ISBNLivroActionPerformed

    private void jTextFieldTituloLivroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTituloLivroActionPerformed
        
    }//GEN-LAST:event_jTextFieldTituloLivroActionPerformed

    private void jButtonAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAdicionarActionPerformed
        onConfirmar();
    }//GEN-LAST:event_jButtonAdicionarActionPerformed

    private void jButtoncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtoncancelarActionPerformed
        onCancelar();
    }//GEN-LAST:event_jButtoncancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdicionar;
    private javax.swing.JButton jButtoncancelar;
    private javax.swing.JLabel jLabelAutor;
    private javax.swing.JLabel jLabelDetalhesLivro;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabel_ISBN;
    private javax.swing.JTextField jTextFieldAutorLivro;
    private javax.swing.JTextField jTextFieldTituloLivro;
    private javax.swing.JTextField jTextField_ISBNLivro;
    // End of variables declaration//GEN-END:variables
}
