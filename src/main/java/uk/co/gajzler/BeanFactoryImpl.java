package uk.co.gajzler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sun.deploy.util.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import uk.co.gajzler.annotation.Bean;
import uk.co.gajzler.annotation.InjectBean;
import uk.co.gajzler.exception.CannotFindBeanException;
import uk.co.gajzler.log.SLogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactoryImpl implements BeanFactory {

    private static final SLogger log = SLogger.getLogger(BeanFactoryImpl.class);

    private static BeanFactoryImpl instance;
    private Map<String, Object> beanMap;
    private Set<String> packageList;


    private BeanFactoryImpl() {
        beanMap = Maps.newHashMap();
        packageList = Sets.newHashSet();
    }

    public static BeanFactoryImpl getBeanFactory() {
        log.info("Instantiating BeanFactory");
        if (instance == null)
            instance = new BeanFactoryImpl();
        return instance;
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
        Object ob = beanMap.get(name);
        for (Field next : ob.getClass().getDeclaredFields()) {
            next.setAccessible(true);
            InjectBean myInject = next.getAnnotation(InjectBean.class);
            try {
                next.set(ob, beanMap.get(myInject.beanName()));
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
        return Lists.newArrayList(beanMap.keySet());
    }

    private void findBeans() {
        log.info("Scaning packages : [{0}]", StringUtils.join(packageList, ", "));

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
                Annotation annotation = clazz.getAnnotation(Bean.class);
                Bean classObject = (Bean) annotation;
                if (classObject != null) {
                    Object object = null;
                    try {
                        object = clazz.newInstance();
                    } catch (InstantiationException e) {
                        log.error("{0}", e);
                    } catch (IllegalAccessException e) {
                        log.error("{0}", e);
                    }
                    if (object == null)
                        throw new CannotFindBeanException();
                    log.info("Registering bean : {0}", object.getClass().getName());

                    String beanName = classObject.name();
                    if("".equals(beanName))
                        beanMap.put(clazz.getSimpleName(),object);
                    else
                        beanMap.put(beanName, object);
                }
            }
        }
    }

}
