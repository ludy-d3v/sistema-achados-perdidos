package br.com.controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class PainelFundoAzul extends JPanel {

    public PainelFundoAzul() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setColor(new Color(10,61,163));
        g2.fillRoundRect(
            0, 0, getWidth(), getHeight(), 0, 0
        );

        g2.dispose();
    }
}
