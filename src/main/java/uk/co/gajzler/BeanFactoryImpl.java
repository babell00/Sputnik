package uk.co.gajzler;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sun.deploy.util.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import uk.co.gajzler.annotations.Bean;
import uk.co.gajzler.annotations.InjectBean;
import uk.co.gajzler.container.BeanContainerImpl;
import uk.co.gajzler.log.SLogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import java.util.Set;

public class BeanFactoryImpl implements BeanFactory {

    private static final SLogger log = SLogger.getLogger(BeanFactoryImpl.class);

    private static final BeanFactoryImpl INSTANCE = new BeanFactoryImpl();
    private Set<String> packageList;
    private BeanContainerImpl beanContainer;


    private BeanFactoryImpl() {
        beanContainer = new BeanContainerImpl();
        packageList = Sets.newHashSet();
    }

    public static BeanFactoryImpl getBeanFactory() {
        return INSTANCE;
    }

    @Override
    public void addPackageToScan(String packageName) {
        log.info("Add package to scan : {0}", packageName);
        boolean isAdded = packageList.add(packageName);
        if (isAdded)
            findBeans();
        else
            log.info("Package : {0} have been already added.", packageName);
    }

    @Override
    public Object getBean(String name) {
        Object ob = beanContainer.getBean(name);
        for (Field next : ob.getClass().getDeclaredFields()) {
            next.setAccessible(true);
            InjectBean myInject = next.getAnnotation(InjectBean.class);
            try {
                next.set(ob, beanContainer.getBean(myInject.beanName()));
            } catch (IllegalAccessException e) {
                log.error("{0}", e);
            }
        }
        log.info("Getting bean : {0}", ob.getClass().getName());
        return ob;
    }

    @Override
    public <T> T getBean(String name, Class<T> type) {
        Object object = getBean(name);
        return type.cast(object);
    }

    @Override
    public List<String> registeredBeans() {
        return beanContainer.getRegisteredBeanNames();
    }

    private void findBeans() {
        log.info("Scanning packages : [{0}]", StringUtils.join(packageList, ", "));

        List<ClassLoader> classLoadersList = Lists.newLinkedList() ;
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
                Annotation annotation = clazz.getAnnotation(Bean.class);
                Bean classObject = (Bean) annotation;
                if (classObject != null) {
                    String beanName = classObject.name();
                    if("".equals(beanName))
                        beanContainer.addBean(clazz.getSimpleName(), clazz);
                    else
                        beanContainer.addBean(beanName, clazz);
                }
            }
        }
    }
}
