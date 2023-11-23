package web_tools;

import java.io.*;
import java.net.Socket;


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
     * @seealso TransmissionListener.java
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
        //Thread.sleep(100);
        this.sender = new Sender(this.serverSocket.getOutputStream(), listener);
        System.out.println("Sender ok");
        this.receiver = new Receiver(this.serverSocket.getInputStream(), listener);
        System.out.println("Receiver ok");
        this.receivingThread = new Thread(this.receiver);
        receivingThread.start();
    }

    /**
     *
     * @param message <b>Should be serialised!</b>
     *                The message you want to send.
     * @seealso: Serializable.java
     * @version beta1.0
     */
    public void send(Object message){
        if(!(message instanceof Serializable)){
            listener.alertError("NONE-SERILIZABLE");
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
    private Socket socket;
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
    void send(Object data){
        if(!flag) {
            listener.alertError("WrongOutputStream");
            return;
        }

        try{
            outputStream.writeObject(data);
        }catch (IOException e){
            e.printStackTrace();
            listener.alertError("WrongWrite");

            flag = false;
        }
    }

    private void send(byte[] bytes){
        try {
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            //throw new RuntimeException(e);
            listener.alertError("todo");
        }
    }

    @Override
    public void close() throws IOException {
        this.outputStream.close();
    }
}

class Receiver implements Runnable, Closeable{
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
                Object o = objectInputStream.readObject();
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

