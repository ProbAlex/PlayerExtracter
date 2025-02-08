package org.example.Utils;

import java.util.ArrayList;
public class Pricing {
    private ArrayList<String> InitialQueue = new ArrayList<String>();
    private ArrayList<String> Queue19 = new ArrayList<String>();
    private ArrayList<String> Queue38 = new ArrayList<String>();
    private String APIKEY;
    private WebsocketNames SOCKET;

    public Pricing(String APIKEY, WebsocketNames SOCKET){
        this.APIKEY = APIKEY;
        this.SOCKET = SOCKET;
    }

    public void checkPlayer(String UUID){
        SOCKET.push(UUID);
    }

    public void pushList(String UUID) throws InterruptedException {
        InitialQueue.add(UUID);
        Thread.sleep(19000);
        Queue19.add(UUID);
        Thread.sleep(19000);
        Queue38.add(UUID);
    }

    public String process(String UUID){

        return "";
    }


}
