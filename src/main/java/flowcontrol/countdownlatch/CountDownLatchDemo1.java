package flowcontrol.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: CyS2020
 * @date: 2021/2/18
 * 描述：工厂中，质检，5个工人检查，所有人认为通过才通过
 */
public class CountDownLatchDemo1 {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++){
            final int no = i + 1;
            Runnable runnable = () -> {
                try {
                    Thread.sleep((long) (Math.random() * 1000));
                    System.out.println("No." + no + "完成了检查");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    latch.countDown();
                }
            };
            executorService.submit(runnable);
        }
        System.out.println("等待五个人检查完......");
        latch.await();
        System.out.println("所有人检查完毕, 进入下一环节");
        executorService.shutdown();
    }
}