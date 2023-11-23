package web_tools;

import java.io.Serial;
import java.io.Serializable;

public class MyData implements Serializable {
    /**
     * Self-version control.
     * To make the JVM know this serializable is serialized by ourselves with the only id,
     * and to make the JVM know the class is the same if used in different executable program.
     */
    @Serial
    private static final long serialVersionUID = 1L;
    private String data;
    private SubData d2;
    public MyData(String data){
        this.data = data;
        d2 = new SubData();
    }
    public String getData(){
        return data;
    }
}

class SubData implements Serializable{
    int data = 0;
}
