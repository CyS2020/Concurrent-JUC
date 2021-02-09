package immutable;

/**
 * @author: CyS2020
 * @date: 2021/2/9
 * 描述：final的方法
 */
public class FinalMethodDemo {

    public int a;

    public FinalMethodDemo(int a) {
        this.a = a;
    }

    public void drink(){

    }

    public final void eat(){

    }

    public static void sleep(){

    }
}

class SubClass extends FinalMethodDemo{


    public SubClass(int a) {
        super(a);
    }

    @Override
    public void drink() {
        super.drink();
        eat();
    }

//    public void eat(){
//
//    }

    public static void sleep(){

    }
}
