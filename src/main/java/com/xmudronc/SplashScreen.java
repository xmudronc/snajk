package com.xmudronc;

import java.io.IOException;

import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

public class SplashScreen {
    private Size startupSize;
    private Size runSize;
    private Terminal terminal;
    private NonBlockingReader reader;
    private Boolean running = false;
    private String a = "|/\\/\\/\\/\\|";
    private String b = "|\\/\\/\\/\\/|";
    private String current = a;
    private Thread input = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                if (reader != null) {
                    while (running) {
                        Integer value = reader.read();
                        handleInput(value);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });
    private Thread animate = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                while (running) {
                    Thread.sleep(150);
                    animate();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
    
    public SplashScreen(Terminal terminal, NonBlockingReader reader, Size startupSize, Size runSize) {
        this.terminal = terminal;
        this.reader = reader;
        this.startupSize = startupSize;
        this.runSize = runSize;
    }

    private void handleInput(Integer value) {
        if (value == 13) {
            running = false;
            Menu menu = new Menu(terminal, reader, startupSize, runSize);
            menu.initWithLogArea();
        }
    }

    private void animate() {
        if (current.equals(a)) {
            current = b;
        } else {
            current = a;
        }
        
        System.out.print(String.format("%c[%d;%df", 0x1B, runSize.getRows()/2+1, runSize.getColumns()/2-5));
        System.out.print("\u001B[34m" + current);
    }

    private void drawSplash() {
        for (int y = 1; y < runSize.getRows(); y++) {
            for (int x = 0; x < runSize.getColumns(); x+=2) {
                System.out.print(String.format("%c[%d;%df", 0x1B, y, x));
                System.out.print("\u001B[0m" + Symbol.EMPTY.value);
            }
        }

        System.out.print(String.format("%c[%d;%df", 0x1B, runSize.getRows()/2-1, runSize.getColumns()/2-6));
        System.out.print("\u001B[32m" + "PRESS RETURN");
        
        System.out.print(String.format("%c[%d;%df", 0x1B, runSize.getRows()/2+1, runSize.getColumns()/2-5));
        System.out.print("\u001B[34m" + a);

        System.out.print("\u001B[0m");
    }

    public void init() {
        running = true;
        drawSplash();
        animate.start();
        input.start();
    }
}
