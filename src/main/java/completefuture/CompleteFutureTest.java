package completefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: CyS2020
 * @date: 2021/10/13
 * 描述：演示CompleteFuture
 * 实际生产不使用该线程池，使用原生线程池自己创建
 */
public class CompleteFutureTest {

    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() ->
                System.out.println("运行异步任务" + Thread.currentThread().getName()), executorService);
        Void res1 = future1.get();
        System.out.println(res1);

        Thread.sleep(1000);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("运行异步任务" + Thread.currentThread().getName());
            return "返回结果";
        }, executorService);
        String res2 = future2.get();
        System.out.println(res2);

        executorService.shutdown();
    }
}