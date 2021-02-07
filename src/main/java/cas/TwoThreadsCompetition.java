package cas;

/**
 * @author: CyS2020
 * @date: 2021/2/7
 * 描述：多线程运行CAS操作
 */
public class TwoThreadsCompetition implements Runnable{

    private volatile int value;

    public static void main(String[] args) throws InterruptedException {
        TwoThreadsCompetition runnable = new TwoThreadsCompetition();
        runnable.value = 0;
        Thread thread1 = new Thread(runnable, "Thread1");
        Thread thread2 = new Thread(runnable, "Thread2");
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(runnable.value);
    }

    public synchronized int compareAndSwap(int expectedValue, int newValue){
        int oldValue = value;
        if (oldValue == expectedValue){
            value = newValue;
        }
        return oldValue;
    }

    @Override
    public void run() {
        compareAndSwap(0, 1);
    }
}
