package threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: CyS2020
 * @date: 2021/1/29
 * 描述：演示只有一个线程的线程池
 */
public class SingleThreadExecutor {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 1000; i++){
            executorService.submit(new FixedThreadPoolTest.Task());
        }
    }
}
