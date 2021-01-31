package threadlocal;

import jdk.nashorn.internal.ir.CallNode;

/**
 * @author: CyS2020
 * @date: 2021/1/31
 * 描述：演示ThreadLocal出现空指针异常情况
 */
public class ThreadLocalNPE {

    ThreadLocal<Long> longThreadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        ThreadLocalNPE threadLocalNPE = new ThreadLocalNPE();
        System.out.println(threadLocalNPE.get());
        Thread thread1 = new Thread(() -> {
            threadLocalNPE.set();
            System.out.println(threadLocalNPE.get());
        });
        thread1.start();
    }

    // 将返回值类型改为long会出现空指针异常
    public Long get(){
        return longThreadLocal.get();
    }

    public void set(){
        longThreadLocal.set(Thread.currentThread().getId());
    }
}
