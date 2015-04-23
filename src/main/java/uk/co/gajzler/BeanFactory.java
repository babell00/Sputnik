package uk.co.gajzler;

import java.util.List;

/**
 * Created by gajzl_j on 2015-04-23.
 */
public interface BeanFactory {
    public void addPackageToScan(String packageName);
    public Object getBean(String name);
    public <T> T getBean(String name, Class<T> type);
    public List<String> registeredBeans();
}
