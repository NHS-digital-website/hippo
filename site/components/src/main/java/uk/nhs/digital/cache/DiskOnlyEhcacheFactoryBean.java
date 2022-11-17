package uk.nhs.digital.cache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import uk.nhs.digital.common.util.DateUtils;

import java.time.Duration;

public class DiskOnlyEhcacheFactoryBean implements FactoryBean<Cache<String, String>>, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(DiskOnlyEhcacheFactoryBean.class);

    private Cache<String, String> cache;

    private final CacheManager cacheManager;
    private final String cacheName;

    private final long maxMegabytesLocalDisk;
    private final boolean diskContentSurvivesJvmRestarts;
    private final String timeToIdle;

    public DiskOnlyEhcacheFactoryBean(
        final CacheManager cacheManager,
        final String cacheName,
        final Long maxMegabytesLocalDisk,
        final boolean diskContentSurvivesJvmRestarts,
        final String timeToIdle
    ) {
        this.cacheManager = cacheManager;
        this.cacheName = cacheName;
        this.maxMegabytesLocalDisk = maxMegabytesLocalDisk;
        this.diskContentSurvivesJvmRestarts = diskContentSurvivesJvmRestarts;
        this.timeToIdle = timeToIdle;
    }

    @Override public Cache<String, String> getObject() throws Exception {
        return cache;
    }

    @Override public Class<?> getObjectType() {
        return Cache.class;
    }

    @Override public void afterPropertiesSet() throws Exception {

        log.info("Configuring cache '{}' with:", cacheName);
        log.info("- maxMegabytesLocalDisk: {}", maxMegabytesLocalDisk);
        log.info("- diskContentSurvivesJvmRestarts: {}", diskContentSurvivesJvmRestarts);
        log.info("- timeToIdle: {}", timeToIdle);

        final Duration timeToIdleDuration = DateUtils.durationFromIso(timeToIdle);

        cache = cacheManager.createCache(
            cacheName,
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                    String.class,
                    String.class,
                    ResourcePoolsBuilder.newResourcePoolsBuilder()
                        .disk(maxMegabytesLocalDisk, MemoryUnit.MB, diskContentSurvivesJvmRestarts)
                )
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(timeToIdleDuration))
                .build()
        );
    }
}
