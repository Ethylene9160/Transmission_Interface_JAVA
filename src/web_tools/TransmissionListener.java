package web_tools;

public interface TransmissionListener {
    /**
     * @TODO Method hasn't been used.
     */
    public void onTransmissionStart();

    /**
     * @TODO Method hasn't been used.
     */
    public void onTransmissionEnd();

    /**
     * When an error happens in the proxy class <code>TransmissionController</code>,
     * this method will be called.
     * @param message The message from the proxy.
     * @param errorType ErrorType.
     * @Todo: Method hasn't used; ErrorType hasn't declared.
     */
    public void onTransmissionError(String message, int errorType);

    /**
     * When receving an message in the proxy,
     * this method will be called.
     * @param messages The data you'll receive. That is the message sent from another socket.
     * @version beta1.0
     */
    public void onTransmissionProgress(Object messages);

    /**
     * A temp method similar to <code>onTransmissionError</code>.
     * @param error
     */
    public void alertError(String error);
}
