package practice;

import lombok.SneakyThrows;

/**
 * @author: CyS2020
 * @date: 2021/2/22
 * 描述：必定发生死锁的情况
 */
public class DeadLock implements Runnable {

    private final int flag;

    private static final Object obj1 = new Object();

    private static final Object obj2 = new Object();

    public DeadLock(int flag) {
        this.flag = flag;
    }

    public static void main(String[] args) {
        new Thread(new DeadLock(0)).start();
        new Thread(new DeadLock(1)).start();
    }

    @SneakyThrows
    @Override
    public void run() {
        if (flag == 1) {
            synchronized (obj1) {
                System.out.println(Thread.currentThread().getName() + "拿到obj1");
                Thread.sleep(100);
                synchronized (obj2) {
                    System.out.println(Thread.currentThread().getName() + "拿到obj2");
                    System.out.println("成功运行");
                }
            }
        }
        if (flag == 0) {
            synchronized (obj2) {
                System.out.println(Thread.currentThread().getName() + "拿到obj2");
                Thread.sleep(100);
                synchronized (obj1) {
                    System.out.println(Thread.currentThread().getName() + "拿到obj1");
                    System.out.println("成功运行");
                }
            }
        }
    }

}
