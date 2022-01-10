package uk.nhs.digital.cache;

import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class OnDiskEhcacheManagerFactoryBean implements FactoryBean<CacheManager>, InitializingBean, DisposableBean, BeanNameAware {

    private static final Logger log = LoggerFactory.getLogger(OnDiskEhcacheManagerFactoryBean.class);

    public static final String DEFAULT_DISK_STORE_PATH_TEMPLATE = "java.io.tmpdir/custom-site-cache";

    private String beanName;

    private PersistentCacheManager cacheManager;

    private final String givenDiskStorePath;

    public OnDiskEhcacheManagerFactoryBean(final String diskStorePath) {
        this.givenDiskStorePath = diskStorePath;
    }

    @Override public void afterPropertiesSet() throws Exception {

        final String diskStorePath = resolveActualDiskStorePath();

        log.info("Cache manager '{}' uses disk location: {}", beanName, diskStorePath);

        cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
            .with(CacheManagerBuilder.persistence(diskStorePath))
            .build(true);
    }

    @Override public void destroy() throws Exception {
        if (cacheManager != null) {
            log.info("Closing cache manager '{}'.", beanName);

            cacheManager.close();
        }
    }

    @Override public CacheManager getObject() throws Exception {
        return cacheManager;
    }

    @Override public Class<?> getObjectType() {
        return (cacheManager == null ? CacheManager.class : cacheManager.getClass());
    }

    @Override public boolean isSingleton() {
        return true;
    }

    @Override public void setBeanName(final String beanName) {
        this.beanName = beanName;
    }

    private String resolveActualDiskStorePath() {
        return "defaultTempLocation".equals(givenDiskStorePath)
            ? DEFAULT_DISK_STORE_PATH_TEMPLATE.replace("java.io.tmpdir", System.getProperty("java.io.tmpdir"))
            : givenDiskStorePath;
    }
}
