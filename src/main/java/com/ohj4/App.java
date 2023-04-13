package com.ohj4;

import javax.swing.*;


public final class App {
    private App() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new StartWindow());
        
    }
}
