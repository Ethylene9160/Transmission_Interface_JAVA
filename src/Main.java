import web_tools.MyData;
import web_tools.TransmissionController;
import web_tools.TransmissionListener;

import java.io.IOException;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Mike INIT over");
        Person mike = new Person("Mike");

        Thread.sleep(50);

        Person gan_yu = new Person("Gan Yu");

        Thread.sleep(50);

        Person ye_yang = new Person("Wang Xiaomei");

        Thread.sleep(100);

        mike.send(102, "bSo beautiful! ");

        Thread.sleep(100);

        gan_yu.send(101, "bThk, Mike.");

        Thread.sleep(100);

        gan_yu.send(101, "bDadada");

        Thread.sleep(100);

        ye_yang.send(101, "bI'm real Ganyu!!");
    }
}


class Person implements TransmissionListener {

    int ownID;
    String name;
    TransmissionController controller;

    public Person(String name) throws InterruptedException{
        this.name = name;
        try {
            controller = new TransmissionController(new Socket("127.0.0.1",8999), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void webAction(String message) {
//        showMessage(message.charAt(0), message.substring(1));
//    }

    public void send(int targetID, String info) {
        //String s = targetID + StringServer.SPLIT_REG + info;
        controller.send(new MyData(targetID + "#" + info));
    }

    private void showMessage(char index, String msg) {
        switch (index) {
            case 'a':
                ownID = Integer.parseInt(msg);
                break;
            case 'b':
                System.out.println(name + " Received: " + msg);
                break;
        }
    }


    @Override
    public void onTransmissionStart() {

    }

    @Override
    public void onTransmissionEnd() {

    }

    @Override
    public void onTransmissionError(String message, int errorType) {

    }

    @Override
    public void onTransmissionProgress(Object o) {
        String message = ((MyData)o).getData();
        showMessage(message.charAt(0), message.substring(1));
    }

    @Override
    public void alertError(String error) {
        System.out.println(error);
    }
}


