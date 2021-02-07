package atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author: CyS2020
 * @date: 2021/2/6
 * 描述：演示AtomicIntegerFieldUpdaterDemo
 * 注意点：可见范围不能是private、不支持static变量
 */
public class AtomicIntegerFieldUpdaterDemo implements Runnable{

    private static  Candidate tom;

    private static  Candidate peter;

    public static final AtomicIntegerFieldUpdater<Candidate> scoreUpdater = AtomicIntegerFieldUpdater.newUpdater(Candidate.class, "score");

    public static void main(String[] args) throws InterruptedException {
        tom = new Candidate();
        peter = new Candidate();
        AtomicIntegerFieldUpdaterDemo runnable = new AtomicIntegerFieldUpdaterDemo();
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("普通变量" + peter.score);
        System.out.println("升级变量" + tom.score);
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++){
            peter.score++;
            scoreUpdater.getAndIncrement(tom);
        }
    }

    public static class Candidate{

        public volatile int score;
    }
}
