package whist;

import java.awt.*;

public class FontSingleton {
    private volatile static FontSingleton uniqueInstance;
    private Font bigFont;
    // TODO: Read font config from peoperties
    private FontSingleton() {
        this.bigFont = new Font("Serif", Font.BOLD, 36);
    }
    public Font getBigFont() {
        return bigFont;
    }

    public static FontSingleton getInstance() {
        if (uniqueInstance == null) {
            synchronized (FontSingleton.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new FontSingleton();
                }
            }
        }
        return uniqueInstance;
    }
}
