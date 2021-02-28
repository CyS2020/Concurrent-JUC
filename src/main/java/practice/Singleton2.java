package practice;

/**
 * @author: CyS2020
 * @date: 2021/2/28
 * 描述：懒汉式-volatile + 双重检查
 */
public class Singleton2 {

    private volatile static Singleton2 instance;

    private Singleton2() {

    }

    public static Singleton2 getInstance() {
        if (instance == null) {
            synchronized (Singleton2.class) {
                if (instance == null) {
                    instance = new Singleton2();
                }
            }
        }
        return instance;
    }
}
