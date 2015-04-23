package uk.co.gajzler.test;

import uk.co.gajzler.annotation.Bean;

@Bean(name = "say")
public class SayHelloImpl implements SayHello {
    @Override
    public void sayHello(String text) {
        System.out.println(text);
    }
}
