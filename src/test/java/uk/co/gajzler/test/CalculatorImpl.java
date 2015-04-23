package uk.co.gajzler.test;

import uk.co.gajzler.annotation.ClassObject;
import uk.co.gajzler.annotation.MyInject;

@ClassObject(name = "Calculator")
public class CalculatorImpl implements Calculator {

    @MyInject(inject = "say")
    public SayHello say;

    @MyInject(inject = "myClass")
    public MyClass myClass;

    @Override
    public void calculate(int a, int b) {
        System.out.print(getClass().getAnnotation(ClassObject.class).name());
        System.out.println(" - " + a + " + " + b + " = " + (a + b));

        myClass.getName();

        say.sayHello("Z klasy po inject");
    }
}