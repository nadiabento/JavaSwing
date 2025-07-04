package gui;

import com.mycompany.gestaobiblioteca.Biblioteca;
import com.mycompany.gestaobiblioteca.Membro; // Importar a classe Membro
import javax.swing.*;

public class DetalhesMembro extends javax.swing.JDialog {

    private Biblioteca biblioteca;
    private Membro membroParaOperacao;
    private boolean edicao;

    // Construtor para ADICIONAR novo membro
    public DetalhesMembro(java.awt.Frame parent, boolean modal, Biblioteca biblioteca) {
        super(parent, modal);
        this.biblioteca = biblioteca;
        this.membroParaOperacao = null; 
        this.edicao = false;            
        initComponents();
        // Define a operação de fecho padrão APÓS initComponents
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Adicionar Novo Membro");
        setLocationRelativeTo(parent);
        // Limpar campos para garantir que estão vazios para um novo membro
        jTextFieldNumSocio.setText("");
        jTextFieldPrimeiroNome.setText("");
        jTextFieldApelido.setText("");
        jTextFieldEmail.setText("");
        jButtonAdd.setText("Adicionar");
    }

    // Construtor para EDITAR membro existente
    public DetalhesMembro(java.awt.Frame parent, boolean modal, Biblioteca biblioteca, Membro membroExistente) {
        super(parent, modal);
        this.biblioteca = biblioteca;
        this.membroParaOperacao = membroExistente; 
        this.edicao = true;
        initComponents();
        // Define a operação de fecho padrão APÓS initComponents
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editar Membro");
        setLocationRelativeTo(parent);
        carregarDadosMembro();
        jButtonAdd.setText("Guardar Alterações");
    }

    private void carregarDadosMembro() {
        if (membroParaOperacao != null) {
            jTextFieldNumSocio.setText(membroParaOperacao.getNumeroSocio());
            jTextFieldPrimeiroNome.setText(membroParaOperacao.getPrimeiroNome());
            jTextFieldApelido.setText(membroParaOperacao.getApelido());
            jTextFieldEmail.setText(membroParaOperacao.getEmail());
        }
    }

    private void onConfirmar() {
        String numSocio = jTextFieldNumSocio.getText().trim();
        String primeiroNome = jTextFieldPrimeiroNome.getText().trim();
        String apelido = jTextFieldApelido.getText().trim();
        String email = jTextFieldEmail.getText().trim();

        if (numSocio.isEmpty() || primeiroNome.isEmpty() || apelido.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Campos Vazios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Formato de email inválido: Falta o ( @ ) ou um ( . )", "Email Inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!edicao || (edicao && !membroParaOperacao.getNumeroSocio().equalsIgnoreCase(numSocio))) {
            for (Membro m : biblioteca.getMembros()) {
                if (m.getNumeroSocio().equalsIgnoreCase(numSocio)) {
                    if (edicao && m.getId() == membroParaOperacao.getId()) {
                        // É o mesmo membro, não há conflito.
                    } else {
                        JOptionPane.showMessageDialog(this, "Já existe um membro com este Número de Sócio.", "Número de Sócio Duplicado", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
        }
        if (!numSocio.matches("\\d+")){
            JOptionPane.showMessageDialog(this, " O número de Sócios só aceita Números Inteiros!!", " Número inválido ", JOptionPane.ERROR_MESSAGE);
        
        }
         

        if (!edicao) {
            Membro novoMembro = new Membro(numSocio, primeiroNome, apelido, email);
            biblioteca.adicionarMembro(novoMembro);
            JOptionPane.showMessageDialog(this, "Membro adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            membroParaOperacao.setNumeroSocio(numSocio);
            membroParaOperacao.setPrimeiroNome(primeiroNome);
            membroParaOperacao.setApelido(apelido);
            membroParaOperacao.setEmail(email);
            JOptionPane.showMessageDialog(this, "Membro atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        dispose();
    }

    private void onCancelar() {
        dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextFieldEmail = new javax.swing.JTextField();
        jLabelApelido = new javax.swing.JLabel();
        jLabelEmail = new javax.swing.JLabel();
        jButtonAdd = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jTextFieldApelido = new javax.swing.JTextField();
        jTextFieldPrimeiroNome = new javax.swing.JTextField();
        jLabelNumSocio = new javax.swing.JLabel();
        jLabelPrimeiroNome = new javax.swing.JLabel();
        jTextFieldNumSocio = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabelApelido.setText("Apelido");

        jLabelEmail.setText("Email");

        jButtonAdd.setText("Adicionar");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jButtonCancelar.setBackground(new java.awt.Color(204, 0, 0));
        jButtonCancelar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButtonCancelar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jLabelNumSocio.setText("Número de Sócio");

        jLabelPrimeiroNome.setText("Primeiro Nome");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel5.setText("Detalhes do Membro");

        jSeparator1.setForeground(new java.awt.Color(153, 153, 153));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabelNumSocio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelPrimeiroNome, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabelApelido)
                            .addComponent(jLabelEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldNumSocio, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                            .addComponent(jTextFieldPrimeiroNome)
                            .addComponent(jTextFieldApelido)
                            .addComponent(jTextFieldEmail)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldNumSocio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNumSocio))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldPrimeiroNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPrimeiroNome))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldApelido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelApelido))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelEmail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancelar)
                    .addComponent(jButtonAdd))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        onConfirmar();
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        onCancelar();
    }//GEN-LAST:event_jButtonCancelarActionPerformed

   
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DetalhesMembro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Para testar, precisas de uma instância de Biblioteca
                Biblioteca bibliotecaTeste = new Biblioteca(); 
                
                // Testar o construtor de ADICIONAR
                DetalhesMembro dialogAdicionar = new DetalhesMembro(new javax.swing.JFrame(), true, bibliotecaTeste);
                dialogAdicionar.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        // System.exit(0); // Não fechar a aplicação toda ao testar um diálogo
                        e.getWindow().dispose();
                    }
                });
                

                //  para testar o construtor de EDITAR (precisarias de um Membro existente)
                Membro membroTesteParaEditar = new Membro("S001", "TestNome", "TestApelido", "test@edit.com");
                bibliotecaTeste.adicionarMembro(membroTesteParaEditar);
                DetalhesMembro dialogEditar = new DetalhesMembro(new javax.swing.JFrame(), true, bibliotecaTeste, membroTesteParaEditar);
                 dialogEditar.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                         e.getWindow().dispose();
                    }
                });
                dialogEditar.setVisible(true); // Descomentar para testar edição
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelApelido;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelNumSocio;
    private javax.swing.JLabel jLabelPrimeiroNome;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextFieldApelido;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldNumSocio;
    private javax.swing.JTextField jTextFieldPrimeiroNome;
    // End of variables declaration//GEN-END:variables
}
