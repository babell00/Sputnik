package uk.co.gajzler.test;

import uk.co.gajzler.annotations.Bean;

@Bean(name ="Add")
public class AddImpl implements Calculator {

    @Override
    public void calculate(int a, int b) {
        System.out.print(getClass().getAnnotation(Bean.class).name());
        System.out.println(" - " + a + " + " + b + " = " + (a + b));
    }
}
