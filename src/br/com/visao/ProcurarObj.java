/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.visao;

import br.com.controller.CampoDataUtil;
import br.com.controller.ObjetoController;
import br.com.controller.ObjetoResumo;
import br.com.controller.ReivindicacaoController;
import br.com.controller.SessaoUsuario;
import java.awt.Color;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author migue
 */
public class ProcurarObj extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ProcurarObj.class.getName());

    /**
     * Creates new form ConsultarObjeto
     */
    public ProcurarObj() {
        initComponents();
        jLabel6.setText(SessaoUsuario.getIdentificacao());
        CampoDataUtil.aplicarFormatoAuxiliar(jFdepois_de);
        CampoDataUtil.aplicarFormatoAuxiliar(jFantes_de);

        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

        jTabresult.setSelectionBackground(new Color(197, 216, 248));
        jTabresult.setSelectionForeground(Color.BLACK);

        carregarCategorias();
        buscar();

        jTnome_obj.addActionListener(evt -> buscar());
    }

    private void reivindicarSelecionado() {
        int linhaView = jTabresult.getSelectedRow();
        if (linhaView == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um objeto na tabela.");
            return;
        }
        int linha = jTabresult.convertRowIndexToModel(linhaView);
        int idObjeto = (Integer) jTabresult.getModel().getValueAt(linha, 0);

        String telefone = JOptionPane.showInputDialog(this, "Telefone para contato (opcional):");

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Confirma a reivindicação deste objeto?",
                "Confirmar reivindicação",
                JOptionPane.YES_NO_OPTION
        );

        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            ReivindicacaoController controller = new ReivindicacaoController();
            controller.setNomeRetirante(SessaoUsuario.getNome());
            controller.setDocumentoRetirante(SessaoUsuario.getCpf());
            controller.setTelefoneRetirante(telefone);
            controller.setIdObjeto(idObjeto);

            if (controller.reivindicarObjeto()) {
                JOptionPane.showMessageDialog(this, "Reivindicação registrada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível registrar a reivindicação.");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao reivindicar objeto: " + e.getMessage());
        }
    }

    private void carregarCategorias() {
        try {
            ObjetoController controller = new ObjetoController();
            DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
            modelo.addElement("Selecionar categoria");

            for (String categoria : controller.listarCategorias()) {
                modelo.addElement(categoria);
            }

            jCcategoria.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar categorias: " + e.getMessage());
        }
    }

    private void buscar() {
        try {
            ObjetoController controller = new ObjetoController();

            controller.setNomeFiltro(jTnome_obj.getText().trim());

            String categoria = (String) jCcategoria.getSelectedItem();
            if (categoria != null && !categoria.equals("Selecionar categoria")) {
                controller.setCategoriaFiltro(categoria);
            }

            controller.setDepoisDeFiltro(lerData(jFdepois_de));
            controller.setAntesDeFiltro(lerData(jFantes_de));

            List<ObjetoResumo> objetos = controller.listarObjetos();

            DefaultTableModel modelo = new DefaultTableModel(
                    new String[]{"ID", "Descrição", "Categoria", "Campus", "Status", "Data do achado"}, 0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (ObjetoResumo objeto : objetos) {
                modelo.addRow(new Object[]{
                    objeto.getId(),
                    objeto.getDescricao(),
                    objeto.getCategoria(),
                    objeto.getCampus(),
                    objeto.getStatus(),
                    objeto.getDataAchado()
                });
            }

            jTabresult.setModel(modelo);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao buscar objetos: " + e.getMessage());
        }
    }

    private LocalDate lerData(JFormattedTextField campo) {
        Object valor = campo.getValue();
        if (valor == null) {
            return null;
        }
        Date data = (Date) valor;
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTnome_obj = new javax.swing.JTextField();
        jCcategoria = new javax.swing.JComboBox<>();
        jFdepois_de = new javax.swing.JFormattedTextField();
        jFantes_de = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTabresult = new javax.swing.JTable();
        jBreivindicarobj = new javax.swing.JButton();
        javax.swing.JPanel jPanel3 = new br.com.controller.PainelFundoAzul();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jBinicio = new javax.swing.JButton();
        jBsairTelaPrin = new javax.swing.JButton();
        jBreivindicacoes = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setViewportView(jPtotal);

        jPtotal.setBackground(new java.awt.Color(243, 246, 249));
        jPtotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPtotal.setPreferredSize(new java.awt.Dimension(900, 900));
        jPtotal.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(243, 246, 249));
        jPanel2.setLayout(null);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(10, 61, 163));
        jLabel15.setText("Procurar objetos");
        jPanel2.add(jLabel15);
        jLabel15.setBounds(160, 10, 390, 30);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(10, 61, 163));
        jLabel13.setText("Busque por objetos");
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

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(4, 120, 203));
        jLabel2.setText("Nome do objeto:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(4, 120, 203));
        jLabel5.setText("Categoria:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(4, 120, 203));
        jLabel7.setText("Depois de:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(4, 120, 203));
        jLabel8.setText("Antes de:");

        jCcategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecionar categoria" }));
        jCcategoria.addActionListener(this::jCcategoriaActionPerformed);

        jFdepois_de.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT))));
        jFdepois_de.addActionListener(this::jFdepois_deActionPerformed);

        jFantes_de.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT))));
        jFantes_de.addActionListener(this::jFantes_deActionPerformed);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(4, 90, 203));
        jLabel9.setText("Filtro de Busca");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTnome_obj, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(178, 178, 178)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jCcategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jFdepois_de, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(97, 97, 97)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFantes_de, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(135, 135, 135))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTnome_obj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCcategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFdepois_de, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFantes_de, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        jPtotal.add(jPanel5);
        jPanel5.setBounds(70, 220, 1190, 110);

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTabresult.setAutoCreateRowSorter(true);
        jTabresult.setModel(new javax.swing.table.DefaultTableModel(
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
        jTabresult.setToolTipText("");
        jTabresult.setCursor(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR));
        jTabresult.setGridColor(new java.awt.Color(243, 247, 254));
        jTabresult.setInheritsPopupMenu(true);
        jTabresult.setSelectionBackground(new java.awt.Color(243, 247, 254));
        jScrollPane3.setViewportView(jTabresult);

        jPtotal.add(jScrollPane3);
        jScrollPane3.setBounds(70, 380, 1190, 470);

        jBreivindicarobj.setBackground(new java.awt.Color(4, 120, 203));
        jBreivindicarobj.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jBreivindicarobj.setForeground(new java.awt.Color(255, 255, 255));
        jBreivindicarobj.setText("Reivindicar");
        jBreivindicarobj.addActionListener(this::jBreivindicarobjActionPerformed);
        jPtotal.add(jBreivindicarobj);
        jBreivindicarobj.setBounds(80, 870, 120, 40);

        jScrollPane1.setViewportView(jPtotal);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(180, 90, 1360, 970);

        jPanel3.setBackground(new java.awt.Color(10, 61, 163));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICON/Emblema Branco sobre Azul Real (1).png"))); // NOI18N
        jLabel1.setToolTipText("");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Achados e Perdidos - Portal do Estudante");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 809, Short.MAX_VALUE)
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

        jBreivindicacoes.setBackground(new java.awt.Color(10, 61, 163));
        jBreivindicacoes.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jBreivindicacoes.setForeground(new java.awt.Color(255, 255, 255));
        jBreivindicacoes.setText("Reivindicações");
        jBreivindicacoes.setContentAreaFilled(false);
        jBreivindicacoes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBreivindicacoes.addActionListener(this::jBreivindicacoesActionPerformed);

        jLabel11.setBackground(new java.awt.Color(0, 51, 204));
        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Procurar Objeto");
        jLabel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 204), 5));
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel11.setOpaque(true);
        jLabel11.setPreferredSize(new java.awt.Dimension(113, 18));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jBsairTelaPrin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBinicio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBreivindicacoes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(248, 248, 248)
                .addComponent(jBinicio, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jBreivindicacoes, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113)
                .addComponent(jBsairTelaPrin, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(499, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel6);
        jPanel6.setBounds(0, 0, 180, 1060);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCcategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCcategoriaActionPerformed
        buscar();
    }//GEN-LAST:event_jCcategoriaActionPerformed

    private void jFdepois_deActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFdepois_deActionPerformed
        buscar();
    }//GEN-LAST:event_jFdepois_deActionPerformed

    private void jFantes_deActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFantes_deActionPerformed
        buscar();
    }//GEN-LAST:event_jFantes_deActionPerformed

    private void jBreivindicarobjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBreivindicarobjActionPerformed
        reivindicarSelecionado();
    }//GEN-LAST:event_jBreivindicarobjActionPerformed

    private void jBinicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBinicioActionPerformed
        // TODO add your handling code here:
        TelaPrincipal_Alunos tela = new TelaPrincipal_Alunos();
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

    private void jBreivindicacoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBreivindicacoesActionPerformed
        // TODO add your handling code here:
        TelaReivindicacoes_Alunos tela = new TelaReivindicacoes_Alunos();
        tela.setLocationRelativeTo(this);
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jBreivindicacoesActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new ProcurarObj().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBinicio;
    private javax.swing.JButton jBreivindicacoes;
    private javax.swing.JButton jBreivindicarobj;
    private javax.swing.JButton jBsairTelaPrin;
    private javax.swing.JComboBox<String> jCcategoria;
    private javax.swing.JFormattedTextField jFantes_de;
    private javax.swing.JFormattedTextField jFdepois_de;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPtotal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTabresult;
    private javax.swing.JTextField jTnome_obj;
    // End of variables declaration//GEN-END:variables
}
