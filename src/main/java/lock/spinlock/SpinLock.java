package lock.spinlock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: CyS2020
 * @date: 2021/2/4
 * 描述：自旋锁
 */
public class SpinLock {

    private final AtomicReference<Thread> sign = new AtomicReference<>();

    public static void main(String[] args) {
        SpinLock spinLock = new SpinLock();
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName() + "尝试获取自旋锁");
            spinLock.lock();
            System.out.println(Thread.currentThread().getName() + "获取到自旋锁");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                spinLock.unlock();
                System.out.println(Thread.currentThread().getName() + "释放了自旋锁");
            }
        };

        new Thread(runnable).start();
        new Thread(runnable).start();
    }

    public void lock(){
        Thread current = Thread.currentThread();
        while (!sign.compareAndSet(null, current)){
            System.out.println(Thread.currentThread().getName() + "自旋获取失败，再次尝试");
        }
    }

    public void unlock(){
        Thread current = Thread.currentThread();
        sign.compareAndSet(current, null);
    }

}
