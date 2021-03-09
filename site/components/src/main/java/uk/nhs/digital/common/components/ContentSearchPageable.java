package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.IdentifiableContentBean;
import org.onehippo.cms7.essentials.components.paging.Pageable;

import java.util.List;

public class ContentSearchPageable<T extends IdentifiableContentBean> extends Pageable<T> {

    private List<T> items;

    ContentSearchPageable(long total, int currentPage, int pageSize) {
        super(total, currentPage, pageSize);
    }

    @Override
    public List<T> getItems() {
        return items;
    }

}
