package uk.co.gajzler;

import java.util.List;

/**
 * Created by gajzl_j on 2015-04-23.
 */
public interface BeanFactory {
    /**
     * Method adds package to list. So later this package will
     * scanned for annotated classes and added to bean container.
     *
     * @param packageName package name which will be scanned e.g. pl.com.system
     */
    public void addPackageToScan(String packageName);

    /**
     * Method look by name of the bean in container for a bean, and if bean is find return Object.
     *
     * @param name name of the bean
     * @return Object from bean container
     */
    public Object getBean(String name);

    /**
     * Method look by name of the bean in container for a bean, and if bean is find return Object in specified type.
     *
     * @param name name of the bean
     * @param type type of object e.g. MyObject.class
     * @return Object from bean container
     */
    public <T> T getBean(String name, Class<T> type);

    /**
     * Method return list of all registered object in bean container.
     *
     * @return list of all registered beans
     */
    public List<String> registeredBeans();
}
