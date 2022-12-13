package com.xmudronc;

import java.io.IOException;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

public class Menu {
    private Terminal terminal;
    private NonBlockingReader reader;
    private Integer width = 20;
    private Integer height = 20;
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
    
    public void snajkSelected() throws IOException {
        Snajk snajk = new Snajk(this.terminal, this.reader, this.width, this.height);
        snajk.init();
    }

    public void scoreSelected() {

    }

    public void exitSelected() throws IOException {
        running = false;
        terminal.close();
        reader.close();
        System.exit(0);
    }

    public void init() throws IOException {
        terminal = TerminalBuilder.builder().build();
        terminal.enterRawMode();
        reader = terminal.reader();
        drawBorder();
        drawMenu();
    }

    public void init(Terminal terminal, NonBlockingReader reader) throws IOException {
        this.terminal = terminal;
        this.reader = reader;
        drawBorder();
        drawMenu();
    }

    public void drawBorder() throws IOException {
        Integer w = terminal.getWidth();
        Integer h = terminal.getHeight();
        Integer res = w<h?w:h;
        width = res - 1;
        height = res - 1;
        System.out.print(String.format("%c[%d;%df", 0x1B, 1, 1));
        for (Integer y = 0; y < height; y++) {
            for (Integer x = 0; x < width; x++) {
                if (y == 0 || y == height-1) {
                    System.out.print("\u001B[31m" + Symbol.BLOCK.value);
                } else {
                    if (x == 0 || x == width-1) {
                        System.out.print("\u001B[31m" + Symbol.BLOCK.value);
                    } else {
                        System.out.print(Symbol.EMPTY.value);
                    }
                }
            }
            System.out.println();
        }       
    }

    public void drawMenu() {
        prepareMenuOptions();
        running = true;
        input.start();
    }

    public void prepareMenuOptions() {
        int x = width;
        int y = (height/2)-3;
        Option newGame = new Option(OptionId.NEW_GAME, new Point(x, y), "New Game");
        Option highScore = new Option(OptionId.HIGH_SCORE ,new Point(x, y+2), "High Score");
        Option exit = new Option(OptionId.EXIT, new Point(x, y+4), "Exit");
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

        public void select() {
            System.out.print(String.format("%c[%d;%df", 0x1B, point.getY(), point.getX()-(value.length()/2)));
            System.out.print("\u001B[34m" + value);
        }

        public void deselect() {
            System.out.print(String.format("%c[%d;%df", 0x1B, point.getY(), point.getX()-(value.length()/2)));
            System.out.print("\u001B[32m" + value);
        }
    }
}
