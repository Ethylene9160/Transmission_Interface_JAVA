package web_tools;

import java.io.Closeable;
import java.io.IOException;

public class WebUtil {
    /**
     * Close all the instances of Closeable you pass.
     * @param cs The instances of Closeable you want to close.
     */
    public static void closeAll(Closeable...cs)  {
        for (Closeable c : cs) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
