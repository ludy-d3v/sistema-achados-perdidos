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
 * @author ludmilla
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
        }

        jLabel6.setText(SessaoUsuario.getIdentificacao());
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

        jTabresult.setSelectionBackground(new Color(197, 216, 248));
        jTabresult.setSelectionForeground(Color.BLACK);

        jTabresult.getSelectionModel().addListSelectionListener(evt -> preencherCamposComSelecao());

        buscar();
    }

    private void buscar() {
        try {
            FuncionarioController controller = new FuncionarioController();
            List<UsuarioResumo> usuarios = controller.listarUsuarios(jTpesquisa.getText().trim());

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

            jTabresult.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar usuários: " + e.getMessage());
        }
    }

    private void preencherCamposComSelecao() {
        int linhaView = jTabresult.getSelectedRow();
        if (linhaView == -1) {
            return;
        }
        int linha = jTabresult.convertRowIndexToModel(linhaView);

        Object cargo = jTabresult.getModel().getValueAt(linha, 4);
        Object setor = jTabresult.getModel().getValueAt(linha, 5);

        jTcargo.setText(cargo == null ? "" : cargo.toString());
        jTsetor.setText(setor == null ? "" : setor.toString());
    }

    private int obterIdSelecionado() {
        int linhaView = jTabresult.getSelectedRow();
        if (linhaView == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela.");
            return -1;
        }
        int linha = jTabresult.convertRowIndexToModel(linhaView);
        return (Integer) jTabresult.getModel().getValueAt(linha, 0);
    }

    private void promoverSelecionado() {
        int id = obterIdSelecionado();
        if (id == -1) {
            return;
        }

        try {
            FuncionarioController controller = new FuncionarioController();
            if (controller.promover(id, jTcargo.getText(), jTsetor.getText())) {
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
            if (controller.atualizarCargoSetor(id, jTcargo.getText(), jTsetor.getText())) {
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
                jTcargo.setText("");
                jTsetor.setText("");
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

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanelTopo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanelMenu = new javax.swing.JPanel();
        jBsairTelaPrin = new javax.swing.JButton();
        jPtotal = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jLpesquisa = new javax.swing.JLabel();
        jTpesquisa = new javax.swing.JTextField();
        jBpesq = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabresult = new javax.swing.JTable();
        jLcargo = new javax.swing.JLabel();
        jTcargo = new javax.swing.JTextField();
        jLsetor = new javax.swing.JLabel();
        jTsetor = new javax.swing.JTextField();
        jBpromover = new javax.swing.JButton();
        jBsalvarCargoSetor = new javax.swing.JButton();
        jBremover = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jPanelTopo.setBackground(new java.awt.Color(10, 61, 163));
        jPanelTopo.setLayout(null);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/img/Emblema Branco sobre Azul Real.png")));
        jPanelTopo.add(jLabel1);
        jLabel1.setBounds(10, 0, 206, 90);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Achados e Perdidos - Gerenciar Funcionários");
        jPanelTopo.add(jLabel4);
        jLabel4.setBounds(220, 30, 500, 27);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Nome Usuário");
        jPanelTopo.add(jLabel6);
        jLabel6.setBounds(1030, 30, 320, 27);

        getContentPane().add(jPanelTopo);
        jPanelTopo.setBounds(0, 0, 1780, 90);

        jPanelMenu.setBackground(new java.awt.Color(16, 66, 165));
        jPanelMenu.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanelMenu.setLayout(null);

        jBsairTelaPrin.setBackground(new java.awt.Color(10, 61, 163));
        jBsairTelaPrin.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jBsairTelaPrin.setForeground(new java.awt.Color(255, 255, 255));
        jBsairTelaPrin.setText("SAIR");
        jBsairTelaPrin.setContentAreaFilled(false);
        jBsairTelaPrin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBsairTelaPrin.addActionListener(this::jBsairTelaPrinActionPerformed);
        jPanelMenu.add(jBsairTelaPrin);
        jBsairTelaPrin.setBounds(10, 227, 160, 40);

        getContentPane().add(jPanelMenu);
        jPanelMenu.setBounds(0, 0, 180, 1060);

        jPtotal.setBackground(new java.awt.Color(243, 246, 249));
        jPtotal.setLayout(null);

        jLabelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24));
        jLabelTitulo.setForeground(new java.awt.Color(10, 61, 163));
        jLabelTitulo.setText("Gerenciar funcionários");
        jPtotal.add(jLabelTitulo);
        jLabelTitulo.setBounds(40, 20, 420, 36);

        jLpesquisa.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLpesquisa.setForeground(new java.awt.Color(4, 120, 203));
        jLpesquisa.setText("Pesquisar por nome, e-mail ou CPF:");
        jPtotal.add(jLpesquisa);
        jLpesquisa.setBounds(40, 80, 300, 20);

        jTpesquisa.addActionListener(this::jTpesquisaActionPerformed);
        jPtotal.add(jTpesquisa);
        jTpesquisa.setBounds(40, 105, 500, 28);

        jBpesq.setBackground(new java.awt.Color(4, 120, 203));
        jBpesq.setForeground(new java.awt.Color(255, 255, 255));
        jBpesq.setText("Pesquisar");
        jBpesq.addActionListener(this::jBpesqActionPerformed);
        jPtotal.add(jBpesq);
        jBpesq.setBounds(560, 105, 110, 28);

        jTabresult.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Nome", "CPF", "Email institucional", "Cargo", "Setor", "Funcionário?"}
        ));
        jScrollPane1.setViewportView(jTabresult);
        jPtotal.add(jScrollPane1);
        jScrollPane1.setBounds(40, 150, 1150, 420);

        jLcargo.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLcargo.setForeground(new java.awt.Color(4, 120, 203));
        jLcargo.setText("Cargo:");
        jPtotal.add(jLcargo);
        jLcargo.setBounds(40, 590, 120, 20);

        jTcargo.setBounds(40, 615, 300, 28);
        jPtotal.add(jTcargo);
        jTcargo.setBounds(40, 615, 300, 28);

        jLsetor.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLsetor.setForeground(new java.awt.Color(4, 120, 203));
        jLsetor.setText("Setor:");
        jPtotal.add(jLsetor);
        jLsetor.setBounds(380, 590, 120, 20);

        jTsetor.setBounds(380, 615, 300, 28);
        jPtotal.add(jTsetor);
        jTsetor.setBounds(380, 615, 300, 28);

        jBpromover.setBackground(new java.awt.Color(4, 120, 203));
        jBpromover.setForeground(new java.awt.Color(255, 255, 255));
        jBpromover.setText("Tornar funcionário");
        jBpromover.addActionListener(this::jBpromoverActionPerformed);
        jPtotal.add(jBpromover);
        jBpromover.setBounds(40, 670, 180, 40);

        jBsalvarCargoSetor.setBackground(new java.awt.Color(4, 120, 203));
        jBsalvarCargoSetor.setForeground(new java.awt.Color(255, 255, 255));
        jBsalvarCargoSetor.setText("Salvar cargo/setor");
        jBsalvarCargoSetor.addActionListener(this::jBsalvarCargoSetorActionPerformed);
        jPtotal.add(jBsalvarCargoSetor);
        jBsalvarCargoSetor.setBounds(240, 670, 180, 40);

        jBremover.setBackground(new java.awt.Color(204, 0, 0));
        jBremover.setForeground(new java.awt.Color(255, 255, 255));
        jBremover.setText("Remover cargo");
        jBremover.addActionListener(this::jBremoverActionPerformed);
        jPtotal.add(jBremover);
        jBremover.setBounds(440, 670, 180, 40);

        getContentPane().add(jPtotal);
        jPtotal.setBounds(180, 90, 1600, 980);

        pack();
    }

    private void jBsairTelaPrinActionPerformed(java.awt.event.ActionEvent evt) {
        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente sair do sistema?",
                "Confirmar saída",
                JOptionPane.YES_NO_OPTION
        );

        if (resposta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void jTpesquisaActionPerformed(java.awt.event.ActionEvent evt) {
        buscar();
    }

    private void jBpesqActionPerformed(java.awt.event.ActionEvent evt) {
        buscar();
    }

    private void jBpromoverActionPerformed(java.awt.event.ActionEvent evt) {
        promoverSelecionado();
    }

    private void jBsalvarCargoSetorActionPerformed(java.awt.event.ActionEvent evt) {
        salvarCargoSetorSelecionado();
    }

    private void jBremoverActionPerformed(java.awt.event.ActionEvent evt) {
        removerSelecionado();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new TelaGerenciarFuncionarios().setVisible(true));
    }

    private javax.swing.JButton jBpesq;
    private javax.swing.JButton jBpromover;
    private javax.swing.JButton jBremover;
    private javax.swing.JButton jBsairTelaPrin;
    private javax.swing.JButton jBsalvarCargoSetor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLcargo;
    private javax.swing.JLabel jLpesquisa;
    private javax.swing.JLabel jLsetor;
    private javax.swing.JPanel jPanelMenu;
    private javax.swing.JPanel jPanelTopo;
    private javax.swing.JPanel jPtotal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabresult;
    private javax.swing.JTextField jTcargo;
    private javax.swing.JTextField jTpesquisa;
    private javax.swing.JTextField jTsetor;
}
