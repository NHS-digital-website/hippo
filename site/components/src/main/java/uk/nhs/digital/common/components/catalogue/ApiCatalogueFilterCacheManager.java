package uk.nhs.digital.common.components.catalogue;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import uk.nhs.digital.common.components.catalogue.filters.Filters;

import java.time.Duration;

public class ApiCatalogueFilterCacheManager {

    private static CacheManager filterCache = null;
    private static CacheManager facetBeanCache = null;

    public static CacheManager loadFilterCache() {

        if (filterCache == null) {
            filterCache = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("apiFilterCache",
                    CacheConfigurationBuilder.newCacheConfigurationBuilder(
                            String.class, Filters.class,
                            ResourcePoolsBuilder.newResourcePoolsBuilder().heap(100, EntryUnit.ENTRIES))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(10)))
                )
                .build(true);
        }
        return filterCache;
    }

    public static CacheManager loadFacetBeanCache() {
        if (facetBeanCache == null) {
            facetBeanCache = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("apiFacetBeanCache",
                    CacheConfigurationBuilder.newCacheConfigurationBuilder(
                            String.class, HippoFacetNavigationBean.class,
                            ResourcePoolsBuilder.newResourcePoolsBuilder().heap(100, EntryUnit.ENTRIES))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(10)))
                )
                .build(true);
        }
        return facetBeanCache;
    }
}
