package threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: CyS2020
 * @date: 2021/1/29
 * 描述：演示 可缓存线程池
 */
public class CacheThreadPool {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 1000; i++){
             executorService.execute(new FixedThreadPoolTest.Task());
        }
    }
}
