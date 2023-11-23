package web_tools;

public interface TransmissionListener<T> {
    public void onTransmissionStart();
    public void onTransmissionEnd();
    public void onTransmissionError();
    public void onTransmissionProgress(T messages);
    public void allertError(String error);
}
