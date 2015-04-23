package uk.co.gajzler;

import pl.com.best.annotation.ClassObject;

@ClassObject(name= "myClass")
public class MyClass {
    private String name;

    public void getName(){
        name = "dupa dupa";
        System.out.println(name);
    }
}
