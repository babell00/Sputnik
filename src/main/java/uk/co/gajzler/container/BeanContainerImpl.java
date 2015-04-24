package uk.co.gajzler.container;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import uk.co.gajzler.exceptions.NoBeanFoundException;
import uk.co.gajzler.log.SLogger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class BeanContainerImpl implements BeanContainer{

    private static final SLogger log = SLogger.getLogger(BeanContainerImpl.class);

    private Map<String, Class> noInstantiatedBeans;
    private Map<String, Object> instantiatedBeans;

    public BeanContainerImpl() {
        log.info("Initializing bean container");
        noInstantiatedBeans = Maps.newHashMap();
        instantiatedBeans = Maps.newHashMap();
    }

    @Override
    public void addBean(Class beanClass) {
        log.info("Registering bean : {0}", beanClass);
        noInstantiatedBeans.put(beanClass.getSimpleName(), beanClass);
    }

    @Override
    public void addBean(String beanName, Class beanClass) {
        log.info("Registering bean : {0}", beanClass);
        noInstantiatedBeans.put(beanName, beanClass);
    }

    @Override
    public void addBeans(Map<String, Class> beanClass) {
        log.debug("Registering beans : {0}", beanClass.size());
        noInstantiatedBeans.putAll(beanClass);
    }

    @Override
    public Object getBean(String beanName) {
        Object object = findBean(beanName);
        if (object == null) {
            log.error("Cannot find bean in bean container");
            throw new NoBeanFoundException();
        }
        return object;
    }

    @Override
    public List<String> getRegisteredBeanNames() {
        List registeredBeansList = Lists.newArrayList(instantiatedBeans.keySet());
        List transformedList = Lists.transform(Lists.newArrayList(noInstantiatedBeans.keySet()), new AsteriskFunction());
        registeredBeansList.addAll(transformedList);
        return registeredBeansList;
    }

    private Object findBean(String beanName) {
        Object beanObject = null;
        if (instantiatedBeans.containsKey(beanName)) {
            log.debug("Loading bean : {0} from instantiatedBeans", beanName);
            beanObject = instantiatedBeans.get(beanName);
        } else {
            log.debug("Loading bean : {0} from noInstantiatedBeans", beanName);
            Class clazz = noInstantiatedBeans.get(beanName);
            if (clazz != null) {
                log.debug("Creating instance of bean : {0}", beanName);
                try {
                    beanObject = clazz.newInstance();
                    moveBeanToInstantiatedBeans(beanName, beanObject);
                } catch (InstantiationException e) {
                    log.error("{0}", e);
                } catch (IllegalAccessException e) {
                    log.error("{0}", e);
                }
            }
        }
        return beanObject;
    }

    private void moveBeanToInstantiatedBeans(String beanName, Object object) {
        log.debug("Moving bean : {0} from noInstantiatedBeans to instantiatedBeans", beanName);
        noInstantiatedBeans.remove(beanName);
        instantiatedBeans.put(beanName, object);
    }

    private class AsteriskFunction implements Function {

        @Nullable
        @Override
        public Object apply(Object o) {
            String name = (String) o;
            return "*" + name;
        }
    }
}
