package gui;

import com.mycompany.gestaobiblioteca.Biblioteca;
import com.mycompany.gestaobiblioteca.Emprestimo;
import com.mycompany.gestaobiblioteca.Livro;
import com.mycompany.gestaobiblioteca.Membro;
import com.mycompany.gestaobiblioteca.Exportar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class GestaoEmprestimos extends javax.swing.JFrame {

    private Biblioteca biblioteca;
    private DefaultTableModel tableModelHistorico;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public GestaoEmprestimos(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        initComponents();
        setTitle("Gestão de Empréstimos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        configurarTabelaHistorico();

        popularComboBoxMembrosParaNovoEmprestimo();
        popularComboBoxLivrosDisponiveisParaNovoEmprestimo();
        popularComboBoxMembrosComEmprestimosAtivos(); // Para a secção de devolução

        configurarFiltroHistorico();
        atualizarTabelaHistorico(this.biblioteca.getEmprestimos());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);

        if (jTextFieldDataEntrega != null) {
            jTextFieldDataEntrega.setText(sdf.format(cal.getTime()));
        }

    }

    public GestaoEmprestimos() {
        this(new Biblioteca());
        System.out.println("AVISO: GestaoEmprestimos chamado sem instância de Biblioteca. Usando uma nova.");
    }

    private void configurarTabelaHistorico() {
        tableModelHistorico = (DefaultTableModel) jTableHistorico.getModel();
    }

    private void configurarFiltroHistorico() {
        jComboBoxFiltroHistoricoCampo.setModel(new DefaultComboBoxModel<>(new String[]{"Todos", "ID Membro", "ID Livro", "Estado"}));
        if (jComboBoxFiltroHistoricoCampo.getItemCount() > 0) {
            jComboBoxFiltroHistoricoCampo.setSelectedIndex(0);
        }
    }

    private void popularComboBoxMembrosParaNovoEmprestimo() {
        jComboBoxMembro.removeAllItems();
        jComboBoxMembro.addItem("-- Selecione um Membro --");
        for (Membro membro : biblioteca.getMembros()) {
            jComboBoxMembro.addItem(membro.getId() + ": " + membro.getNomeCompleto());
        }
    }

    private void popularComboBoxLivrosDisponiveisParaNovoEmprestimo() {
        jComboBoxLivro.removeAllItems();
        jComboBoxLivro.addItem("-- Selecione um Livro Disponível --");
        for (Livro livro : biblioteca.getLivros()) {
            if (livro.isDisponivel()) {
                jComboBoxLivro.addItem(livro.getId() + ": " + livro.getTitulo());
            }
        }
    }

    private void popularComboBoxMembrosComEmprestimosAtivos() {
        jComboBoxMembroDevolucao.removeAllItems();
        jComboBoxMembroDevolucao.addItem("-- Selecione Membro --");
        ArrayList<Integer> idsMembrosComEmprestimos = new ArrayList<>();
        for (Emprestimo emp : biblioteca.getEmprestimos()) {
            if (emp.getDataDevolucaoEfetiva() == null && !idsMembrosComEmprestimos.contains(emp.getIdMembro())) {
                idsMembrosComEmprestimos.add(emp.getIdMembro());
                Membro m = biblioteca.procurarMembroPorId(emp.getIdMembro());
                if (m != null) {
                    jComboBoxMembroDevolucao.addItem(m.getId() + ": " + m.getNomeCompleto());
                }
            }
        }
        jComboBoxLivroDevolucao.removeAllItems();
        jComboBoxLivroDevolucao.addItem("-- Selecione Livro --");
        jComboBoxLivroDevolucao.setEnabled(false);
    }

    private void popularComboBoxLivrosParaDevolucaoPorMembro(int idMembro) {
        jComboBoxLivroDevolucao.removeAllItems();
        jComboBoxLivroDevolucao.addItem("-- Selecione Livro --");
        boolean encontrouLivros = false;
        for (Emprestimo emp : biblioteca.getEmprestimos()) {
            if (emp.getIdMembro() == idMembro && emp.getDataDevolucaoEfetiva() == null) {
                Livro l = biblioteca.procurarLivroPorId(emp.getIdLivro());
                if (l != null) {
                    jComboBoxLivroDevolucao.addItem(l.getId() + ": " + l.getTitulo() + " (Emp. ID: " + emp.getId() + ")");
                    encontrouLivros = true;
                }
            }
        }
        jComboBoxLivroDevolucao.setEnabled(encontrouLivros);
    }

    public void atualizarTabelaHistorico(ArrayList<Emprestimo> lista) {
        tableModelHistorico.setRowCount(0);
        if (lista == null) {
            return;
        }

        for (Emprestimo emp : lista) {
            Livro livro = biblioteca.procurarLivroPorId(emp.getIdLivro());
            Membro membro = biblioteca.procurarMembroPorId(emp.getIdMembro());
            tableModelHistorico.addRow(new Object[]{
                emp.getId(),
                (livro != null) ? livro.getTitulo() : "ID: " + emp.getIdLivro(),
                (membro != null) ? membro.getNomeCompleto() : "ID: " + emp.getIdMembro(),
                sdf.format(emp.getDataEmprestimo()),
                sdf.format(emp.getDataDevolucaoPrevista()),
                (emp.getDataDevolucaoEfetiva() != null) ? sdf.format(emp.getDataDevolucaoEfetiva()) : "-",
                emp.getEstado()
            });
        }
    }

    private void registarNovoEmprestimo() {
        if (jComboBoxMembro.getSelectedIndex() <= 0 || jComboBoxLivro.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um membro e um livro.", "Seleção Inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String membroSelecionadoStr = (String) jComboBoxMembro.getSelectedItem();
        String livroSelecionadoStr = (String) jComboBoxLivro.getSelectedItem();
        int idMembro = Integer.parseInt(membroSelecionadoStr.split(":")[0].trim());
        int idLivro = Integer.parseInt(livroSelecionadoStr.split(":")[0].trim());

        Date dataEmprestimo = new Date();
        Date dataDevolucaoPrevista;

        try {
            dataDevolucaoPrevista = sdf.parse(jTextFieldDataEntrega.getText());
            if (dataDevolucaoPrevista.before(dataEmprestimo) && !sdf.format(dataDevolucaoPrevista).equals(sdf.format(dataEmprestimo))) { // Permite mesmo dia
                JOptionPane.showMessageDialog(this, "A data de devolução prevista não pode ser anterior à data de empréstimo.", "Data Inválida", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data de devolução prevista inválido. Use dd-MM-yyyy.", "Data Inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean sucesso = biblioteca.registarEmprestimo(idLivro, idMembro, dataEmprestimo, dataDevolucaoPrevista);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Empréstimo registado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            atualizarTudo();
        } else {
            JOptionPane.showMessageDialog(this, "Falha ao registar empréstimo. Verifique se o livro está disponível ou se os dados são válidos.", "Falha no Registo", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registarDevolucao() {
        // Verifica se algo foi selecionado nos ComboBoxes de devolução
        if (jComboBoxMembroDevolucao.getSelectedIndex() <= 0
                || jComboBoxLivroDevolucao.getSelectedIndex() <= 0
                || ((String) jComboBoxLivroDevolucao.getSelectedItem()).startsWith("--")) { // Verifica o item placeholder
            JOptionPane.showMessageDialog(this, "Por favor, selecione um membro e o livro correspondente para devolução.", "Seleção Inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String livroSelecionadoParaDevolucaoStr = (String) jComboBoxLivroDevolucao.getSelectedItem();
        int idEmprestimo = -1;
        try {
            // Extrai o ID do empréstimo do texto do ComboBox
            // Exemplo: "1: O Hobbit (Emp. ID: 3)" -> extrai 3
            String parteIdEmp = livroSelecionadoParaDevolucaoStr.substring(livroSelecionadoParaDevolucaoStr.indexOf("(Emp. ID: ") + "(Emp. ID: ".length());
            idEmprestimo = Integer.parseInt(parteIdEmp.replace(")", "").trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao obter ID do empréstimo para devolução a partir de: '" + livroSelecionadoParaDevolucaoStr + "'", "Erro Interno", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        // Se, por alguma razão, o ID não foi extraído corretamente (embora o try-catch deva pegar)
        if (idEmprestimo == -1) {
            JOptionPane.showMessageDialog(this, "Não foi possível identificar o empréstimo para o livro selecionado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date dataDevolucaoEfetiva = new Date(); // Data atual para a devolução

        // Chama o método do BACKEND (Biblioteca.java)
        boolean sucesso = biblioteca.registarDevolucao(idEmprestimo, dataDevolucaoEfetiva);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Devolução registada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            atualizarTudo(); // Atualiza a interface
        } else {
            JOptionPane.showMessageDialog(this, "Falha ao registar devolução. Verifique se o empréstimo é válido ou se já foi devolvido.", "Falha na Devolução", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procurarNoHistorico() {
        String termoFiltro = jTextFieldFiltroHistoricoValor.getText().toLowerCase().trim();
        String campoSelecionado = (String) jComboBoxFiltroHistoricoCampo.getSelectedItem();
        ArrayList<Emprestimo> todosEmprestimos = biblioteca.getEmprestimos();

        if (campoSelecionado == null || (campoSelecionado.equals("Todos") && termoFiltro.isEmpty())) {
            atualizarTabelaHistorico(todosEmprestimos);
            return;
        }

        ArrayList<Emprestimo> emprestimosFiltrados = new ArrayList<>();
        for (Emprestimo emp : todosEmprestimos) {
            boolean match = false;
            if (campoSelecionado.equals("Todos") && !termoFiltro.isEmpty()) {
                Livro l = biblioteca.procurarLivroPorId(emp.getIdLivro());
                Membro m = biblioteca.procurarMembroPorId(emp.getIdMembro());
                String livroTitulo = (l != null) ? l.getTitulo().toLowerCase() : "";
                String membroNome = (m != null) ? m.getNomeCompleto().toLowerCase() : "";
                if (String.valueOf(emp.getId()).contains(termoFiltro)
                        || livroTitulo.contains(termoFiltro)
                        || membroNome.contains(termoFiltro)
                        || emp.getEstado().toLowerCase().contains(termoFiltro)
                        || sdf.format(emp.getDataEmprestimo()).contains(termoFiltro)
                        || sdf.format(emp.getDataDevolucaoPrevista()).contains(termoFiltro)
                        || (emp.getDataDevolucaoEfetiva() != null && sdf.format(emp.getDataDevolucaoEfetiva()).contains(termoFiltro))) {
                    match = true;
                }
            } else {
                switch (campoSelecionado) {
                    case "ID Membro":
                        if (String.valueOf(emp.getIdMembro()).contains(termoFiltro)) {
                            match = true;
                        }
                        break;
                    case "ID Livro":
                        if (String.valueOf(emp.getIdLivro()).contains(termoFiltro)) {
                            match = true;
                        }
                        break;
                    case "Estado":
                        if (emp.getEstado().toLowerCase().contains(termoFiltro)) {
                            match = true;
                        }
                        break;
                }
            }
            if (match) {
                emprestimosFiltrados.add(emp);
            }
        }
        atualizarTabelaHistorico(emprestimosFiltrados);
    }

    private void exportarHistorico() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Histórico de Empréstimos como CSV");
        fileChooser.setSelectedFile(new java.io.File("historico_emprestimos.csv"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".csv")) {
                fileToSave = new java.io.File(filePath + ".csv");
            }
            Exportar.exportarEmprestimos(fileToSave.getAbsolutePath(), biblioteca.getEmprestimos());
        }
    }

    private void atualizarTudo() {
        popularComboBoxMembrosParaNovoEmprestimo();
        popularComboBoxLivrosDisponiveisParaNovoEmprestimo();
        popularComboBoxMembrosComEmprestimosAtivos();
        jComboBoxLivroDevolucao.removeAllItems();
        jComboBoxLivroDevolucao.addItem("-- Selecione Livro --");
        jComboBoxLivroDevolucao.setEnabled(false);
        atualizarTabelaHistorico(biblioteca.getEmprestimos());
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelNovoEmprest = new javax.swing.JLabel();
        jLabelMembro = new javax.swing.JLabel();
        jLabelLivro = new javax.swing.JLabel();
        jLabelDataEntrega = new javax.swing.JLabel();
        jComboBoxMembro = new javax.swing.JComboBox<>();
        jComboBoxLivro = new javax.swing.JComboBox<>();
        jTextFieldDataEntrega = new javax.swing.JTextField();
        jButtonRegistarNovoEmprestimo = new javax.swing.JButton();
        jLabelHistoricoEmprestimos = new javax.swing.JLabel();
        jTextFieldFiltroHistoricoValor = new javax.swing.JTextField();
        jComboBoxFiltroHistoricoCampo = new javax.swing.JComboBox<>();
        jButtonProcurarHistorico = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableHistorico = new javax.swing.JTable();
        jButtonExportarFicheiro = new javax.swing.JButton();
        jLabelDevolucao = new javax.swing.JLabel();
        jLabelMembro1 = new javax.swing.JLabel();
        jLabelLivro1 = new javax.swing.JLabel();
        jComboBoxMembroDevolucao = new javax.swing.JComboBox<>();
        jComboBoxLivroDevolucao = new javax.swing.JComboBox<>();
        jButtonRegistarDevolucao = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabelNovoEmprest.setText("Novo Empréstimo");

        jLabelMembro.setText("Membro");

        jLabelLivro.setText("Livro");

        jLabelDataEntrega.setText("Data Entrega");

        jComboBoxMembro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBoxLivro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextFieldDataEntrega.setText("(DD-MM-AAAA)");
        jTextFieldDataEntrega.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDataEntregaActionPerformed(evt);
            }
        });

        jButtonRegistarNovoEmprestimo.setText("Validar");
        jButtonRegistarNovoEmprestimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegistarNovoEmprestimoActionPerformed(evt);
            }
        });

        jLabelHistoricoEmprestimos.setText("Histórico de Empréstimos");

        jTextFieldFiltroHistoricoValor.setText("Filtro");

        jComboBoxFiltroHistoricoCampo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButtonProcurarHistorico.setText("Procurar");
        jButtonProcurarHistorico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonProcurarHistoricoActionPerformed(evt);
            }
        });

        jTableHistorico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Livro", "Membro", "Estado", "Empréstimo", "Retorno"
            }
        ));
        jScrollPane1.setViewportView(jTableHistorico);

        jButtonExportarFicheiro.setText("Exportar para Ficheiro");
        jButtonExportarFicheiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportarFicheiroActionPerformed(evt);
            }
        });

        jLabelDevolucao.setText("Devolução de Empréstimos");

        jLabelMembro1.setText("Membro");

        jLabelLivro1.setText("Livro");

        jComboBoxMembroDevolucao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxMembroDevolucao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxMembroDevolucaoActionPerformed(evt);
            }
        });

        jComboBoxLivroDevolucao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButtonRegistarDevolucao.setText("Validar");
        jButtonRegistarDevolucao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegistarDevolucaoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelHistoricoEmprestimos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelMembro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxMembro, 0, 126, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBoxLivro, 0, 138, Short.MAX_VALUE)
                            .addComponent(jLabelLivro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelDataEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextFieldDataEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonRegistarNovoEmprestimo)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelNovoEmprest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextFieldFiltroHistoricoValor)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBoxFiltroHistoricoCampo, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonProcurarHistorico))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelDevolucao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButtonExportarFicheiro, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxMembroDevolucao, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelMembro1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelLivro1, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                                .addGap(278, 278, 278))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jComboBoxLivroDevolucao, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonRegistarDevolucao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(12, 12, 12))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelNovoEmprest, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelMembro)
                    .addComponent(jLabelLivro)
                    .addComponent(jLabelDataEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxMembro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxLivro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDataEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonRegistarNovoEmprestimo))
                .addGap(18, 18, 18)
                .addComponent(jLabelHistoricoEmprestimos, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldFiltroHistoricoValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxFiltroHistoricoCampo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonProcurarHistorico))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonExportarFicheiro)
                .addGap(12, 12, 12)
                .addComponent(jLabelDevolucao, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelMembro1)
                    .addComponent(jLabelLivro1))
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxMembroDevolucao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxLivroDevolucao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonRegistarDevolucao))
                .addContainerGap(55, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonRegistarNovoEmprestimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegistarNovoEmprestimoActionPerformed
        registarNovoEmprestimo();
    }//GEN-LAST:event_jButtonRegistarNovoEmprestimoActionPerformed

    private void jButtonRegistarDevolucaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegistarDevolucaoActionPerformed
        registarDevolucao();
    }//GEN-LAST:event_jButtonRegistarDevolucaoActionPerformed

    private void jButtonExportarFicheiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportarFicheiroActionPerformed
        exportarHistorico();
    }//GEN-LAST:event_jButtonExportarFicheiroActionPerformed

    private void jButtonProcurarHistoricoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonProcurarHistoricoActionPerformed
        procurarNoHistorico();
    }//GEN-LAST:event_jButtonProcurarHistoricoActionPerformed

    private void jComboBoxMembroDevolucaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxMembroDevolucaoActionPerformed
        if (jComboBoxMembroDevolucao.getSelectedIndex() > 0) {
            String membroSelecionadoStr = (String) jComboBoxMembroDevolucao.getSelectedItem();
            if (membroSelecionadoStr != null && !membroSelecionadoStr.startsWith("--")) {
                // Adicionar verificação para evitar NumberFormatException se o split falhar
                try {
                    int idMembro = Integer.parseInt(membroSelecionadoStr.split(":")[0].trim());
                    popularComboBoxLivrosParaDevolucaoPorMembro(idMembro);
                } catch (NumberFormatException e) {
                    System.err.println("Erro ao parsear ID do membro no ComboBox de devolução: " + membroSelecionadoStr);
                    // Limpar ComboBox de livros ou tratar o erro
                    jComboBoxLivroDevolucao.removeAllItems();
                    jComboBoxLivroDevolucao.addItem("-- Erro ao carregar livros --");
                    jComboBoxLivroDevolucao.setEnabled(false);
                }
            }
        } else {
            jComboBoxLivroDevolucao.removeAllItems();
            jComboBoxLivroDevolucao.addItem("-- Selecione Livro --");
            jComboBoxLivroDevolucao.setEnabled(false);
        }
    }//GEN-LAST:event_jComboBoxMembroDevolucaoActionPerformed

    private void jTextFieldDataEntregaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDataEntregaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldDataEntregaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonExportarFicheiro;
    private javax.swing.JButton jButtonProcurarHistorico;
    private javax.swing.JButton jButtonRegistarDevolucao;
    private javax.swing.JButton jButtonRegistarNovoEmprestimo;
    private javax.swing.JComboBox<String> jComboBoxFiltroHistoricoCampo;
    private javax.swing.JComboBox<String> jComboBoxLivro;
    private javax.swing.JComboBox<String> jComboBoxLivroDevolucao;
    private javax.swing.JComboBox<String> jComboBoxMembro;
    private javax.swing.JComboBox<String> jComboBoxMembroDevolucao;
    private javax.swing.JLabel jLabelDataEntrega;
    private javax.swing.JLabel jLabelDevolucao;
    private javax.swing.JLabel jLabelHistoricoEmprestimos;
    private javax.swing.JLabel jLabelLivro;
    private javax.swing.JLabel jLabelLivro1;
    private javax.swing.JLabel jLabelMembro;
    private javax.swing.JLabel jLabelMembro1;
    private javax.swing.JLabel jLabelNovoEmprest;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableHistorico;
    private javax.swing.JTextField jTextFieldDataEntrega;
    private javax.swing.JTextField jTextFieldFiltroHistoricoValor;
    // End of variables declaration//GEN-END:variables
}
