package uk.co.gajzler.test;

import uk.co.gajzler.annotations.Bean;
import uk.co.gajzler.enums.BeanScope;

@Bean(name = "say",scope = BeanScope.SINGLETON)
public class SayHelloImpl implements SayHello {
    @Override
    public void sayHello(String text) {
        System.out.println(text);
    }
}
