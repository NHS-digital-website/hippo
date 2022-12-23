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
            throw new Exception("No cache implentation found");
        }
    }

    public static int factory2(String redisUrl) throws Exception {
        if (redisUrl.equals("redis")) {
            return 1;
        } else {
            return 2;
        }
    }
}
