import web_tools.MyData;
import web_tools.TransmissionController;
import web_tools.TransmissionListener;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
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


class Person implements TransmissionListener<MyData> {

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
        controller.send(new MyData(MyData.STRING_INDEX,targetID + "#" + info));
    }

    public void send(int targetID, FileData data) {
        controller.send(new MyData(MyData.FILE_INDEX,targetID + "#" , data));
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
    public void onTransmissionProgress(MyData data) {
        switch (data.getType()){
            case MyData.STRING_INDEX:
                String message = data.getServerMessage();
                showMessage(message.charAt(0), message.substring(1));
                break;
            case MyData.FILE_INDEX:
                String fileData = data.getData();
                //System.out.println(fileData.getFileName());
                //help me to write this file to the local disk

                break;
        }
//        String message = messages.getData();
//        showMessage(message.charAt(0), message.substring(1));
    }


    @Override
    public void alertError(String error) {
        System.out.println(error);
    }
}

class FileData implements Serializable{
    @Serial
    private static final long serialVersionUID = 2333L;
    private File file;
    private String fileName;
    public FileData(File file, String fileName){
        this.file = file;
        this.fileName = fileName;
    }
    public File getFile(){
        return file;
    }

    public String getFileName(){
        return fileName;
    }
}


