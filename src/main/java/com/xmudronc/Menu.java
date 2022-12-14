package com.xmudronc;

import java.io.IOException;

import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

public class Menu {
    private LogArea logArea;
    private Size startupSize;
    private Size runSize = new Size(120, 40);
    private Terminal terminal;
    private NonBlockingReader reader;
    private Integer buttonWidth = 20;
    private Option selectedOption;
    private Boolean running = false;
    private Thread input = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                if (reader != null) {
                    while (running) {
                        Integer value = reader.read();
                        if (value >= 65 && value <= 90) {
                            value += 32;
                        }
                        moveCursor(value);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });
    
    private void moveCursor(Integer key) throws IOException {
        switch (key) {
            case 115: // s
                selectedOption.deselect();
                selectedOption = selectedOption.getNext();
                selectedOption.select();
                break;
            case 119: // w
                selectedOption.deselect();
                selectedOption = selectedOption.getPrevious();
                selectedOption.select();
                break;
            case 13:
                select();
                break;
            default:
                break;
        }
    }

    public void select() throws IOException {
        running = false;
        switch (selectedOption.getId()) {
            case NEW_GAME:
                snajkSelected();
                break;
            case HIGH_SCORE:
                scoreSelected();
                break;
            case EXIT:
                exitSelected();
                break;
            default:
                exitSelected();
                break;
        }
    }

    public void clearGameArea() {
        for (Integer y = 1; y < runSize.getRows(); y++) {
            for (Integer x = 1; x < runSize.getRows()*2+4; x+=2) {
                System.out.print(String.format("%c[%d;%df", 0x1B, y, x));
                System.out.print("\u001B[0m" + Symbol.EMPTY.value);
            }
        } 
    }
    
    public void snajkSelected() throws IOException {
        clearGameArea();
        Snajk snajk = new Snajk(this.terminal, this.reader, this.logArea, this.startupSize, runSize.getRows(), runSize.getRows());
        snajk.init();
    }

    public void scoreSelected() throws IOException {
        clearGameArea();
        HighScore highScore = new HighScore(this.terminal, this.reader, this.logArea, this.startupSize, runSize.getRows(), runSize.getRows());
        highScore.init();
    }

    public void exitSelected() throws IOException {
        resizeTerminal(startupSize.getColumns(), startupSize.getRows());
        running = false;
        terminal.close();
        reader.close();
        System.exit(0);
    }

    private void drawArea(Integer fromY, Integer toY, Integer fromX, Integer toX) {
        for (Integer y = fromY; y < toY; y++) {
            for (Integer x = fromX; x < toX; x+=2) {
                System.out.print(String.format("%c[%d;%df", 0x1B, y, x));
                if (y == fromY || y == toY-1) {
                    System.out.print("\u001B[41m" + Symbol.EMPTY.value);
                } else {
                    if (x == fromX || x == toX-1) {
                        System.out.print("\u001B[41m" + Symbol.EMPTY.value);
                    } else {
                        System.out.print("\u001B[0m" + Symbol.EMPTY.value);
                    }
                }
            }
            if (y != fromY) {
                System.out.print(String.format("%c[%d;%df", 0x1B, y, toX+1));
                System.out.print("\u001B[30m\u001B[40m" + Symbol.BLOCK.value);
            } else {
                System.out.print(String.format("%c[%d;%df", 0x1B, y, toX+1));
                System.out.print("\u001B[0m" + Symbol.EMPTY.value);
            }
            System.out.println();
        }    
        for (Integer x = fromX; x < toX+2; x+=2) {
            System.out.print(String.format("%c[%d;%df", 0x1B, toY, x));
            if (x < fromX+2) {
                System.out.print("\u001B[0m" + Symbol.EMPTY.value);
            } else {
                System.out.print("\u001B[30m\u001B[40m" + Symbol.BLOCK.value);
            }
        } 
        System.out.print("\u001B[0m"); 
    }

    private void drawLogArea() {
        Integer fromY = 11;
        Integer toY = runSize.getRows();
        Integer fromX = runSize.getRows()*2+5;
        Integer toX = runSize.getColumns()-2;
        drawArea(fromY, toY, fromX, toX);
    }

    public void drawScoreArea() {
        Integer fromY = 1;
        Integer toY = 9;
        Integer fromX = runSize.getRows()*2+5;
        Integer toX = runSize.getColumns()-2;
        drawArea(fromY, toY, fromX, toX);

        System.out.print(String.format("%c[%d;%df", 0x1B, 3, fromX+4));
        System.out.print("\u001B[37m" + "SCORE:");
        System.out.print(String.format("%c[%d;%df", 0x1B, 6, fromX+4));
        System.out.print("\u001B[37m" + "MULTIPLIER: ");
        System.out.print("\u001B[0m"); 
    }

    public void drawGameArea() {
        Integer fromY = 1;
        Integer toY = runSize.getRows();
        Integer fromX = 1;
        Integer toX = runSize.getRows()*2;
        drawArea(fromY, toY, fromX, toX);
    }

    public void init() throws IOException {
        terminal = TerminalBuilder.builder().build();
        terminal.enterRawMode();
        startupSize = terminal.getSize();
        resizeTerminal(runSize.getColumns(), runSize.getRows());
        reader = terminal.reader();
        logArea = new LogArea(89, 13, 26, 25);
        init(terminal, reader, logArea, startupSize);
        drawScoreArea();
        drawLogArea();
        logArea.printToLog("UP:     W\nDOWN:   S\nLEFT:   A\nRIGHT:  D\n\nSELECT: RETURN\nQUIT:   P\n\n");
        logArea.printToLog("--------------------------\n");
    }

