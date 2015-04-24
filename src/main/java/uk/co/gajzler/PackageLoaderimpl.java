package uk.co.gajzler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import uk.co.gajzler.annotations.Bean;
import uk.co.gajzler.log.SLogger;
import uk.co.gajzler.proxy.PackageLoader;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Kuba on 24/04/15.
 */
public class PackageLoaderImpl implements PackageLoader{

    private static final SLogger log = SLogger.getLogger(PackageLoaderImpl.class);

    private List<ClassLoader> classLoadersList;
    private Map<String, Class<?>> scannedClasses;

    public PackageLoaderImpl() {
        scannedClasses = Maps.newHashMap();
        classLoadersList = Lists.newLinkedList();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());
    }

    @Override
    public void addPackage(String packageName) {
        log.info("Add package to scan : {0}", packageName);
        findBeans(packageName);
    }

    @Override
    public Map<String, Class<?>> getMap() {
        return scannedClasses;
    }

    private void findBeans(String packageName) {
        FilterBuilder filterBuilder = new FilterBuilder();
        filterBuilder.include(FilterBuilder.prefix(packageName));

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
                    if ("".equals(beanName))
                        scannedClasses.put(clazz.getSimpleName(), clazz);
                    else
                        scannedClasses.put(beanName, clazz);
                }
            }
        }
    }
}
