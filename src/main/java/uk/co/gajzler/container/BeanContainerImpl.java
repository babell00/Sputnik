package uk.co.gajzler.container;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import uk.co.gajzler.exceptions.NoBeanFoundException;
import uk.co.gajzler.log.SLogger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class BeanContainerImpl implements BeanContainer {

    private static final SLogger LOG = SLogger.getLogger(BeanContainerImpl.class);

    private Map<String, Class> noInstantiatedBeans;
    private Map<String, Object> instantiatedBeans;

    public BeanContainerImpl() {
        LOG.info("Initializing bean container");
        noInstantiatedBeans = Maps.newHashMap();
        instantiatedBeans = Maps.newHashMap();
    }

    @Override
    public void addBean(Class beanClass) {
        LOG.info("Registering bean : {0}", beanClass);
        noInstantiatedBeans.put(beanClass.getSimpleName(), beanClass);
    }

    @Override
    public void addBean(String beanName, Class beanClass) {
        LOG.info("Registering bean : {0}", beanClass);
        noInstantiatedBeans.put(beanName, beanClass);
    }

    @Override
    public void addBeans(Map<String, Class> beanClass) {
        LOG.debug("Registering beans : {0}", beanClass.size());
        noInstantiatedBeans.putAll(beanClass);
    }

    @Override
    public Object getBean(String beanName) {
        Object object = findBean(beanName);
        if (object == null) {
            LOG.error("Cannot find bean in bean container");
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
            LOG.debug("Loading bean : {0} from instantiatedBeans", beanName);
            beanObject = instantiatedBeans.get(beanName);
        } else {
            LOG.debug("Loading bean : {0} from noInstantiatedBeans", beanName);
            Class<?> clazz = noInstantiatedBeans.get(beanName);
            if (clazz != null) {
                LOG.debug("Creating instance of bean : {0}", beanName);
                try {
                    beanObject = clazz.newInstance();
                    moveBeanToInstantiatedBeans(beanName, beanObject);
                } catch (InstantiationException e) {
                    LOG.error("{0}", e);
                } catch (IllegalAccessException e) {
                    LOG.error("{0}", e);
                }
            }
        }
        return beanObject;
    }

    private void moveBeanToInstantiatedBeans(String beanName, Object object) {
        LOG.debug("Moving bean : {0} from noInstantiatedBeans to instantiatedBeans", beanName);
        noInstantiatedBeans.remove(beanName);
        instantiatedBeans.put(beanName, object);
    }

    private static class AsteriskFunction implements Function<String, String> {
        @Nullable
        @Override
        public String apply(String o) {
            String name = o;
            return "*" + name;
        }
    }
}
