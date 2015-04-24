package uk.co.gajzler;

import java.util.Map;

/**
 * Created by Kuba on 24/04/15.
 */
public interface PackageLoader {
    public void addPackage(String packageName);
    public Map<String, Class> getMap();
}
