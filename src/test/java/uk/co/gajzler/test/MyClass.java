package uk.co.gajzler.test;

import uk.co.gajzler.annotation.Bean;

@Bean(name= "myClass")
public class MyClass {
    private String name;

    public void getName(){
        name = "dupa dupa";
        System.out.println(name);
    }
}
