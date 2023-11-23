package web_tools;

import java.io.Serializable;

public interface TransmissionListener <T extends Serializable> {
    /**
     * The type of the error.
     * @version beta1.0
     */
    public enum ErrorType{
        NONE_POINTER, IO_EXCEPTION, WRONG_OUTPUT_STREAM, WRONG_WRITE, WRONG_READ, CLASS_NOT_FOUND
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
     * Todo: Method hasn't used; ErrorType hasn't declared.
     */
    public void onTransmissionError(String message, ErrorType errorType);

    /**
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
     * @param error
     */
    public void alertError(String error);
}
