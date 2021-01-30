package threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: CyS2020
 * @date: 2021/1/29
 * 描述：演示定时线程任务
 */
public class ScheduledThreadPoolTest {

    public static void main(String[] args) {
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(10);
        //threadPool.schedule(new FixedThreadPoolTest.Task(), 5, TimeUnit.SECONDS);
        threadPool.scheduleAtFixedRate(new FixedThreadPoolTest.Task(), 1, 3, TimeUnit.SECONDS);
    }
}
