package uk.co.gajzler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class BeanContainer {
    private Map<String, Object> instantiatedBeans;

    public BeanContainer(){
        instantiatedBeans = Maps.newHashMap();
    }

    public void addBean(String name, Object bean){
        instantiatedBeans.put(name,bean);
    }

    public Object getBean(String name){
        return instantiatedBeans.get(name);
    }
    public List<String> getRegisteredBeanNames(){
        return  Lists.newArrayList(instantiatedBeans.keySet());
    }
}
