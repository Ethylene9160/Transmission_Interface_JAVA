import web_tools.MyData;
import web_tools.TransmissionController;
import web_tools.TransmissionListener;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Mike INIT over");
        Person mike = new Person("Mike");

//        Thread.sleep(50);

        Person gan_yu = new Person("Gan Yu");

//        Thread.sleep(50);

        Person ye_yang = new Person("Wang Xiaomei");
        System.out.println("persons init over");
//        Thread.sleep(100);

        mike.send(102, "bSo beautiful! ");
        System.out.println("mike send over");
//        Thread.sleep(100);

        gan_yu.send(101, "bThk, Mike.");
        System.out.println("gan_yu send over");
//        Thread.sleep(100);

//        gan_yu.send(101, "bDadada");
        gan_yu.send(101, new FileData("myHeader.jpg", "rec_header.jpg"));
        System.out.println("gan_yu send over");
//        Thread.sleep(100);

        ye_yang.send(101, "bI'm real Ganyu!!");
        System.out.println("ye_yang send over");
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

    public void send(int targetID, String info) {
        //String s = targetID + StringServer.SPLIT_REG + info;
        controller.send(new MyData(MyData.STRING_INDEX,String.valueOf(targetID),info));
    }

    public void send(int targetID, FileData data) {
        controller.send(new MyData(MyData.FILE_INDEX,String.valueOf(targetID) , data));
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
    public void onTransmissionError(String message, ErrorType errorType) {

    }

    @Override
    public void onTransmissionProgress(MyData data) {
        switch (data.getType()){
            //If I get a string, I will show it.
            case MyData.STRING_INDEX:
                String message = data.getData();
                showMessage(message.charAt(0), message.substring(1));
                break;
            // if I get a file, I will convert it to a file and write it to local path and store.
            case MyData.FILE_INDEX:
                FileData fileData = data.getData();
                System.out.println(fileData.getFileName());
                System.out.println(fileData.getFileBytes().length);
                FileToBinaryConverter.binaryToWrittenFile(fileData.getFileBytes(), fileData.getFileName());
                break;
        }
    }

    @Override
    public void alertError(String error) {
        System.out.println(error);
    }
}

/**
 * A test class to store the file data. I use this class to store a file name and the binary of the file.
 * And the put the file to the <code>MyData</code> class, as the member variable <code>data</code> in the <code>MyData</code> class.
 */
class FileData implements Serializable{
    @Serial
    private static final long serialVersionUID = 2333L;
    private byte[] file;
    private String fileName;
    public FileData(String filePath, String fileName){
        try {
            this.file = FileToBinaryConverter.fileToBinary(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.fileName = fileName;
    }
    public byte[] getFileBytes(){
        return file;
    }

    public String getFileName(){
        return fileName;
    }
}


/**
 * A class to convert file to binary and binary to file.
 */
class FileToBinaryConverter {
    /**
     * Convert a file to binary.
     * @param file The file you want to convert.
     * @return The binary of the file.
     * @throws IOException When the file is not found, this exception will appear.
     */
    public static byte[] fileToBinary(File file) throws IOException {
        // 使用Java的NIO库读取文件内容并返回二进制数组
        return Files.readAllBytes(file.toPath());
    }

    /**
     * Convert a file to binary.
     * @param filePath The path(String) of the file you want to convert.
     * @return The binary of the file.
     * @throws IOException When the file is not found, this exception will appear.
     */
    public static byte[] fileToBinary(String filePath) throws IOException {
        // 使用Java的NIO库读取文件内容并返回二进制数组
        return Files.readAllBytes(Paths.get(filePath));
    }

    /**
     * Convert binary to file.
     * @param binaryData The binary you want to convert.
     * @param filePath The path(String) of the file you want to write to.
     */
    public static void binaryToWrittenFile(byte[] binaryData, String filePath){
        // 创建一个FileOutputStream对象，用于将二进制数据写入文件
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(filePath);
            fos.write(binaryData);
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}



