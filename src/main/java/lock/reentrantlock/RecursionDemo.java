package lock.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: CyS2020
 * @date: 2021/2/2
 * 描述：演示可重入，递归调用方法
 */
public class RecursionDemo {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        accessResource();
    }

    private static void accessResource(){
        lock.lock();
        try{
            System.out.println("已经对资源进行了处理");
            if (lock.getHoldCount() < 5){
                System.out.println(lock.getHoldCount());
                accessResource();
                System.out.println(lock.getHoldCount());
            }
        }finally {
            lock.unlock();
        }
    }
}
