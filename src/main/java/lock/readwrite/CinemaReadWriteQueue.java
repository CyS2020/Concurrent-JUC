package lock.readwrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: CyS2020
 * @date: 2021/2/3
 * 描述：演示读写锁
 */
public class CinemaReadWriteQueue {

    private static final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(false);

    private static final ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();

    private static final ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    public static void main(String[] args) {
        new Thread(CinemaReadWriteQueue::write, "Thread1").start();
        new Thread(CinemaReadWriteQueue::read, "Thread2").start();
        new Thread(CinemaReadWriteQueue::read, "Thread3").start();
        new Thread(CinemaReadWriteQueue::write, "Thread4").start();
        new Thread(CinemaReadWriteQueue::read, "Thread5").start();

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
