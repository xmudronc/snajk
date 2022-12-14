package com.xmudronc;

public class LogArea {
    private Integer safeAreaX;
    private Integer safeAreaY;
    private Integer safeAreaWidth;
    private Integer safeAreaHeight;
    private Cursor cursor;
    private Integer lastPrintLength = 0;

    public LogArea(Integer x, Integer y, Integer widdth, Integer height) {
        this.safeAreaX = x;
        this.safeAreaY = y;
        this.safeAreaWidth = widdth;
        this.safeAreaHeight = height;
        this.cursor = new Cursor(safeAreaX, safeAreaY);
    }

    public void printToLog(String print) {
        if (print != null) {
            lastPrintLength = print.length();
            Integer counterWidth = cursor.getX();
            Integer counterHeight = cursor.getY();
            for (int i = 0; i < print.length(); i++) {
                if (counterHeight < safeAreaY+safeAreaHeight) {
                    System.out.print(String.format("%c[%d;%df", 0x1B, counterHeight, counterWidth));
                    if (print.charAt(i) == '\n') {
                        System.out.println();
                        counterHeight++;
                        counterWidth = safeAreaX;
                    } else {
                        System.out.print("\u001B[37m" + print.charAt(i));
                        counterWidth++;
                        if (counterWidth >= safeAreaX+safeAreaWidth) {
                            counterWidth = safeAreaX;
                            counterHeight++;
                        }
                    }
                    cursor.setPosition(counterWidth, counterHeight);
                } else {
                    break;
                }
            }  
        }      
    }

    public void printToLogOverwritable(String print) {
        Cursor originCursor = new Cursor(cursor);
        StringBuilder realPrint = new StringBuilder(print);
        if (print.length() < lastPrintLength) {
            for (int i = 0; i < lastPrintLength-print.length(); i++) {
                realPrint.append(" ");
            }
        }
        printToLog(realPrint.toString());
        cursor = originCursor;
    }

    private class Cursor {
        private Integer x;
        private Integer y;

        public Cursor(Cursor cursor) {
            this.x = cursor.getX();
            this.y = cursor.getY();
        }

        public Cursor(Integer x, Integer y) {
            this.x = x;
            this.y = y;
        }

        public void setPosition(Integer x, Integer y) {
            this.x = x;
            this.y = y;
        }

        public Integer getX() {
            return x;
        }

        public Integer getY() {
            return y;
        }
    }
}
