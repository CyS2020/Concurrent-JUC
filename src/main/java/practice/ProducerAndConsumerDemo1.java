package practice;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author: CyS2020
 * @date: 2021/2/21
 * 描述：生产者消费者，wait() - notify()
 */
public class ProducerAndConsumerDemo1 {

    public static void main(String[] args) {
        int size = 10;
        Queue<Long> storage = new LinkedList<>();
        new Thread(new Producer(size, storage)).start();
        new Thread(new Consumer(storage)).start();
    }

    public static class Producer implements Runnable {

        private final int size;

        private final Queue<Long> storage;

        public Producer(int size, Queue<Long> storage) {
            this.size = size;
            this.storage = storage;
        }

        @Override
        public void run() {
            produce();
        }

        private void produce() {
            while (true) {
                synchronized (storage) {
                    while (storage.size() == size) {
                        try {
                            storage.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    long l = (long) (Math.random() * 1000);
                    storage.offer(l);
                    System.out.println("生产了资源" + l + "资源个数" + storage.size());
                    storage.notifyAll();
                }
            }
        }
    }

    public static class Consumer implements Runnable {

        private final Queue<Long> storage;

        public Consumer(Queue<Long> storage) {
            this.storage = storage;
        }

        @Override
        public void run() {
            consume();
        }

        private void consume() {
            while (true) {
                synchronized (storage) {
                    while (storage.size() == 0) {
                        try {
                            storage.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Long l = storage.poll();
                    System.out.println("消费了资源" + l + "资源个数" + storage.size());
                    storage.notifyAll();
                }
            }
        }
    }
}