    public void init(Terminal terminal, NonBlockingReader reader, LogArea logArea, Size startupSize) throws IOException {
        this.terminal = terminal;
        this.reader = reader;
        this.logArea = logArea;
        this.startupSize = startupSize;
        clearGameArea();
        drawGameArea();
        drawMenu();
    }

    private void resizeTerminal(Integer columns, Integer rows) {
        System.out.print("\u001B[8;" + rows + ";" + columns + "t"); 
    }

    public void drawMenu() {
        prepareMenuOptions();
        running = true;
        input.start();
    }

    public void prepareMenuOptions() {
        int x = runSize.getRows();
        int y = (runSize.getRows()/2)-5;
        Option newGame = new Option(OptionId.NEW_GAME, new Point(x, y), "New Game");
        Option highScore = new Option(OptionId.HIGH_SCORE ,new Point(x, y+5), "High Score");
        Option exit = new Option(OptionId.EXIT, new Point(x, y+10), "Exit");
        newGame.setLinks(highScore, exit);
        highScore.setLinks(exit, newGame);
        exit.setLinks(newGame, highScore);
        selectedOption = newGame;
        newGame.select();
        highScore.deselect();
        exit.deselect();
    }

    private enum OptionId {
        NEW_GAME,
        HIGH_SCORE,
        EXIT
    }

    private class Option {
        private final OptionId id;
        private final Point point;
        private final String value;
        private Option next;
        private Option previous;
    
        public Option(OptionId id, Point point, String value) {
            this.id = id;
            this.point = point;
            this.value = value;
        }

        public void setLinks(Option next, Option previous) {
            this.next = next;
            this.previous = previous;
        }

        public Option getNext() {
            return this.next;
        }

        public Option getPrevious() {
            return this.previous;
        }

        public OptionId getId() {
            return this.id;
        }

        public void deselect() {
            int startPos = point.getX()-(buttonWidth/2);
            int endPos = point.getX()+(buttonWidth/2);
            for (int x = startPos; x < endPos; x+=2) {
                System.out.print(String.format("%c[%d;%df", 0x1B, point.getY()-1, x));
                System.out.print("\u001B[0m" + Symbol.EMPTY.value);
            }
            
            for (int x = startPos+2; x < endPos+2; x+=2) {
                System.out.print(String.format("%c[%d;%df", 0x1B, point.getY(), x));
                System.out.print("\u001B[41m" + Symbol.EMPTY.value);
            }
            System.out.print(String.format("%c[%d;%df", 0x1B, point.getY(), startPos));
            System.out.print("\u001B[0m" + Symbol.EMPTY.value);
            
            for (int x = startPos+2; x < endPos+2; x+=2) {
                System.out.print(String.format("%c[%d;%df", 0x1B, point.getY()+1, x));
                System.out.print("\u001B[41m" + Symbol.EMPTY.value);
            }
            System.out.print(String.format("%c[%d;%df", 0x1B, point.getY()+1, startPos));
            System.out.print("\u001B[0m" + Symbol.EMPTY.value);
            
            for (int x = startPos+2; x < endPos+2; x+=2) {
                System.out.print(String.format("%c[%d;%df", 0x1B, point.getY()+2, x));
                System.out.print("\u001B[41m" + Symbol.EMPTY.value);
            }
            
            System.out.print(String.format("%c[%d;%df", 0x1B, point.getY()+1, point.getX()-(value.length()/2)+2));
            System.out.print("\u001B[37m\u001B[41m" + value);
            //reset color
            System.out.print("\u001B[0m");
        }

        public void select() {
            int startPos = point.getX()-(buttonWidth/2);
            int endPos = point.getX()+(buttonWidth/2);
            for (int x = startPos; x < endPos; x+=2) {
                System.out.print(String.format("%c[%d;%df", 0x1B, point.getY()-1, x));
                System.out.print("\u001B[91m\u001B[101m" + Symbol.EMPTY.value);
            }
            
            for (int x = startPos; x < endPos; x+=2) {
                System.out.print(String.format("%c[%d;%df", 0x1B, point.getY(), x));
                System.out.print("\u001B[91m\u001B[101m" + Symbol.EMPTY.value);
            }
            System.out.print(String.format("%c[%d;%df", 0x1B, point.getY(), endPos));
            System.out.print("\u001B[30m\u001B[40m" + Symbol.BLOCK.value);
            
            for (int x = startPos; x < endPos; x+=2) {
                System.out.print(String.format("%c[%d;%df", 0x1B, point.getY()+1, x));
                System.out.print("\u001B[91m\u001B[101m" + Symbol.EMPTY.value);
            }
            System.out.print(String.format("%c[%d;%df", 0x1B, point.getY()+1, endPos));
            System.out.print("\u001B[30m\u001B[40m" + Symbol.BLOCK.value);
            
            for (int x = startPos+2; x < endPos+2; x+=2) {
                System.out.print(String.format("%c[%d;%df", 0x1B, point.getY()+2, x));
                System.out.print("\u001B[30m\u001B[40m" + Symbol.BLOCK.value);
            }
            
            System.out.print(String.format("%c[%d;%df", 0x1B, point.getY(), point.getX()-(value.length()/2)));
            System.out.print("\u001B[97m\u001B[101m" + value);
            //reset color
            System.out.print("\u001B[0m");
        }
    }
}
