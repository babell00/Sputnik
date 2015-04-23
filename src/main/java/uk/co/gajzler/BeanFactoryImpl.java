package uk.co.gajzler;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import uk.co.gajzler.annotation.ClassObject;
import uk.co.gajzler.annotation.MyInject;
import uk.co.gajzler.exception.CannotFindBeanException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class BeanFactoryImpl implements BeanFactory {

    private static BeanFactoryImpl instance;
    private Map<String, Object> beanMap;
    private List<String> packageList;


    private BeanFactoryImpl() {
        beanMap = new HashMap<String, Object>();
        packageList = new ArrayList<String>();
    }

    public static BeanFactoryImpl getBeanFactory() {
        if (instance == null)
            instance = new BeanFactoryImpl();
        return instance;
    }

    @Override
    public void addPackageToScan(String packageName) {
        packageList.add(packageName);
        findBeans();
    }

    @Override
    public Object getBean(String name) {
        Object ob = beanMap.get(name);
        for (Field next : ob.getClass().getDeclaredFields()) {
            MyInject myInject = next.getAnnotation(MyInject.class);
            try {
                next.set(ob, beanMap.get(myInject.inject()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return ob;
    }

    @Override
    public <T> T getBean(String name, Class<T> type) {
        Object object = getBean(name);
        return type.cast(object);
    }

    private void findBeans() {
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());


        FilterBuilder filterBuilder = new FilterBuilder();
        for (String next : packageList)
            filterBuilder.include(FilterBuilder.prefix(next));

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(filterBuilder));

        Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);

        for (Class clazz : allClasses) {
            if (!clazz.isInterface()) {
                Annotation annotation = clazz.getAnnotation(ClassObject.class);
                ClassObject classObject = (ClassObject) annotation;
                if (classObject != null) {
                    Object object = null;
                    try {
                        object = clazz.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (object == null)
                        throw new CannotFindBeanException();
                    beanMap.put(classObject.name(), object);
                }
            }
        }
    }

}
