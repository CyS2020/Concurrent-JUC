package flowcontrol.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: CyS2020
 * @date: 2021/2/18
 * 描述：模拟100米，跑步，5名选手准备好了，只等裁判员一声令下，所有人开始同时跑步
 */
public class CountDownLatchDemo2 {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch begin = new CountDownLatch(1);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            final int no = i + 1;
            Runnable runnable = () -> {
                System.out.println("No." + no + "准备完毕，等待发令枪");
                try {
                    begin.await();
                    System.out.println("No." + no + "开始跑步了");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            executorService.submit(runnable);
        }
        // 裁判员检查发令枪
        Thread.sleep(5000);
        System.out.println("发令枪响, 比赛开始");
        begin.countDown();
        executorService.shutdown();
    }
}
