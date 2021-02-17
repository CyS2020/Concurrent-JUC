package collections.concurrenthashmap;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: CyS2020
 * @date: 2021/2/17
 * 描述：组合操作并不保证线程安全
 */
public class OptionsNotSafe implements Runnable {

    private static final ConcurrentHashMap<String, Integer> scores = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {
        scores.put("小明", 0);
        Thread thread1 = new Thread(new OptionsNotSafe());
        Thread thread2 = new Thread(new OptionsNotSafe());
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(scores);
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            //synchronized (OptionsNotSafe.class){
            while (true){
                Integer score = scores.get("小明");
                Integer newScore = score + 1;
                boolean b = scores.replace("小明", score, newScore);
                if (b){
                    break;
                }
            }
            //scores.put("小明", newScore);
            //}
        }
    }
}
