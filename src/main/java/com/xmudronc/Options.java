package com.xmudronc;

import java.io.IOException;

import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

public class Options {
    private Terminal terminal;
    private NonBlockingReader reader;
    private LogArea logArea;
    private Size startupSize;
    private Size runSize;

    public Options(Terminal terminal, NonBlockingReader reader, LogArea logArea, Size startupSize, Size runSize) {
        this.terminal = terminal;
        this.reader = reader;
        this.logArea = logArea;
        this.startupSize = startupSize;
        this.runSize = runSize;
    }

    public void init() throws IOException {
        logArea.printToLogOverwritable("OPTIONS TBD");
        Menu menu = new Menu(terminal, reader, startupSize, runSize);
        menu.initPlayAreaOnly(logArea); 
    }
}
