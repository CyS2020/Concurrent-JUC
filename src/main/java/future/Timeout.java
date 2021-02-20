package future;

import lombok.Data;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author: CyS2020
 * @date: 2021/2/20
 * 描述：演示get的超时方法，需要注意超时后处理，调用future.cancel()
 * 演示cancel传入true和false的区别，代表是否中断正在执行的任务
 */
public class Timeout {

    private static final Ad DEFAULT_AD = new Ad("无网络的时候的默认广告");

    private static final ExecutorService exec = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        Timeout timeout = new Timeout();
        timeout.printAd();
    }

    public void printAd(){
        Future<Ad> f = exec.submit(new FetchAdTask());
        Ad ad;
        try {
            ad = f.get(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            ad = new Ad("中断时候的默认广告");
        } catch (ExecutionException e) {
            ad = new Ad("异常时候的默认广告");
        } catch (TimeoutException e) {
            ad = new Ad("超时时候的默认广告");
            System.out.println("超时，未获取到广告");
            boolean cancel = f.cancel(false);
            System.out.println("cancel结果：" + cancel);
        }
        exec.shutdown();
        System.out.println(ad);
    }

    @Data
    public static class Ad{

        private String name;

        public Ad(String name) {
            this.name = name;
        }
    }

    public static class FetchAdTask implements Callable<Ad>{

        @Override
        public Ad call() {
            try{
                Thread.sleep(3000);
            }catch (InterruptedException e){
                System.out.println("sleep期间被中断了");
                return new Ad("被中断时候的默认广告");
            }
            return new Ad("旅游订票哪家强？找某程");
        }
    }
}
