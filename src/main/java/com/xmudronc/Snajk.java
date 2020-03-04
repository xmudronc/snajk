/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.xmudronc;

import java.io.IOException;
import java.util.ArrayList;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

public class Snajk {
    private ArrayList<Point> food = new ArrayList<>();
    private Terminal terminal;
    private Boolean running = false;
    private String direction = "N";
    private int delay = 150;
    private Integer width = 20;
    private Integer height = 20;
    private final String BLOCK = "\u2588\u2588";
    private final String EMPTY = "  ";
    private Segment mainSegment;
    private Thread input = new Thread(new Runnable() {
        @Override
        public void run() {
            NonBlockingReader reader;
            try {
                reader = terminal.reader();
                if (reader != null) {
                    while (running) {
                        Integer value = reader.read();
                        if (value >= 65 && value <= 90) {
                            value += 32;
                        }
                        if (value == 113 || value == 27) {
                            running = false;
                            reader.close();
                            terminal.close();
                            move.interrupt();
                            System.exit(0);
                        } else {
                            move(value);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });
    private Thread move = new Thread(new Runnable() {
        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    System.out.print("GAME ENDED BY USER");
                }
                switch (direction) {
                    case "L":
                        if (mainSegment.getX() > 2) {
                            mainSegment.setY(mainSegment.getY() + 0);
                            mainSegment.setX(mainSegment.getX() - 2);
                            if (checkMove(mainSegment)) {
                                printSegment(mainSegment);
                            } else {
                                gover();
                            }
                        }
                        break;
                    case "R":
                        if (mainSegment.getX() < (width*2)-2) {
                            mainSegment.setY(mainSegment.getY() + 0);
                            mainSegment.setX(mainSegment.getX() + 2);
                            if (checkMove(mainSegment)) {
                                printSegment(mainSegment);
                            } else {
                                gover();
                            }
                        }    
                        break;
                    case "U":
                        if (mainSegment.getY() > 1) {
                            mainSegment.setY(mainSegment.getY() - 1);
                            mainSegment.setX(mainSegment.getX() + 0);
                            if (checkMove(mainSegment)) {
                                printSegment(mainSegment);
                            } else {
                                gover();
                            }
                        }
                        break;
                    case "D":
                        if (mainSegment.getY() < height) {
                            mainSegment.setY(mainSegment.getY() + 1);
                            mainSegment.setX(mainSegment.getX() + 0);
                            if (checkMove(mainSegment)) {
                                printSegment(mainSegment);
                            } else {
                                gover();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    });

    public void move(Integer key) {
        switch (key) {
            case 97: // a
                if (direction != "R") {
                    direction = "L";                    
                }
                break;
            case 100: // d
                if (direction != "L") {
                    direction = "R";
                }
                break;
            case 115: // s
                if (direction != "U") {
                    direction = "D";
                }
                break;
            case 119: // w
                if (direction != "D") {
                    direction = "U";
                }
                break;
            default:
                break;
        }
    }

    public void checkCollisionFood() {
        for (Point _point : food) {
            if (mainSegment.getX().equals(_point.getX()) && mainSegment.getY().equals(_point.getY())) {
                mainSegment.addSegment(new Segment());
                System.out.print(String.format("%c[%d;%df", 0x1B, 1, 41));
                System.out.print("ADDSEG");
            }
        }
    }

    public Boolean checkCollisionSelf(Segment segment) {
        if (segment != null) {
            if (segment.getX().equals(mainSegment.getX()) && segment.getY().equals(mainSegment.getY())) {
                return false;
            } else {
                if (segment.getNext() != null) {
                    return checkCollisionSelf(segment.getNext());
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    public void gover() {
        System.out.print(String.format("%c[%d;%df", 0x1B, 1, 41));
        System.out.print("GOVER");
        running = false;        
        try {
            terminal.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.input.interrupt();
        this.move.interrupt();
        System.exit(0);
    }

    public boolean checkMove(Segment segment) {
        System.out.print(String.format("%c[%d;%df", 0x1B, 1, 1));
        System.out.print(mainSegment.getX() + " " + mainSegment.getY());
        if (segment.getX() <= 2 || segment.getX() >= (width*2)-2 || segment.getY() <= 1
                || segment.getY() >= height) {
            return false;
        } else {
            checkCollisionFood();
            return checkCollisionSelf(mainSegment.getNext());
        }
    }

    public void printToXY(Integer x, Integer y, String print, String fgr, String bgr) {
        System.out.print(String.format("%c[%d;%df", 0x1B, y, x));
        System.out.print(print);
    }

    public void printSegment(Segment segment) {
        if (segment != null) {
            printToXY(segment.getX(), segment.getY(), "\u001B[32m" + BLOCK, "fgr", "bgr");
            if (segment.getNext() != null) {
                Segment next = segment.getNext();
                next.setX(segment.getPrevX());
                next.setY(segment.getPrevY());
                printSegment(next);
            } else {
                printToXY(segment.getPrevX(), segment.getPrevY(), EMPTY, "fgr", "bgr");
            }
        }
    }

    public void start() {
        this.running = true;
        this.input.start();
        this.move.start();
    }

    public void init() throws IOException {
        terminal = TerminalBuilder.builder().build();
        terminal.enterRawMode();
        Integer w = terminal.getWidth();
        Integer h = terminal.getHeight();
        Integer res = w<h?w:h;
        this.width = res - 1;
        this.height = res - 1;
        System.out.print(String.format("%c[%d;%df", 0x1B, 1, 1));
        for (Integer y = 0; y < height; y++) {
            for (Integer x = 0; x < width; x++) {
                if (y == 0 || y == height-1) {
                    System.out.print("\u001B[31m" + BLOCK);
                } else {
                    if (x == 0 || x == width-1) {
                        System.out.print("\u001B[31m" + BLOCK);
                    } else {
                        System.out.print(EMPTY);
                    }
                }
            }
            System.out.println();
        }
        //DEBUG ONLY points
        food.add(new Point(11, 11));
        System.out.print(String.format("%c[%d;%df", 0x1B, 11, 11));
        System.out.print(BLOCK);
        food.add(new Point(21, 21));
        System.out.print(String.format("%c[%d;%df", 0x1B, 21, 21));
        System.out.print(BLOCK);
        food.add(new Point(31, 31));
        System.out.print(String.format("%c[%d;%df", 0x1B, 31, 31));
        System.out.print(BLOCK);
    }

    public static void main(String[] args) {
        try {
            Snajk snajk = new Snajk();
            snajk.init();
            snajk.mainSegment = new Segment(3, 2);
            Segment initSegment = new Segment(3, 2);
            initSegment.setPrev(snajk.mainSegment);
            snajk.mainSegment.setNext(initSegment);
            snajk.mainSegment.setX(5);
            snajk.mainSegment.setY(2);
            snajk.printSegment(snajk.mainSegment);
            snajk.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
