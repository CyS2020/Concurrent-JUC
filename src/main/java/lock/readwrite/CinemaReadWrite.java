package lock.readwrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: CyS2020
 * @date: 2021/2/3
 * 描述：演示读写锁，读锁可以一起加锁解锁，写锁要一个一个来
 */
public class CinemaReadWrite {

    private static final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    private static final ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();

    private static final ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    public static void main(String[] args) {
        new Thread(CinemaReadWrite::read, "Thread1").start();
        new Thread(CinemaReadWrite::read, "Thread2").start();
        new Thread(CinemaReadWrite::write, "Thread3").start();
        new Thread(CinemaReadWrite::write, "Thread4").start();

    }

    private static void read(){
        readLock.lock();
        try{
            System.out.println(Thread.currentThread().getName() + "得到了读锁");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放了读锁");
            readLock.unlock();
        }
    }

    private static void write(){
        writeLock.lock();
        try{
            System.out.println(Thread.currentThread().getName() + "得到了写锁，正在写入");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放了写锁");
            writeLock.unlock();
        }
    }
}
