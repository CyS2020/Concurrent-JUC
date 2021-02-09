package immutable;

/**
 * @author: CyS2020
 * @date: 2021/2/9
 * 描述：演示栈封闭的两种情况，基本变量和对象
 * 先演示线程争抢带来的错误结果，然后把变量放在方法内，情况就变了
 */
public class StackConfinement implements Runnable {

    public int index = 0;

    public static void main(String[] args) throws InterruptedException {
        StackConfinement r1 = new StackConfinement();
        Thread thread1 = new Thread(r1);
        Thread thread2 = new Thread(r1);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(r1.index);
    }

    public void inThread(){
        int neverGoOut = 0;
        for (int i = 0; i < 10000; i++){
            neverGoOut++;
        }
        System.out.println("栈内保护的数字是线程安全的：" + neverGoOut);
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++){
            index++;
        }
        inThread();
    }
}
