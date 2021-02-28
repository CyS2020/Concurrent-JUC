package practice;

/**
 * @author: CyS2020
 * @date: 2021/2/28
 * 描述：静态内部类
 */
public class Singleton3 {

    private Singleton3() {

    }

    public static Singleton3 getInstance() {
        return SingletonInstance.INSTANCE;
    }

    private static class SingletonInstance {
        private static final Singleton3 INSTANCE = new Singleton3();
    }
}
