package future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author: CyS2020
 * @date: 2021/2/20
 * 描述：演示批量提交任务时，用List批量接收结果
 */
public class MultiFutures {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(20);
        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Future<Integer> future = service.submit(new OneFuture.CallableTask());
            futures.add(future);
        }
        Thread.sleep(5000);
        futures.forEach(future -> {
            try {
                Integer integer = future.get();
                System.out.println(integer);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        service.shutdown();
    }
}
