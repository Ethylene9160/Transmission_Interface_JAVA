package web_tools;

import java.io.Closeable;
import java.io.IOException;

public class WebUtil {
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
