package web_tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;


public class TransmissionController{
    public final static byte STRING_TYPE = 0, FILE_TYPE  =1;
    private TransmissionListener listener;
    private Sender sender;
    private Receiver receiver;
    private Socket socket;

    void send(Object message){
        sender.send(message);
    }
}

class Sender{
    private Socket socket;
    private OutputStream outputStream;

    private TransmissionListener listener;
    void send(Object data){

    }

    private void send(byte[] bytes){
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            //throw new RuntimeException(e);
            listener.allertError("todo");
        }
    }
}

class Receiver implements Runnable{
    private Socket socket;
    private TransmissionListener listener;
    private ObjectInputStream objectInputStream;
    boolean flag;

    public Receiver(InputStream inputStream, TransmissionListener listener){
        //this->inputStream = inputStream;
        //this->listener = listener;

    }
    @Override
    public void run(){
        while(flag){
            try {
//                byte[] bytes = new byte[1025];
//                int readBytes = 0;
//                int len = bytes.length;
                //todo: 缓冲池！
//                while (readBytes < len) {
//                    int read = inputStream.read(bytes, readBytes, len - readBytes);
//                    //judge whether the stream has read the last file.
//                    if (read == -1) break;
//                    readBytes += read;
//                }
//                if(byteArrayToHexString(bytes) == 0xFFEE12A1) return;
                Object o = objectInputStream.readObject();
                //listener.onTransmissionProgress(T);
            }catch (IOException e){
                e.printStackTrace();
                listener.allertError("todo");
                flag = false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                listener.allertError("ClassNotFound");
            }
        }
    }
}
