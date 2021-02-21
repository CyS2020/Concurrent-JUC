package imooccache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: CyS2020
 * @date: 2021/2/21
 * 描述：最简单的缓存形式：HashMap
 */
public class ImoocCache1 {

    // 加上final关键字，增强安全性
    private final Map<String, Integer> cache = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        ImoocCache1 imoocCache1 = new ImoocCache1();
        System.out.println("开始计算");
        Integer result = imoocCache1.computer("13");
        System.out.println("第一次计算结果：" + result);
        result = imoocCache1.computer("13");
        System.out.println("第二次计算结果：" + result);
    }

    public Integer computer(String userId) throws InterruptedException {
        Integer result = cache.get(userId);
        // 先检查HashMap里面有没有保存之间的计算结果
        if (result == null) {
            // 如果缓存中找不到，那么需要现在计算下结果，并保存到HashMap中
            result = doCompute(userId);
            cache.put(userId, result);
        }
        return result;
    }

    private synchronized Integer doCompute(String userId) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        return new Integer(userId);
    }
}
