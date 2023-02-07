package uk.nhs.digital.cache;

import org.springframework.beans.factory.BeanNameAware;

import java.util.function.Supplier;

public interface HeavyContentCache<K, V> extends BeanNameAware {
    V get(K key, Supplier<V> valueFactory);

    void remove(K key);

    void purge();

    @Override
    void setBeanName(String name);
}
