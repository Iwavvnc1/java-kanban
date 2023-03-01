package Main;


import HttpTaskServer.HttpTaskServer;
import KVServer.KVServer;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        new KVServer().start();
        new HttpTaskServer().start();

    }
}