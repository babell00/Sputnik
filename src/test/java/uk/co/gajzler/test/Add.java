package uk.co.gajzler.test;

import uk.co.gajzler.annotation.MyAnnotation;

/**
 * Created by gajzl_j on 2015-04-23.
 */
public interface Add {
    @MyAnnotation
    public void add(int a, int b);
}
