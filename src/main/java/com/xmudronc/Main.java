package com.xmudronc;

import java.io.IOException;

import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

public class Main {
    private static Size startupSize;
    private static Size runSize = new Size(120, 40);
    private static Terminal terminal;
    private static NonBlockingReader reader;

    private static void resizeTerminal(Integer columns, Integer rows) {
        System.out.print("\u001B[8;" + rows + ";" + columns + "t"); 
    }

    public static void main(String[] args) {
        try {
            terminal = TerminalBuilder.builder().build();
            terminal.enterRawMode();
            startupSize = terminal.getSize();
            resizeTerminal(runSize.getColumns(), runSize.getRows());
            reader = terminal.reader();

            SplashScreen splash = new SplashScreen(terminal, reader, startupSize, runSize);
            splash.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
