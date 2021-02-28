package practice;

/**
 * @author: CyS2020
 * @date: 2021/2/28
 * 单例模式特征：两静态 + 一私有
 * 描述：饿汉式
 */
public class Singleton1 {

    private static final Singleton1 INSTANCE = new Singleton1();

    private Singleton1() {

    }

    public static Singleton1 getInstance() {
        return INSTANCE;
    }
}
