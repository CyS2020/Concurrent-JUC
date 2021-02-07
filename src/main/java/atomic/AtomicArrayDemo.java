package atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author: CyS2020
 * @date: 2021/2/6
 * 描述：演示原子数组的使用方法
 */
public class AtomicArrayDemo {

    private static final AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(1000);

    public static void main(String[] args) throws InterruptedException {
        Incrementer incrementer = new Incrementer(atomicIntegerArray);
        Decrementer decrementer = new Decrementer(atomicIntegerArray);
        Thread[] threadsIncrementer = new Thread[100];
        Thread[] threadsDecrementer = new Thread[100];
        for (int i = 0; i < 100; i++){
            threadsDecrementer[i] = new Thread(decrementer);
            threadsIncrementer[i] = new Thread(incrementer);
            threadsDecrementer[i].start();
            threadsIncrementer[i].start();
        }
        for (int i = 0; i < 100; i++){
            threadsDecrementer[i].join();
            threadsIncrementer[i].join();
        }
        for (int i = 0; i < atomicIntegerArray.length(); i++){
            if (atomicIntegerArray.get(i) != 0){
                System.out.println("发现了错误" + i);
            }
            System.out.println(atomicIntegerArray.get(i));
        }
        System.out.println("运行结束");
    }

    public static class Decrementer implements Runnable{

        private final AtomicIntegerArray array;

        public Decrementer(AtomicIntegerArray array) {
            this.array = array;
        }

        @Override
        public void run() {
            for (int i = 0; i < array.length();i++){
                array.getAndDecrement(i);
            }
        }
    }

    public static class Incrementer implements Runnable{

        private final AtomicIntegerArray array;

        public Incrementer(AtomicIntegerArray array) {
            this.array = array;
        }

        @Override
        public void run() {
            for (int i = 0; i < array.length();i++){
                array.getAndIncrement(i);
            }
        }
    }
}
