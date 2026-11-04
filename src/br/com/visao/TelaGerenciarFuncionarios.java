/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.visao;

import br.com.controller.FuncionarioController;
import br.com.controller.SessaoUsuario;
import br.com.controller.UsuarioResumo;
import java.awt.Color;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author migue
 */
public class TelaGerenciarFuncionarios extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TelaGerenciarFuncionarios.class.getName());

    public TelaGerenciarFuncionarios() {
        initComponents();

        if (!SessaoUsuario.isAdministrador()) {
            JOptionPane.showMessageDialog(this, "Acesso restrito a administradores.");
            javax.swing.SwingUtilities.invokeLater(() -> {
                TelaPrincipal tela = new TelaPrincipal();
                tela.setVisible(true);
                this.dispose();
            });
            return;
        }

        jLabel6.setText(SessaoUsuario.getIdentificacao());
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

        jTabresultadousuarios.setSelectionBackground(new Color(197, 216, 248));
        jTabresultadousuarios.setSelectionForeground(Color.BLACK);

        jTabresultadousuarios.getSelectionModel().addListSelectionListener(evt -> preencherCamposComSelecao());

        buscar();

        jTnome_email_cpf_usuario.addActionListener(evt -> buscar());
    }

    private void buscar() {
        try {
            FuncionarioController controller = new FuncionarioController();
            List<UsuarioResumo> usuarios = controller.listarUsuarios(jTnome_email_cpf_usuario.getText().trim());

            DefaultTableModel modelo = new DefaultTableModel(
                    new String[]{"ID", "Nome", "CPF", "Email institucional", "Cargo", "Setor", "Funcionário?"}, 0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (UsuarioResumo usuario : usuarios) {
                modelo.addRow(new Object[]{
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getCpf(),
                    usuario.getEmail(),
                    usuario.getCargo(),
                    usuario.getSetor(),
                    usuario.isFuncionario() ? "Sim" : "Não"
                });
            }

            jTabresultadousuarios.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar usuários: " + e.getMessage());
        }
    }

    private void preencherCamposComSelecao() {
        int linhaView = jTabresultadousuarios.getSelectedRow();
        if (linhaView == -1) {
            return;
        }
        int linha = jTabresultadousuarios.convertRowIndexToModel(linhaView);

        Object cargo = jTabresultadousuarios.getModel().getValueAt(linha, 4);
        Object setor = jTabresultadousuarios.getModel().getValueAt(linha, 5);

        jTnome_cargo.setText(cargo == null ? "" : cargo.toString());
        jTnome_setor.setText(setor == null ? "" : setor.toString());
    }

    private int obterIdSelecionado() {
        int linhaView = jTabresultadousuarios.getSelectedRow();
        if (linhaView == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela.");
            return -1;
        }
        int linha = jTabresultadousuarios.convertRowIndexToModel(linhaView);
        return (Integer) jTabresultadousuarios.getModel().getValueAt(linha, 0);
    }

    private void promoverSelecionado() {
        int id = obterIdSelecionado();
        if (id == -1) {
            return;
        }

        try {
            FuncionarioController controller = new FuncionarioController();
            if (controller.promover(id, jTnome_cargo.getText(), jTnome_setor.getText())) {
                JOptionPane.showMessageDialog(this, "Usuário promovido a funcionário com sucesso!");
                buscar();
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível promover o usuário.");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, "Esse usuário já é funcionário.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao promover usuário: " + e.getMessage());
        }
    }

    private void salvarCargoSetorSelecionado() {
        int id = obterIdSelecionado();
        if (id == -1) {
            return;
        }

        try {
            FuncionarioController controller = new FuncionarioController();
            if (controller.atualizarCargoSetor(id, jTnome_cargo.getText(), jTnome_setor.getText())) {
                JOptionPane.showMessageDialog(this, "Cargo/setor atualizado com sucesso!");
                buscar();
            } else {
                JOptionPane.showMessageDialog(this, "Esse usuário ainda não é funcionário.");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar cargo/setor: " + e.getMessage());
        }
    }

    private void removerSelecionado() {
        int id = obterIdSelecionado();
        if (id == -1) {
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente remover o cargo de funcionário deste usuário?",
                "Confirmar remoção",
                JOptionPane.YES_NO_OPTION
        );

        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            FuncionarioController controller = new FuncionarioController();
            if (controller.removerFuncionario(id)) {
                JOptionPane.showMessageDialog(this, "Cargo de funcionário removido com sucesso!");
                jTnome_cargo.setText("");
                jTnome_setor.setText("");
                buscar();
            } else {
                JOptionPane.showMessageDialog(this, "Esse usuário não é funcionário.");
            }
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this,
                    "Não é possível remover: esse funcionário já tem devoluções registradas no sistema.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao remover funcionário: " + e.getMessage());
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
        jPtotal = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jBpesquisar_usuario = new javax.swing.JButton();
        jTnome_email_cpf_usuario = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTabresultadousuarios = new javax.swing.JTable();
        jBremovercargo = new javax.swing.JButton();
        jBtornarfuncionario = new javax.swing.JButton();
        jBsalvar_cargo_setor = new javax.swing.JButton();
        jTnome_setor = new javax.swing.JTextField();
        jTnome_cargo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        javax.swing.JPanel jPanel3 = new br.com.controller.PainelFundoAzul();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jBinicio = new javax.swing.JButton();
        jBsairTelaPrin = new javax.swing.JButton();
        jBgerenciarfuncionarios = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setViewportView(jPtotal);

        jPtotal.setBackground(new java.awt.Color(243, 246, 249));
        jPtotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPtotal.setPreferredSize(new java.awt.Dimension(900, 1000));
        jPtotal.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(243, 246, 249));
        jPanel2.setLayout(null);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(10, 61, 163));
        jLabel15.setText("Gerenciar funcionários");
        jPanel2.add(jLabel15);
        jLabel15.setBounds(160, 10, 390, 30);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(10, 61, 163));
        jLabel13.setText("Atribuição e retirada de cargos");
        jPanel2.add(jLabel13);
        jLabel13.setBounds(160, 60, 410, 30);

        jLabel14.setBackground(new java.awt.Color(153, 204, 255));
        jLabel14.setOpaque(true);
        jPanel2.add(jLabel14);
        jLabel14.setBounds(120, 10, 3, 85);

        jLabel3.setForeground(new java.awt.Color(4, 120, 203));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICON/Background (4).png"))); // NOI18N
        jLabel3.setToolTipText("");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(-10, 0, 1220, 110);

        jPtotal.add(jPanel2);
        jPanel2.setBounds(60, 80, 1220, 120);

        jPanel5.setBackground(new java.awt.Color(243, 247, 254));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(197, 216, 248), 2));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(4, 90, 203));
        jLabel9.setText("Pesquisar por nome, e-mail ou CPF:");

        jBpesquisar_usuario.setBackground(new java.awt.Color(4, 120, 203));
        jBpesquisar_usuario.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jBpesquisar_usuario.setForeground(new java.awt.Color(255, 255, 255));
        jBpesquisar_usuario.setText("Pesquisar");
        jBpesquisar_usuario.addActionListener(this::jBpesquisar_usuarioActionPerformed);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jTnome_email_cpf_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addComponent(jBpesquisar_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(418, 418, 418))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBpesquisar_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTnome_email_cpf_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );

        jPtotal.add(jPanel5);
        jPanel5.setBounds(70, 220, 1190, 110);

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTabresultadousuarios.setAutoCreateRowSorter(true);
        jTabresultadousuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTabresultadousuarios.setToolTipText("");
        jTabresultadousuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR));
        jTabresultadousuarios.setGridColor(new java.awt.Color(243, 247, 254));
        jTabresultadousuarios.setInheritsPopupMenu(true);
        jTabresultadousuarios.setSelectionBackground(new java.awt.Color(243, 247, 254));
        jScrollPane3.setViewportView(jTabresultadousuarios);

        jPtotal.add(jScrollPane3);
        jScrollPane3.setBounds(70, 350, 1190, 470);

        jBremovercargo.setBackground(new java.awt.Color(204, 0, 0));
        jBremovercargo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jBremovercargo.setForeground(new java.awt.Color(255, 255, 255));
        jBremovercargo.setText("Remover cargo");
        jBremovercargo.addActionListener(this::jBremovercargoActionPerformed);
        jPtotal.add(jBremovercargo);
        jBremovercargo.setBounds(1150, 860, 150, 40);

        jBtornarfuncionario.setBackground(new java.awt.Color(4, 120, 203));
        jBtornarfuncionario.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jBtornarfuncionario.setForeground(new java.awt.Color(255, 255, 255));
        jBtornarfuncionario.setText("Tornar funcionário");
        jBtornarfuncionario.addActionListener(this::jBtornarfuncionarioActionPerformed);
        jPtotal.add(jBtornarfuncionario);
        jBtornarfuncionario.setBounds(730, 860, 160, 40);

        jBsalvar_cargo_setor.setBackground(new java.awt.Color(4, 120, 203));
        jBsalvar_cargo_setor.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jBsalvar_cargo_setor.setForeground(new java.awt.Color(255, 255, 255));
        jBsalvar_cargo_setor.setText("Salvar cargo/setor");
        jBsalvar_cargo_setor.addActionListener(this::jBsalvar_cargo_setorActionPerformed);
        jPtotal.add(jBsalvar_cargo_setor);
        jBsalvar_cargo_setor.setBounds(940, 860, 160, 40);
        jPtotal.add(jTnome_setor);
        jTnome_setor.setBounds(400, 870, 300, 26);
        jPtotal.add(jTnome_cargo);
        jTnome_cargo.setBounds(70, 870, 280, 26);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(4, 120, 203));
        jLabel2.setText("Setor:");
        jPtotal.add(jLabel2);
        jLabel2.setBounds(400, 830, 115, 20);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(4, 120, 203));
        jLabel5.setText("Cargo:");
        jPtotal.add(jLabel5);
        jLabel5.setBounds(70, 830, 115, 20);

        jScrollPane1.setViewportView(jPtotal);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(180, 90, 1360, 1120);

        jPanel3.setBackground(new java.awt.Color(10, 61, 163));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICON/Emblema Branco sobre Azul Real (1).png"))); // NOI18N
        jLabel1.setToolTipText("");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Achados e Perdidos - Administração");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Nome Usuário");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 857, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(283, 283, 283))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
        );

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 0, 1780, 90);

        jPanel6.setBackground(new java.awt.Color(16, 66, 165));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jBinicio.setBackground(new java.awt.Color(10, 61, 163));
        jBinicio.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jBinicio.setForeground(new java.awt.Color(255, 255, 255));
        jBinicio.setText("Início");
        jBinicio.setBorder(null);
        jBinicio.setBorderPainted(false);
        jBinicio.setContentAreaFilled(false);
        jBinicio.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBinicio.addActionListener(this::jBinicioActionPerformed);

        jBsairTelaPrin.setBackground(new java.awt.Color(10, 61, 163));
        jBsairTelaPrin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jBsairTelaPrin.setForeground(new java.awt.Color(255, 255, 255));
        jBsairTelaPrin.setText("SAIR");
        jBsairTelaPrin.setContentAreaFilled(false);
        jBsairTelaPrin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBsairTelaPrin.addActionListener(this::jBsairTelaPrinActionPerformed);

        jBgerenciarfuncionarios.setBackground(new java.awt.Color(0, 51, 204));
        jBgerenciarfuncionarios.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jBgerenciarfuncionarios.setForeground(new java.awt.Color(255, 255, 255));
        jBgerenciarfuncionarios.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jBgerenciarfuncionarios.setText("Gerenciar Funcionários");
        jBgerenciarfuncionarios.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 204), 5));
        jBgerenciarfuncionarios.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBgerenciarfuncionarios.setOpaque(true);
        jBgerenciarfuncionarios.setPreferredSize(new java.awt.Dimension(113, 18));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBinicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBgerenciarfuncionarios, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addComponent(jBsairTelaPrin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(248, 248, 248)
                .addComponent(jBinicio, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jBgerenciarfuncionarios, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(229, 229, 229)
                .addComponent(jBsairTelaPrin, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(591, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel6);
        jPanel6.setBounds(0, 0, 180, 1210);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBinicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBinicioActionPerformed
        TelaPrincipal tela = new TelaPrincipal();
        tela.setLocationRelativeTo(this);
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jBinicioActionPerformed

    private void jBsairTelaPrinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBsairTelaPrinActionPerformed
        int resposta = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente sair do sistema?",
            "Confirmar saída",
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (resposta == javax.swing.JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_jBsairTelaPrinActionPerformed

    private void jBsalvar_cargo_setorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBsalvar_cargo_setorActionPerformed
        salvarCargoSetorSelecionado();
    }//GEN-LAST:event_jBsalvar_cargo_setorActionPerformed

    private void jBtornarfuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtornarfuncionarioActionPerformed
        promoverSelecionado();
    }//GEN-LAST:event_jBtornarfuncionarioActionPerformed

    private void jBremovercargoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBremovercargoActionPerformed
        removerSelecionado();
    }//GEN-LAST:event_jBremovercargoActionPerformed

    private void jBpesquisar_usuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBpesquisar_usuarioActionPerformed
        buscar();
    }//GEN-LAST:event_jBpesquisar_usuarioActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new TelaGerenciarFuncionarios().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jBgerenciarfuncionarios;
    private javax.swing.JButton jBinicio;
    private javax.swing.JButton jBpesquisar_usuario;
    private javax.swing.JButton jBremovercargo;
    private javax.swing.JButton jBsairTelaPrin;
    private javax.swing.JButton jBsalvar_cargo_setor;
    private javax.swing.JButton jBtornarfuncionario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPtotal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTabresultadousuarios;
    private javax.swing.JTextField jTnome_cargo;
    private javax.swing.JTextField jTnome_email_cpf_usuario;
    private javax.swing.JTextField jTnome_setor;
    // End of variables declaration//GEN-END:variables
}
