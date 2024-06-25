package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.content.beans.standard.HippoDocumentIterator;

import java.util.Iterator;

public class CustomHippoDocumentIterator<T> implements HippoDocumentIterator<T> {

    private final Iterator<T> iterator;

    public CustomHippoDocumentIterator(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public void skip(int i) {

    }

    @Override
    public int getPosition() {
        return 0;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }
}
