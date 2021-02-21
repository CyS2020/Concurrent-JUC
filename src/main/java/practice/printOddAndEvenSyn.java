package practice;

/**
 * @author: CyS2020
 * @date: 2021/2/21
 * 描述：双线程交替打印奇偶数，会出现A线程一直获取到锁，但是if条件不满足无法打印，直到B线程获取锁并更新count值
 */
public class printOddAndEvenSyn {

    private static int count = 0;

    private final static Object obj = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            while (count < 100) {
                synchronized (obj) {
                    if ((count & 1) == 0) {
                        System.out.println(Thread.currentThread().getName() + " : " + count++);
                    }
                }
            }
        }, "偶数").start();

        new Thread(() -> {
            while (count < 100) {
                synchronized (obj) {
                    if ((count & 1) == 1) {
                        System.out.println(Thread.currentThread().getName() + " : " + count++);
                    }
                }
            }
        }, "奇数").start();
    }
}
