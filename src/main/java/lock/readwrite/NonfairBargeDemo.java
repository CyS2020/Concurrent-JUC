package lock.readwrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: CyS2020
 * @date: 2021/2/3
 * 描述：演示非公平锁的读锁在等待队列头节点为尝试获取读锁时可以插队，头结点为尝试获取写锁时是不允许插队
 */
public class NonfairBargeDemo {

    private static final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(false);

    private static final ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();

    private static final ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    public static void main(String[] args) {
        new Thread(NonfairBargeDemo::write, "Thread1").start();
        new Thread(NonfairBargeDemo::read, "Thread2").start();
        new Thread(NonfairBargeDemo::read, "Thread3").start();
        new Thread(NonfairBargeDemo::write, "Thread4").start();
        new Thread(NonfairBargeDemo::read, "Thread5").start();
        new Thread(() -> {
            Thread[] threads = new Thread[1000];
            for (int i = 0; i < 1000; i++){
                threads[i] = new Thread(NonfairBargeDemo::read, "子线程创建" + i);
            }
            for (int i = 0; i < 1000; i++){
                threads[i].start();
            }
        }).start();
    }

    private static void read(){
        System.out.println(Thread.currentThread().getName() + "开始尝试获取读锁");
        readLock.lock();
        try{
            System.out.println(Thread.currentThread().getName() + "得到读锁，正在读取");
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放读锁");
            readLock.unlock();
        }
    }

    private static void write(){
        System.out.println(Thread.currentThread().getName() + "开始尝试获取写锁");
        writeLock.lock();
        try{
            System.out.println(Thread.currentThread().getName() + "得到写锁，正在写入");
            Thread.sleep(40);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放写锁");
            writeLock.unlock();
        }
    }
}
