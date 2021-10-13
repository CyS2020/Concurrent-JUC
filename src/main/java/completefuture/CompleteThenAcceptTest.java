package completefuture;

import java.util.concurrent.CompletableFuture;

import static completefuture.CompleteFutureTest.executorService;

/**
 * @author: CyS2020
 * @date: 2021/10/13
 * 描述：异步串行化执行
 */
public class CompleteThenAcceptTest {

    public static void main(String[] args) {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "执行运算");
            return 10 / 2;
        }, executorService).thenAccept(res ->
                System.out.println(Thread.currentThread().getName() + "上一步的运行结果是 " + res));
        System.out.println(future);

        executorService.shutdown();
    }
}
