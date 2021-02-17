package collections.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author: CyS2020
 * @date: 2021/2/17
 * 描述：生产者消费者的阻塞队列实现
 */
public class ArrayBlockingQueueDemo {


    public static void main(String[] args) {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(3);

        Interviewer r1 = new Interviewer(queue);
        Consumer r2 = new Consumer(queue);
        new Thread(r1).start();
        new Thread(r2).start();

    }

    public static class Interviewer implements Runnable {

        private final BlockingQueue<String> queue;

        public Interviewer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            System.out.println("10个候选人都来啦");
            for (int i = 0; i < 10; i++) {
                String candidate = "Candidate" + i;
                try {
                    queue.put(candidate);
                    System.out.println("安排好了" + candidate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                queue.put("stop");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Consumer implements Runnable {

        private final BlockingQueue<String> queue;

        public Consumer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String msg;
            try {
                while (!(msg = queue.take()).equals("stop")) {
                    System.out.println(msg + "到了");
                }
                System.out.println("所有候选人都结束了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
