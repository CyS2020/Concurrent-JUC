package future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author: CyS2020
 * @date: 2021/2/20
 * 描述：演示FutureTask用法
 */
public class FutureTaskDemo {

    public static void main(String[] args) {
        Task task = new Task();
        FutureTask<Integer> integerFutureTask = new FutureTask<>(task);
//        new Thread(integerFutureTask).start();
        ExecutorService service = Executors.newFixedThreadPool(10);
        service.submit(integerFutureTask);
        try {
            System.out.println("task运行结果" + integerFutureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        service.shutdown();
    }

    public static class Task implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            System.out.println("子线程正在计算");
            Thread.sleep(3000);
            int sum = 0;
            for (int i = 0; i < 100; i++){
                sum += i;
            }
            return sum;
        }
    }
}
