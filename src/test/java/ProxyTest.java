import org.junit.Test;
import uk.co.gajzler.BeanFactoryImpl;
import uk.co.gajzler.test.Calculator;
import uk.co.gajzler.BeanFactory;
import uk.co.gajzler.test.SayHello;

public class ProxyTest {

    @Test
    public void test1() {
        BeanFactory bf = BeanFactoryImpl.getBeanFactory();

        bf.addPackageToScan("uk.co.gajzler");
        bf.addPackageToScan("uk.co.dupa");
        bf.addPackageToScan("uk.co.gajzler");
        bf.addPackageToScan("pl.com.kuba");
        bf.addPackageToScan("uk.co.gajzler");
        bf.addPackageToScan("uk.co.gajzler");
        bf.addPackageToScan("uk.co.gajzler");


        Calculator cal1 = (Calculator) bf.getBean("Add");
        Calculator cal2 = bf.getBean("Calculator", Calculator.class);
        SayHello say1 = bf.getBean("say",SayHello.class);


        cal1.calculate(1, 4);
        cal2.calculate(21,8);

        say1.sayHello("Z metody testowej");
    }



}
