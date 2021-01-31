package threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: CyS2020
 * @date: 2021/1/31
 * 描述：两个线程打印日期
 */
public class ThreadLocalNormalUsage00 {

    public static void main(String[] args) {
        new Thread(() -> {
            String date = new ThreadLocalNormalUsage00().date(10);
            System.out.println(date);
        }).start();
        new Thread(() -> {
            String date = new ThreadLocalNormalUsage00().date(104707);
            System.out.println(date);
        }).start();
    }

    public String date(int seconds){
        // 参数的单位是毫秒，是从1970.01.01 00:00:00 GMT计时
        Date date = new Date(1000 * seconds);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dateFormat.format(date);
    }
}
