package Main;


import HttpTaskServer.HttpTaskServer;
import KVServer.KVServer;

import java.io.IOException;
import java.net.URISyntaxException;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        new KVServer().start();
        new HttpTaskServer().start();

    }
}