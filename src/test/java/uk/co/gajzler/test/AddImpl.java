package uk.co.gajzler.test;

import uk.co.gajzler.annotation.ClassObject;

@ClassObject(name ="Add")
public class AddImpl implements Calculator {

    @Override
    public void calculate(int a, int b) {
        System.out.print(getClass().getAnnotation(ClassObject.class).name());
        System.out.println(" - " + a + " + " + b + " = " + (a + b));
    }
}
