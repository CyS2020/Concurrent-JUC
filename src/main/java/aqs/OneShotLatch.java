package aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author: CyS2020
 * @date: 2021/2/18
 * 描述：利用AQS实现一个自己的Latch门闩--线程协作器
 */
public class OneShotLatch {

    private final Sync sync = new Sync();

    public static void main(String[] args) throws InterruptedException {
        OneShotLatch oneShotLatch = new OneShotLatch();
        for (int i = 0; i < 10; i++){
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "尝试获取latch, 获取失败就等待");
                oneShotLatch.await();
                System.out.println(Thread.currentThread().getName() + "开闸放行，继续运行");
            }).start();
        }
        Thread.sleep(5000);
        oneShotLatch.signal();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "尝试获取latch, 获取失败就等待");
            oneShotLatch.await();
            System.out.println(Thread.currentThread().getName() + "开闸放行，继续运行");
        }).start();
    }

    public void await() {
        sync.acquireShared(0);
    }

    public void signal() {
        sync.releaseShared(0);
    }

    private class Sync extends AbstractQueuedSynchronizer {

        @Override
        protected int tryAcquireShared(int arg) {
            return (getState() == 1) ? 1 : -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            setState(1);
            return true;
        }
    }
}
