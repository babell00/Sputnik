package uk.co.gajzler.test;

import uk.co.gajzler.annotations.Bean;
import uk.co.gajzler.annotations.InjectBean;

@Bean
public class NoNameTest {

    @InjectBean(beanName = "say")
    private SayHello sss;

    public void doit(){
        sss.sayHello("Dupa dupa dupa");
    }

}
