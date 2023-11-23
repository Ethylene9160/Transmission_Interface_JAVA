package web_tools;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

public class TransmissionController implements Closeable{
    /**
     * An instance of TransmissionListener.
     * To make static proxy to use.
     */
    private TransmissionListener listener;
    private Sender sender;
    private Receiver receiver;
    private Socket serverSocket;
    private Thread receivingThread;

    /**
     * Constructor of the transmission proxy <code>TransmissionController</code>
     * @param serverSocket The socket your program plugged
     * @param listener An instance you want to present.
     * TransmissionListener.java
     * @throws NullPointerException If the parameters you pass are all non-initialized variables, the exception will appear.
     * @throws IOException If there exists IOException in the input or output stream of the parameter <code>serverSocket</code>,
     * this exception will appear.
     * @version beta1.0
     */
    public TransmissionController(Socket serverSocket, TransmissionListener listener) throws IOException, NullPointerException{
        if(serverSocket == null) throw new NullPointerException("The socket has not been initialized");
        if(listener == null) throw new NullPointerException("The listener has not been initialized");

        this.serverSocket = serverSocket;
        this.listener = listener;

        this.sender = new Sender(this.serverSocket.getOutputStream(), listener);
        this.receiver = new Receiver(this.serverSocket.getInputStream(), listener);

        this.receivingThread = new Thread(this.receiver);
        receivingThread.start();
    }

    /**
     *
     * @param message <b>Should be serialised!</b>
     *                The message you want to send.
     * Serializable.java
     * @version beta1.0
     */
    public <T extends Serializable> void send(T message){
        if(message == null){
            listener.alertError("NULL-Pointer");
            return;
        }
        new Thread(()->sender.send(message)).start();
    }

    /**
     *
     * @throws IOException When exception happens on closing the instances of receiver and sender,
     * the exception will appear.
     */
    @Override
    public void close() throws IOException {
        receiver.close();
        sender.close();
    }
}

class Sender implements Closeable{
    private ObjectOutputStream outputStream;
    private TransmissionListener listener;
    boolean flag;

    public Sender(OutputStream outputStream, TransmissionListener listener) {
        this.listener = listener;
        try {
            this.outputStream = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            listener.alertError("OutputInitError");
        }
        flag = true;
    }
    <T extends Serializable> void send(T data){
        //Ensure each thread can write the data to the stream one by one.
        synchronized (this) {
            if(!flag) {
                listener.alertError("WrongOutputStream");
                return;
            }
            try {
                outputStream.writeObject(data);
            } catch (IOException e) {
                e.printStackTrace();
                listener.alertError("WrongWrite");
                listener.onTransmissionError("WrongWrite", TransmissionListener.ErrorType.WRONG_WRITE);
                flag = false;
            }
        }
    }

    @Override
    public void close() throws IOException {
        this.outputStream.close();
    }
}

class Receiver<T extends Serializable> implements Runnable, Closeable{
    private Socket socket;
    private TransmissionListener listener;
    private ObjectInputStream objectInputStream;
    boolean flag;

    public Receiver(InputStream inputStream, TransmissionListener listener) throws IOException {
        try {
            this.objectInputStream = new ObjectInputStream(new BufferedInputStream(inputStream));
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("pass");
        this.listener = listener;
        flag = true;
        System.out.println("Receiver init over");
    }
    @Override
    public void run(){
        while(flag){
            try {
                T o = (T)objectInputStream.readObject();
                listener.onTransmissionProgress(o);
            }catch (IOException e){
                e.printStackTrace();
                listener.alertError("ReadError!");
                WebUtil.closeAll(objectInputStream);
                flag = false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                listener.alertError("ClassNotFound");

            }
        }
    }

    @Override
    public void close() throws IOException {
        this.objectInputStream.close();
    }
}

