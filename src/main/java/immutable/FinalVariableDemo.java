package immutable;

/**
 * @author: CyS2020
 * @date: 2021/2/9
 * 描述：演示final变量
 */
public class FinalVariableDemo {

    private static final int a;

//    public FinalVariableDemo(int a) {
//        this.a = a;
//    }

//    {
//        this.a = 7;
//    }

    static {
        a = 7;
    }

    void testFinal(){
        final int b;
        b = 5;
        int c = b;
//        b = 4;
    }
}
