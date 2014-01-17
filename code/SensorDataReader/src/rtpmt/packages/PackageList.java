/*
 * Package table - Singleton Implementation
 *@author Saravana Kumar
 *@version 1.0
 */
package rtpmt.packages;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Kumar
 */
public class PackageList {

    //ingleton instance
    private static ConcurrentHashMap<Integer, Package> INSTANCE;

    //private constructor to prevent accidental instialization
    private PackageList() {

    }

    /**
     *
     * @return
     */
    private static ConcurrentHashMap<Integer, Package> getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new ConcurrentHashMap<Integer, Package>();
        }

        return INSTANCE;
    }

    /**
     *
     * @param shortId
     * @param macId
     */
    public static void addPackage(int shortId, String macId) {

        ConcurrentHashMap<Integer, Package> packageTable = getInstance();

        if (packageTable.containsKey(shortId)) {

            Package pack = packageTable.get(shortId);
            pack.setSensorId(macId);

        } else {

            Package pack;
            pack = new Package(shortId, macId);
            packageTable.put(shortId, pack);
        }

    }

    /**
     *
     * @param shortId
     * @param pack
     */
    public static void addPackage(int shortId, Package pack) {

        ConcurrentHashMap<Integer, Package> packageTable = getInstance();

        packageTable.put(shortId, pack);

    }

    /**
     *
     * @param shortId
     * @return Package
     */
    public static Package getPackage(int shortId) {

        ConcurrentHashMap<Integer, Package> packageTable = getInstance();

        return packageTable.containsKey(shortId) ? packageTable.get(shortId) : null;
    }

    /**
     *
     * @param macId
     * @return Package
     */
    public static Package getPackage(String macId) {

        ConcurrentHashMap<Integer, Package> packageTable = getInstance();

        for (Map.Entry<Integer, Package> entry : packageTable.entrySet()) {
            Package package1 = entry.getValue();

            if (package1.getSensorId().equals(macId)) {
                return package1;
            }

        }

        return null;
    }

    public static Collection<Package> getPackages() {

        return getInstance().values();

    }
}
