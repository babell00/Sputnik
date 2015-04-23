package uk.co.gajzler;

import pl.com.best.annotation.ClassObject;

@ClassObject(name = "say")
public class SayHelloImpl implements SayHello {
    @Override
    public void sayHello(String text) {
        System.out.println(text);
    }
}
