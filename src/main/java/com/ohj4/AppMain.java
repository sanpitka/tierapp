package com.ohj4;

import javax.swing.*;


public final class AppMain {
    private AppMain() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new StartWindow());
        
    }
}
