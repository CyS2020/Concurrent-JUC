package lock.readwrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: CyS2020
 * @date: 2021/2/3
 * 描述：写锁降级为读锁，但是读锁不能升级为写锁--造成阻塞
 */
public class Upgrading {

    private static final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(false);

    private static final ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();

    private static final ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("降级是可以的");
        Thread thread1 = new Thread(Upgrading::writeDegrading, "Thread1");
        thread1.start();
        thread1.join();
        System.out.println("---");
        System.out.println("升级是不行的");
        Thread thread2 = new Thread(Upgrading::readUpgrading, "Thread2");
        thread2.start();


    }

    private static void readUpgrading(){
        readLock.lock();
        try{
            System.out.println(Thread.currentThread().getName() + "得到了读锁");
            Thread.sleep(1000);
            System.out.println("升级会带来阻塞");
            writeLock.lock();
            System.out.println(Thread.currentThread().getName() + "升级成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放了读锁");
            readLock.unlock();
        }
    }

    private static void writeDegrading(){
        writeLock.lock();
        try{
            System.out.println(Thread.currentThread().getName() + "得到了写锁，正在写入");
            Thread.sleep(1000);
            readLock.lock();
            System.out.println("在不释放写锁的情况下，直接获取读锁，成功降级");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
            System.out.println(Thread.currentThread().getName() + "释放了写锁");
            writeLock.unlock();
        }
    }
}
