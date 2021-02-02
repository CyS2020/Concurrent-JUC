package lock.reentrantlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: CyS2020
 * @date: 2021/2/2
 * 描述：演示ReentrantLock用法，中间被打断
 */
public class LockDemo {

    public static void main(String[] args) {
        new LockDemo().init();
    }

    private void init(){
        final Outputer outputer = new Outputer();
        new Thread(() -> {
            while(true){
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputer.output("悟空");
            }
        }).start();

        new Thread(() -> {
            while(true){
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputer.output("大师兄");
            }
        }).start();
    }

    public static class Outputer{

        Lock lock = new ReentrantLock();

        // 字符串打印方法，一个一个字符的打印
        public void output(String name){
            int len = name.length();
            lock.lock();
            try{
                for (int i = 0; i < len; i++){
                    System.out.print(name.charAt(i));
                }
                System.out.println();
            }finally {
                lock.unlock();
            }
        }
    }
}