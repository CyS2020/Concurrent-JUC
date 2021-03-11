/**
 * @author: CyS2020
 * @date: 2021/3/11
 * 描述：操纵共享变量
 * 多线程的几点思考：
 * 1. 共享变量会造成线程安全问题，而共享变量的来源一是引用传递(值传递不会)，二是全局变量
 * 2. 涉及到对共享变量的修改才会造成线程安全问题，如果只是读取则不会造成线程安全问题
 * 3. 基本数据类型包装类与字符串是不可变对象，基本数据类型是值传递，共享他们不会造成线程安全问题
 * 4. 方法中的局部变量均是存放在自己的方法栈中，不会造成线程安全问题，即使变量类型是对象类型
 */
public class Main {

    public static class Person {
        int age;
    }

    public static void main(String[] args) {
        Person p = new Person();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> addAge(p)).start();
        }
        System.out.println(p.age);

        Integer x = 0;
        for (int i = 0; i < 100; i++) {
            new Thread(() -> addInt(x)).start();
        }
        System.out.println(x);
    }

    public static void addAge(Person p) {
        p.age++;
    }

    public static void addInt(Integer i) {
        i++;
    }
}
