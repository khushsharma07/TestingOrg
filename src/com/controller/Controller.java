package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public interface Controller {
    public void displayMenu(BufferedReader stdIn, PrintWriter out, BufferedReader in) throws IOException;
}
