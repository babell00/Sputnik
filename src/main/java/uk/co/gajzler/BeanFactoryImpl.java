package uk.co.gajzler;

import uk.co.gajzler.annotations.InjectBean;
import uk.co.gajzler.container.BeanContainer;
import uk.co.gajzler.container.BeanContainerImpl;
import uk.co.gajzler.log.SLogger;
import uk.co.gajzler.proxy.PackageLoader;

import java.lang.reflect.Field;
import java.util.List;

public class BeanFactoryImpl implements BeanFactory {

    private static final SLogger log = SLogger.getLogger(BeanFactoryImpl.class);

    private static final BeanFactoryImpl INSTANCE = new BeanFactoryImpl();
    private PackageLoader packageLoader;
    private BeanContainer beanContainer;


    private BeanFactoryImpl() {
        beanContainer = new BeanContainerImpl();
        packageLoader = new PackageLoaderImpl();
    }

    public static BeanFactoryImpl getBeanFactory() {
        return INSTANCE;
    }

    @Override
    public void addPackageToScan(String packageName) {
        packageLoader.addPackage(packageName);
        beanContainer.addBeans(packageLoader.getMap());
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

}
