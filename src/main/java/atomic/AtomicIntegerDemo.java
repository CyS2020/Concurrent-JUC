package atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: CyS2020
 * @date: 2021/2/6
 * 描述：演示AtomicInteger基本用法，对比费原子类的线程安全问题
 * 使用了原子类不需要加锁也可以保证线程安全问题
 */
public class AtomicIntegerDemo implements Runnable {

    private static final AtomicInteger atomicInteger = new AtomicInteger();

    private static volatile int basicCount = 0;

    public static void main(String[] args) throws InterruptedException {
        AtomicIntegerDemo runnable = new AtomicIntegerDemo();
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("原子类的结果：" + atomicInteger.get());
        System.out.println("普通变量的值：" + basicCount);
    }

    public void incrementAtomic() {
        atomicInteger.getAndAdd(-90);
    }

    public synchronized void incrementBasic() {
        basicCount++;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++){
            incrementAtomic();
            incrementBasic();
        }
    }
}
