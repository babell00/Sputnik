import org.junit.Test;
import pl.com.best.BeanFactoryImpl;
import uk.co.gajzler.Calculator;
import pl.com.best.BeanFactory;
import uk.co.gajzler.SayHello;

public class ProxyTest {

    @Test
    public void test1() {
        BeanFactory bf = BeanFactoryImpl.getBeanFactory();

        bf.addPackageToScan("pl.com.best");
        bf.addPackageToScan("uk.co.gajzler");

        Calculator cal1 = (Calculator) bf.getBean("Add");
        Calculator cal2 = bf.getBean("Calculator", Calculator.class);
        SayHello say1 = bf.getBean("say",SayHello.class);


        cal1.calculate(1, 4);
        cal2.calculate(21,8);

        say1.sayHello("Z metody testowej");
    }

    @Test
    public void test2() throws IllegalAccessException, InstantiationException {


    }


}
