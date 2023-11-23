import web_tools.MyData;
import web_tools.TransmissionController;
import web_tools.TransmissionListener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyServer{
    public static final char SEND_ID = 'a', RECEIVE_MESSAGE = 'b';
    public static String SPLIT_REG = "#";
    //
    //public static List<MoveChannel> list = new ArrayList<MoveChannel>();
    public static Map<Integer, Channel> listMap = new HashMap<>();
    public static int webID;
    public static void main(String[] args) throws IOException {
        webID  = 100;//
        System.out.println("start");
        ServerSocket moveServer = new ServerSocket(8999);
        while(true){
            webID ++;
            Socket socket = moveServer.accept();
            //System.out.println(socket.getRemoteSocketAddress());
            //
            Channel moveChannel = new Channel(socket);
            moveChannel.ownID = webID;
            System.out.println("someone comes"+(listMap.size()+1));
            //
            //new DataOutputStream(socket.getOutputStream()).writeUTF(SEND_ID+Integer.toString(webID));
            moveChannel.controller.send(new MyData(SEND_ID+Integer.toString(webID)));
            listMap.put(webID, moveChannel);
            //list.add(moveChannel);
            //
//            new Thread(moveChannel).start();
        }
    }
}

class Channel implements TransmissionListener {
    int ownID;
    TransmissionController controller;
    public Channel(Socket clientSocket){
        try {
            controller = new TransmissionController(clientSocket, this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void webAction(String message) {
        String info[] = message.split(MyServer.SPLIT_REG);
        Objects.requireNonNull(MyServer.listMap.get(Integer.parseInt(info[0]))).controller.send(new MyData(info[1]));
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
    public void onTransmissionProgress(Object messages) {
        String s = ((MyData) messages).getData();
        webAction(s);
    }

    @Override
    public void alertError(String error) {

    }
}