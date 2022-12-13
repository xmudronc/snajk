package com.xmudronc;

import java.io.IOException;

public class Main {
    private static Menu menu = new Menu();
    public static void main(String[] args) {
        try {
            menu.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
