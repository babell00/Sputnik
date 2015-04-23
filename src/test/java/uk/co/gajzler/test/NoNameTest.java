package uk.co.gajzler.test;

import uk.co.gajzler.annotation.Bean;
import uk.co.gajzler.annotation.InjectBean;

@Bean
public class NoNameTest {

    @InjectBean(beanName = "say")
    private SayHello sss;

    public void doit(){
        sss.sayHello("Dupa dupa dupa");
    }

}
