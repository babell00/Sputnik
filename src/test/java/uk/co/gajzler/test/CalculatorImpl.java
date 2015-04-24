package uk.co.gajzler.test;

import uk.co.gajzler.annotations.Bean;
import uk.co.gajzler.annotations.InjectBean;

@Bean(name = "Calculator")
public class CalculatorImpl implements Calculator {


    @InjectBean(beanName = "say")
    private SayHello say;

    @InjectBean(beanName = "myClass")
    private MyClass myClass;

    @Override
    public void calculate(int a, int b) {
        System.out.print(getClass().getAnnotation(Bean.class).name());
        System.out.println(" - " + a + " + " + b + " = " + (a + b));
        myClass.getName();

        say.sayHello("Z klasy po beanName");
    }
}
