package flowcontrol.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: CyS2020
 * @date: 2021/2/18
 * 描述：演示Condition的基本用法
 */
public class ConditionDemo1 {

    private final ReentrantLock lock = new ReentrantLock();

    private final Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        ConditionDemo1 conditionDemo1 = new ConditionDemo1();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                conditionDemo1.method2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        conditionDemo1.method1();
    }

    void method1() throws InterruptedException {
        lock.lock();
        try{
            System.out.println("条件不满足，开始await");
            condition.await();
            System.out.println("条件满足了，开始执行后续的任务");
        }finally {
            lock.unlock();
        }
    }

    void method2(){
        lock.lock();
        try{
            System.out.println("准别工作完成");
            condition.signal();
        }finally {
            lock.unlock();
        }
    }
}
