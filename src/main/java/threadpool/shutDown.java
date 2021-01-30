package threadpool;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author: CyS2020
 * @date: 2021/1/30
 * 描述：演示关闭线程池
 */
public class shutDown {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++){
            executorService.execute(new ShutDownTask());
        }
        Thread.sleep(1500);
        List<Runnable> runnableList = executorService.shutdownNow();
//        executorService.shutdown();
//        boolean b = executorService.awaitTermination(7L, TimeUnit.SECONDS);
//        System.out.println(b);
//        System.out.println(executorService.isShutdown());
//        executorService.shutdown();
//        System.out.println(executorService.isShutdown());
//        Thread.sleep(10000);
//        System.out.println(executorService.isTerminated());
//        executorService.execute(new ShutDownTask());
    }

    public static class ShutDownTask implements Runnable{

        @Override
        public void run() {
            try {
                Thread.sleep(500);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "被中断了");
            }
        }
    }
}
