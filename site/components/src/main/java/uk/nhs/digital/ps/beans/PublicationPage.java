package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.pagination.Paginated;
import uk.nhs.digital.pagination.Pagination;

import java.util.List;
import java.util.stream.IntStream;

@HippoEssentialsGenerated(internalName = "publicationsystem:publication")
@Node(jcrType = "publicationsystem:publicationPage")
public class PublicationPage extends BaseDocument implements IndexPage, Paginated {

    @HippoEssentialsGenerated(internalName = "publicationsystem:Title")
    public String getTitle() {
        return getSingleProperty("publicationsystem:Title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:bodySections", allowModifications = false)
    public List<HippoBean> getSections() {
        return getChildBeansIfPermitted("publicationsystem:bodySections", HippoBean.class);
    }

    public Publication getPublication() {
        return HippoBeanHelper.getParentPublication(this);
    }

    public HippoHtml getSeosummary() {
        return getPublication().getSeosummary();
    }

    @Override
    public HippoBean getLinkedBean() {
        return this;
    }

    @Override
    public Pagination paginate() {
        Publication publication = getPublication();
        List<IndexPage> pageIndex = publication.getPageIndex();
        int index = IntStream
            .range(0, pageIndex.size())
            .filter(i -> pageIndex.get(i).getTitle().equalsIgnoreCase(getTitle()))
            .findFirst()
            .orElse(-1);

        if (index == -1) {
            return null;
        }

        IndexPage previous = index > 0 ? pageIndex.get(index - 1) : null;
        IndexPage next = index < pageIndex.size() - 1 ? pageIndex.get(index + 1) : null;

        return previous == null && next == null ? null : new Pagination(previous, next);
    }
}
