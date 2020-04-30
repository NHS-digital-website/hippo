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
        return getChildBeansIfPermitted("publicationsystem:bodySections", null);
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
        int index = IntStream
            .range(0, getPublication().getPageIndex().size())
            .filter(i -> getPublication().getPageIndex().get(i).getTitle().equalsIgnoreCase(getTitle()))
            .findFirst()
            .orElse(-1);
        if (0 <= index && index < getPublication().getPageIndex().size()) {
            if (index == 0) {
                return new Pagination(null, getIndexPage(getPublication(), 1));
            } else if (index < getPublication().getPageIndex().size() - 1) {
                return new Pagination(getIndexPage(getPublication(), index - 1), getIndexPage(getPublication(), index + 1));
            } else {
                return new Pagination(getIndexPage(getPublication(), index - 1), null);
            }
        }
        return null;
    }

    private static IndexPage getIndexPage(Publication publication, int index) {
        return publication.getPageIndex().get(index);
    }
}
