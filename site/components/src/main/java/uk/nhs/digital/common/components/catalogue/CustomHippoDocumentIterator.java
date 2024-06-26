package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.content.beans.standard.HippoDocumentIterator;

import java.util.Iterator;

public class CustomHippoDocumentIterator<T> implements HippoDocumentIterator<T> {

    private final Iterator<T> iterator;
    private int position;

    public CustomHippoDocumentIterator(Iterator<T> iterator) {
        this.iterator = iterator;
        this.position = 0;
    }

    @Override
    public void skip(int i) {
        for (int index = 0; index < i; index++) {
            this.next();
        }
    }


    @Override
    public int getPosition() {
        return this.position;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        position++;
        return iterator.next();
    }
}
