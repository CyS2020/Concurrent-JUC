package practice;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author: CyS2020
 * @date: 2021/2/21
 * 描述：生产者消费者，BlockingQueue
 */
public class ProducerAndConsumerDemo2 {

    public static void main(String[] args) {
        BlockingQueue<Long> storage = new ArrayBlockingQueue<>(10);
        new Thread(new Producer(storage)).start();
        new Thread(new Consumer(storage)).start();
    }

    public static class Producer implements Runnable {

        private final BlockingQueue<Long> storage;

        public Producer(BlockingQueue<Long> storage) {
            this.storage = storage;
        }

        @Override
        public void run() {
            produce();
        }

        private void produce() {
            while (true) {
                try {
                    long l = (long) (Math.random() * 1000);
                    storage.put(l);
                    System.out.println("生产了资源" + l + "资源个数" + storage.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer implements Runnable {

        private final BlockingQueue<Long> storage;

        public Consumer(BlockingQueue<Long> storage) {
            this.storage = storage;
        }

        @Override
        public void run() {
            consume();
        }

        private void consume() {
            while (true) {
                try {
                    long l = storage.take();
                    System.out.println("消费了资源" + l + "资源个数" + storage.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
