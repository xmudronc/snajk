package com.xmudronc;

import java.io.IOException;

import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

public class HighScore {
    private Terminal terminal;
    private NonBlockingReader reader;
    private LogArea logArea;
    private Size startupSize;
    private Integer width;
    private Integer height;

    public HighScore(Terminal terminal, NonBlockingReader reader, LogArea logArea, Size startupSize, Integer width, Integer height) {
        this.terminal = terminal;
        this.reader = reader;
        this.logArea = logArea;
        this.startupSize = startupSize;
        this.width = width;
        this.height = height;
    }

    public void init() throws IOException {
        Menu menu = new Menu();
        menu.init(terminal, reader, logArea, startupSize); 
    }
}
