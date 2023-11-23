import web_tools.MyData;
import web_tools.TransmissionController;
import web_tools.TransmissionListener;

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
            Channel moveChannel = new Channel(socket);
            moveChannel.ownID = webID;
            System.out.println("someone comes"+(listMap.size()+1));
            //Because we're the server, so I don't want to send the <code>serverMessage</code> to the client.
            moveChannel.controller.send(new MyData(MyData.STRING_INDEX,SEND_ID+Integer.toString(webID)));
            listMap.put(webID, moveChannel);
        }
    }
}

class Channel implements TransmissionListener<MyData> {
    int ownID;
    TransmissionController controller;
    public Channel(Socket clientSocket){
        try {
            controller = new TransmissionController(clientSocket, this);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        String message = data.getServerMessage();
        System.out.println("Server reseive the msg: "+message);
        //String info[] = message.split(MyServer.SPLIT_REG);

        Objects.requireNonNull(MyServer.listMap.get(Integer.parseInt(message))).controller.send(data);
        System.out.println("Server reseive the msg to: "+Integer.parseInt(message));
    }

    @Override
    public void alertError(String error) {
        System.out.println(error);
    }
}