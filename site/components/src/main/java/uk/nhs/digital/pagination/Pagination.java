package uk.nhs.digital.pagination;

import uk.nhs.digital.ps.beans.IndexPage;

public class Pagination {
    private IndexPage previous;
    private IndexPage next;

    public Pagination(IndexPage previous, IndexPage next) {
        this.previous = previous;
        this.next = next;
    }

    public IndexPage getPrevious() {
        return previous;
    }

    public IndexPage getNext() {
        return next;
    }
}
