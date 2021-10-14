package completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static completablefuture.CompletableFutureTest.executorService;

/**
 * @author: CyS2020
 * @date: 2021/10/13
 * 描述：异步编排测试
 * 实际生产不使用该线程池，使用原生线程池自己创建
 */
public class CompletableWhenCompleteTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "执行运算");
            return 10 / 0;
        }, executorService).whenComplete((res, exception) -> {
            System.out.println(Thread.currentThread().getName() + "上一步运行结果 " + res);
            System.out.println(Thread.currentThread().getName() + "上一步运行异常 " + exception);
        }).whenComplete((res, exception) -> {
            System.out.println(Thread.currentThread().getName() + "上一步运行结果 " + res);
            System.out.println(Thread.currentThread().getName() + "上一步运行异常 " + exception);
        }).exceptionally(throwable -> 10);

        System.out.println(future1.get());

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "执行运算");
            return 10 / 2;
        }, executorService).whenCompleteAsync((res, exception) -> {
            System.out.println(Thread.currentThread().getName() + "上一步运行结果 " + res);
            System.out.println(Thread.currentThread().getName() + "上一步运行异常 " + exception);
        }, executorService).whenCompleteAsync((res, exception) -> {
            System.out.println(Thread.currentThread().getName() + "上一步运行结果 " + res);
            System.out.println(Thread.currentThread().getName() + "上一步运行异常 " + exception);
        }, executorService).handleAsync((res, exception) -> {
            System.out.println(Thread.currentThread().getName() + "处理 " + res);
            if (res != null) {
                return res;
            }
            return 10;
        }, executorService);

        System.out.println(future2.get());

        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "执行运算");
            return 10 / 0;
        }, executorService).whenComplete((res, exception) -> {
            System.out.println(Thread.currentThread().getName() + "上一步运行结果 " + res);
            System.out.println(Thread.currentThread().getName() + "上一步运行异常 " + exception);
        }).whenComplete((res, exception) -> {
            System.out.println(Thread.currentThread().getName() + "上一步运行结果 " + res);
            System.out.println(Thread.currentThread().getName() + "上一步运行异常 " + exception);
        }).exceptionally(throwable -> 10);

        System.out.println(future3.get());

        executorService.shutdown();
    }
}
