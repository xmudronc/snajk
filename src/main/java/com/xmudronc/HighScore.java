package com.xmudronc;

import java.io.IOException;

import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

public class HighScore {
    private Terminal terminal;
    private NonBlockingReader reader;
    private Integer width;
    private Integer height;

    public HighScore(Terminal terminal, NonBlockingReader reader, Integer width, Integer height) {
        this.terminal = terminal;
        this.reader = reader;
        this.width = width;
        this.height = height;
    }

    public void init() throws IOException {
        Menu menu = new Menu();
        menu.init(terminal, reader); 
    }
}
