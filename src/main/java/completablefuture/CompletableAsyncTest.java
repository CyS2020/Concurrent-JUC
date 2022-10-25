package completablefuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static completablefuture.CompletableFutureTest.executorService;

/**
 * @author: CyS2020
 * @date: 2021/10/14
 * 描述：同步与异步
 */
public class CompletableAsyncTest {

    public static void main(String[] args) throws InterruptedException {
        CompletableFuture.allOf();
        List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");

        System.out.println("以下是同步执行--默认执行");
        List<CompletableFuture<Void>> futures = list.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApply(CompletableAsyncTest::toUpperCase).thenAccept(CompletableAsyncTest::print))
                .collect(Collectors.toList());
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        Thread.sleep(7000);
        System.out.println("以下是异步执行--默认异步执行");
        futures = list.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApplyAsync(CompletableAsyncTest::toUpperCase).thenAcceptAsync(CompletableAsyncTest::print))
                .collect(Collectors.toList());
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        Thread.sleep(7000);
        System.out.println("以下是异步执行--自定义执行");
        futures = list.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApplyAsync(CompletableAsyncTest::toUpperCase, executorService).thenAcceptAsync(CompletableAsyncTest::print, executorService))
                .collect(Collectors.toList());
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        Thread.sleep(9000);

        executorService.shutdown();
    }

    public static String toUpperCase(String s) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "转换大写");
        return s.toUpperCase();
    }

    public static void print(String s) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "打印结果: " + s);
    }
}
