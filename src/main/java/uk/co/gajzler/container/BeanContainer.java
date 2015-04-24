package uk.co.gajzler.container;

import java.util.List;
import java.util.Map;

/**
 * Created by gajzl_j on 2015-04-24.
 */
public interface BeanContainer {
    public void addBean(Class beanClass);
    public void addBean(String beanName, Class beanClass);
    public void addBeans(Map<String, Class> beanClass);
    public Object getBean(String beanName);
    public List<String> getRegisteredBeanNames();
}
