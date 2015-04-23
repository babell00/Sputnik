package uk.co.gajzler;

/**
 * Created by gajzl_j on 2015-04-23.
 */
public interface BeanFactory {
    public void addPackageToScan(String packageName);
    public Object getBean(String name);
    public <T> T getBean(String name, Class<T> type);
}