package web_tools;

import java.awt.*;
import java.io.File;
import java.io.Serial;
import java.io.Serializable;

/**
 * A class to store the data you want to send.
 * @version beta1.0
 * @see java.io.Serializable
 * @see java.io.Serial
 *
 */
public class MyData implements Serializable {
    /**
     * Self-version control.
     * To make the JVM know this serializable is serialized by ourselves with the only id,
     * and to make the JVM know the class is the same if used in different executable program.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public static final int STRING_INDEX = 0, FILE_INDEX = 1, IMAGE_INDEX = 2;//, AUDIO_INDEX = 3, VIDEO_INDEX = 4;
    private Object data;
    private int data_Index;
    private String serverMessage;

    @Deprecated
    public<T extends Serializable> MyData(int type, T data){
        this(type, "", data);
    }

    /**
     * Constructor of the class <code>MyData</code>.
     * This is a constructor to store the data you want to send. Including the type of the data, the message you want to send to the server,
     * and the data you want to send.
     * @param type The type of the data you want to send.
     * @param serverMessage The message you want to send to the server. The server will read this message.
     *                      For example, if you want to send a message to the server(if the operation of the server have sth to do with this param),
     *                      you can use this parameter to send the message.
     * @param data The data you want to send.
     * @param <T> The class type of the data you want to send.
     */
    public<T extends Serializable> MyData(int type, String serverMessage, T data){
        if(data == null || serverMessage == null) throw new NullPointerException("The data is null.");

        if(type < STRING_INDEX || type > IMAGE_INDEX) throw new IllegalArgumentException("The type is not in the range of supported types.");

//        if(type == FILE_INDEX && !(data instanceof byte[])) throw new IllegalArgumentException("The data is not a file.");
//        if(type == IMAGE_INDEX && !(data instanceof Image)) throw new IllegalArgumentException("The data is not an image.");

        this.data = data;
        this.serverMessage = serverMessage;
        this.data_Index = type;
    }

    public int getType(){
        return data_Index;
    }

    public String getServerMessage(){
        return serverMessage;
    }

    /**
     * Get the data you've stored in the <code>MyData</code> class.
     * @return The data you want to get.
     * @param <T> The type of the data you want to get.
     * @throws IllegalArgumentException If the data is not serializable, this exception will appear.
     * @version beta1.0
     * @see java.io.Serializable
     * @see java.io.Serial
     * @see java.lang.IllegalArgumentException
     *
     */
    public <T extends Serializable> T getData()throws IllegalArgumentException{
        if(!(data instanceof Serializable)) {
            throw new IllegalArgumentException("The data is not serializable.");
        }
        return (T)this.data;
    }
}
