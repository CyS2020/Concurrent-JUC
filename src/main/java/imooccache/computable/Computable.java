package imooccache.computable;

/**
 * @author: CyS2020
 * @date: 2021/2/21
 * 描述：有一个计算函数compute，用来代表计算耗时，每个计算器都要实现这个接口
 * 这样就可以无侵入试下缓存功能
 */
public interface Computable<A, V> {

    V compute(A arg) throws Exception;
}
