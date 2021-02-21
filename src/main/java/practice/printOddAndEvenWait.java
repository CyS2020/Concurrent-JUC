package practice;

/**
 * @author: CyS2020
 * @date: 2021/2/21
 * 描述：双线程交替打印奇偶数，线程之间互相通知另一方
 */
public class printOddAndEvenWait {

    private static int count = 0;

    private static final Object obj = new Object();

    public static void main(String[] args) {
        new Thread(new TurningRunner()).start();
        new Thread(new TurningRunner()).start();
    }

    public static class TurningRunner implements Runnable {

        @Override
        public void run() {
            while (count < 100) {
                synchronized (obj) {
                    System.out.println(Thread.currentThread().getName() + " : " + count++);
                    obj.notify();
                    if (count < 100) {
                        try {
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
