package completefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static completefuture.CompleteFutureTest.executorService;

/**
 * @author: CyS2020
 * @date: 2021/10/13
 * 描述：任务组合
 */
public class CompleteCombineTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "执行运算");
            return 10 / 2;
        }, executorService);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "执行字符串");
            return "hello world";
        }, executorService);

        CompletableFuture<Void> run = future1.runAfterBothAsync(future2, () ->
                System.out.println(Thread.currentThread().getName() + "执行任务3"), executorService);

        CompletableFuture<Void> accept = future1.thenAcceptBothAsync(future2, (f1, f2) -> {
            System.out.println(Thread.currentThread().getName() + "运算结果 " + f1);
            System.out.println(Thread.currentThread().getName() + "字符串 " + f2);
            System.out.println(Thread.currentThread().getName() + "执行任务3");
        }, executorService);

        CompletableFuture<String> combine = future1.thenCombineAsync(future2, (f1, f2) -> {
            System.out.println(Thread.currentThread().getName() + "运算结果 " + f1);
            System.out.println(Thread.currentThread().getName() + "字符串 " + f2);
            System.out.println(Thread.currentThread().getName() + "执行任务3");
            return f1 + " " + f2;
        }, executorService);

        System.out.println(combine.get());

        executorService.shutdown();
    }
}
