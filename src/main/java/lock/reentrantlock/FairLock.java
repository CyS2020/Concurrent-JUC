package lock.reentrantlock;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: CyS2020
 * @date: 2021/2/2
 * 描述：演示公平和不公平
 */
public class FairLock {

    public static void main(String[] args) throws InterruptedException {
        PrintQueue printQueue = new PrintQueue();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new Job(printQueue));
        }
        for (int i = 0; i < 10; i++) {
            threads[i].start();
            Thread.sleep(100);
        }
    }

    public static class Job implements Runnable {

        PrintQueue printQueue;

        public Job(PrintQueue printQueue) {
            this.printQueue = printQueue;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "开始打印");
            printQueue.printJob(new Object());
            System.out.println(Thread.currentThread().getName() + "打印完毕");
        }
    }

    public static class PrintQueue {

        // 调整参数可以设置为公平锁
        private final Lock queueLock = new ReentrantLock(false);

        public void printJob(Object document) {
            queueLock.lock();
            try {
                int duration = new Random().nextInt(10) + 1;
                System.out.println(Thread.currentThread().getName() + "正在打印，需要" + duration + "秒");
                Thread.sleep(duration * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                queueLock.unlock();
            }

            queueLock.lock();
            try {
                int duration = new Random().nextInt(10) + 1;
                System.out.println(Thread.currentThread().getName() + "正在打印，需要" + duration + "秒");
                Thread.sleep(duration * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                queueLock.unlock();
            }
        }
    }
}
