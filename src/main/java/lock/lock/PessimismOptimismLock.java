package lock.lock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: CyS2020
 * @date: 2021/2/2
 * 描述：悲观锁与乐观锁
 */
public class PessimismOptimismLock {

    private int a;

    public static void main(String[] args) {
        // 乐观锁
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.incrementAndGet();
    }

    // 悲观锁
    public synchronized void testMethod(){
        a++;
    }
}
