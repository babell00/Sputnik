package uk.co.gajzler;

import pl.com.best.annotation.MyAnnotation;

/**
 * Created by gajzl_j on 2015-04-23.
 */
public interface Calculator {
    @MyAnnotation(enabled = false, name = "Calculator")
    public void calculate(int a, int b);
}
