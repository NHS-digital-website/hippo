package uk.nhs.digital.common.components.catalogue;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import uk.nhs.digital.common.components.catalogue.filters.Filters;

import java.time.Duration;

public class ApiCatalogueCacheManager {

    static CacheManager cacheManager = null;

    public static CacheManager loadCacheManager() {

        if (cacheManager == null) {
            cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("apiFilterCache",
                    CacheConfigurationBuilder.newCacheConfigurationBuilder(
                            String.class, Filters.class,
                            ResourcePoolsBuilder.newResourcePoolsBuilder().heap(100, EntryUnit.ENTRIES))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(10)))
                )
                .build(true);
        }
        return cacheManager;
    }
}
