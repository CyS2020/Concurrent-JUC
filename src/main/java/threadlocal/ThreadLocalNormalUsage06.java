package threadlocal;

/**
 * @author: CyS2020
 * @date: 2021/1/31
 * 描述：演示ThreadLocal用法2：避免参数传递
 */
public class ThreadLocalNormalUsage06 {

    public static void main(String[] args) {
        new Service1().process();
    }
}

class Service1{

    public void process(){
        User user = new User("超哥");
        UserContextHolder.holder.set(user);
        new Service2().process();
    }
}

class Service2{

    public void process(){
        User user = UserContextHolder.holder.get();
        System.out.println("Service2拿到用户名" + user.name);
        // 调用remove方法，会清除User
        UserContextHolder.holder.remove();
        // 调用set方法，重新赋值
        user = new User("王姐");
        UserContextHolder.holder.set(user);
        new Service3().process();
    }
}

class Service3{

    public void process(){
        User user = UserContextHolder.holder.get();
        System.out.println("Service3拿到用户名" + user.name);
    }
}

class UserContextHolder{

    public static ThreadLocal<User> holder = new ThreadLocal<>();
}

class User{

    String name;

    public User(String name) {
        this.name = name;
    }
}
