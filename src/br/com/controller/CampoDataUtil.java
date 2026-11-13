
package br.com.controller;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JFormattedTextField;

public class CampoDataUtil {

    public static final String FORMATO = "DD/MM/AAAA";

    public static void aplicarFormatoAuxiliar(JFormattedTextField campo) {
        campo.setForeground(Color.GRAY);
        campo.setText(FORMATO);

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(FORMATO)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().trim().isEmpty()) {
                    campo.setForeground(Color.GRAY);
                    campo.setText(FORMATO);
                }
            }
        });
    }

    public static String obterValor(JFormattedTextField campo) {
        String texto = campo.getText();
        return texto == null || texto.equals(FORMATO) ? "" : texto.trim();
    }

    public static boolean estaVazio(JFormattedTextField campo) {
        return obterValor(campo).isEmpty();
    }
}
