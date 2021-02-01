package lock.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: CyS2020
 * @date: 2021/2/1
 * 描述：演示等待锁的过程可能被中断
 */
public class LockIncorruptibly implements Runnable {

    private Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        LockIncorruptibly lockIncorruptibly = new LockIncorruptibly();
        Thread thread0 = new Thread(lockIncorruptibly);
        Thread thread1 = new Thread(lockIncorruptibly);
        thread0.start();
        thread1.start();
        Thread.sleep(2000);
        // 打断0则线程睡眠期间被打断--thread0有锁
        thread0.interrupt();
        // 打断1则获取锁期间被打断--thread1无锁
        thread1.interrupt();
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "尝试获取锁");
        try {
            lock.lockInterruptibly();
            try {
                System.out.println(Thread.currentThread().getName() + "获取到了锁");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "睡眠期间被中断了");
            } finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + "释放了锁");
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + "等锁期间被中断");
        }
    }
}
