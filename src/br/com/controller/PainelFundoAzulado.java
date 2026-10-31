package br.com.controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class PainelFundoAzulado extends JPanel {

    public PainelFundoAzulado() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setColor(new Color(243, 246, 249));
        g2.fillRoundRect(
            0, 0, getWidth(), getHeight(), 15, 15
        );

        g2.dispose();
    }
}
