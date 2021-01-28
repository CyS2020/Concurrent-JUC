package threadpool;

/**
 * @author: CyS2020
 * @date: 2021/1/28
 * 描述：手动创建多线程
 */
public class EveryTaskOneThread {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++){
            Thread thread = new Thread(new Task());
            thread.start();
        }
    }

    public static class Task implements Runnable{

        @Override
        public void run() {
            System.out.println("执行子任务");
        }
    }
}
