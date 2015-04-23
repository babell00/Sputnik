package pl.com.best.proxy;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import pl.com.best.annotation.ClassObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;

public class MyProxy implements InvocationHandler {

    private String name;
    private Class targetObject;
    private Map<String,Class> beanMap;

    public MyProxy() {
        beanMap = new HashMap<String, Class>();

        try {
            findAnnotatedObject();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public MyProxy(Class targetObject) {
        this();
        this.targetObject = targetObject;


    }


    public Object getObject(String name)  {
        this.name = name;
        Object retObject = null;
        try {
            retObject = beanMap.get(name).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return retObject;
    }

    public <T extends Object> T getObject(String name, Class<T> clazz){
        return clazz.cast(getObject(name));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object returnObject;
        System.out.println("Before Proxy");
        returnObject = targetObject.newInstance();
        System.out.println("After Proxy");
        return returnObject;
    }

    public void findAnnotatedObject() throws IllegalAccessException, InstantiationException {
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());



        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("pl.com.best"))));

        Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);
        for (Class clazz : allClasses) {
            if (!clazz.isInterface()) {
                Annotation annotation = clazz.getAnnotation(ClassObject.class);
                ClassObject classObject = (ClassObject) annotation;
                if (classObject != null) {
                    beanMap.put(classObject.name(),clazz);
                }
            }
        }
    }
}
