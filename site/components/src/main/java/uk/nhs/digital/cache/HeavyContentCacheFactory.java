package uk.nhs.digital.cache;

public class HeavyContentCacheFactory {
    public static <K, V> HeavyContentCache<K, V> factory(
        String type,
        HeavyContentCache<K, V> redisCache,
        HeavyContentCache<K, V> diskCache) throws Exception {

        if (type.equals("redis")) {
            return redisCache;
        } else if (type.equals("disk")) {
            return diskCache;
        } else {
            throw new Exception("No cache implementation found");
        }
    }
}
