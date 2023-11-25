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
         * When the pointer of the parameter <code>data</code>  you transmit to the method <code>send</code>
         * in the proxy class <code>TransmissionController</code>
         * has not been initialized, this error will appear.
         */
        NULL_POINTER,
        /**
         * When unknown <code>IOException</code> happens in the proxy class <code>TransmissionController</code>,
         * this error type will appear.
         */
        IO_EXCEPTION,
        /**
         * When something wrong happens in the output stream when writing the data,
         * this error type will appear.
         * This mostly happens when there exists <b>un-serialised sub-instance</b> in your <code>data</code> you want to send.
         */
        WRONG_OUTPUT_STREAM,
        /**
         * When the params you pass has not been seriallized, this error type will appear.
         */
        WRONG_SERIALIZABLE,
        /**
         * When something wrong happens in the input stream when reading the data in the proxy instance <code>TransmissionController</code>,
         * this error type will appear.
         */
        WRONG_INPUT_STREAM,
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
    @Deprecated
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
    @Deprecated
    public void alertError(String error);
}
