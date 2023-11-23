package web_tools;

import java.io.Serializable;

/**
 * A listener that can be used to listen to the transmission of the proxy class <code>TransmissionController</code>.
 * Use template to make the class more flexible, and easy to use.
 * @param <T> The class type of the message you want to receive.
 * @version beta1.1
 */
public interface TransmissionListener <T extends Serializable> {
    /**
     * The error type of the proxy class <code>TransmissionController</code>.
     * @version beta1.1
     */
    public enum ErrorType{
        /**
         * When the pointer of the parameter <code>serverSocket</code> in the constructor of the proxy class <code>TransmissionController</code>
         */
        NONE_POINTER,
        /**
         * When the parameter <code>listener</code> in the constructor of the proxy class <code>TransmissionController</code>
         */
        IO_EXCEPTION,
        /**
         * When the parameter <code>listener</code> in the constructor of the proxy class <code>TransmissionController</code>
         */
        WRONG_OUTPUT_STREAM,
        /**
         * When the parameter <code>listener</code> in the constructor of the proxy class <code>TransmissionController</code>
         */
        WRONG_WRITE,
        /**
         * When the parameter <code>listener</code> in the constructor of the proxy class <code>TransmissionController</code>
         */
        WRONG_READ,
        /**
         * When the parameter <code>listener</code> in the constructor of the proxy class <code>TransmissionController</code>
         */
        CLASS_NOT_FOUND
    }
    /**
     * TODO Method hasn't been used.
     */
    public void onTransmissionStart();

    /**
     * TODO Method hasn't been used.
     */
    public void onTransmissionEnd();

    /**
     * When an error happens in the proxy class <code>TransmissionController</code>,
     * this method will be called.
     * @param message The message from the proxy.
     * @param errorType ErrorType.
     * @version beta1.1
     */
    public void onTransmissionError(String message, ErrorType errorType);

    /**
     * <b>The most important method</b> in the interface <code>TransmissionListener</code>.
     * When the proxy class <code>TransmissionController</code> receives a message,
     * this method will be called.
     * @param messages The messages from the proxy.
     *                 <b>Should be serialised!</b>
     * @version beta1.1
     * @see java.io.Serializable
     */
    public void onTransmissionProgress(T messages);


    /**
     * A temp method similar to <code>onTransmissionError</code>.
     * @param error A string that contains the error message.
     */
    public void alertError(String error);
}
