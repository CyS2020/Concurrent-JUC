package future;

/**
 * @author: CyS2020
 * @date: 2021/2/19
 * 描述：在run方法中无法抛出checked Exception--引出Callable
 */
public class RunnableCantThrowsException {

    public static void main(String[] args) {
        Runnable runnable = () -> {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public void testException() throws Exception{

    }
}
