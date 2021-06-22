package uk.nhs.digital.website.beans;

import static org.apache.commons.collections.IteratorUtils.toList;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.query.builder.Constraint;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:feedhub")
@Node(jcrType = "website:feedhub")
public class FeedHub extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:hubType")
    public String getHubType() {
        return getSingleProperty("website:hubType");
    }

    @HippoEssentialsGenerated(internalName = "website:feedType")
    public String getFeedType() {
        return getSingleProperty("website:feedType");
    }

    @HippoEssentialsGenerated(internalName = "website:subject")
    public List<HippoBean> getSubject() {
        return getLinkedBeans("website:subject", HippoBean.class);
    }

    public List<Blog> getLatestFeed() throws QueryException {
        Constraint constrain = constraint("website:title").exists();
        HippoBean folder = getCanonicalBean().getParentBean();
        if ("Site-wide documents".equalsIgnoreCase(getHubType())) {
            folder = RequestContextProvider.get().getSiteContentBaseBean();
            constrain = constraint("website:display").equalTo(true);
        }
        Class feedClass = null;
        switch (getFeedType()) {
            case "News":
                feedClass = News.class;
                break;
            case "Events":
                feedClass = Event.class;
                break;
            case "Cyber Alerts":
                feedClass = CyberAlert.class;
                break;
            case "Supplimentary information":
                feedClass = SupplementaryInformation.class;
                break;
            default:
        }

        HippoBeanIterator hippoBeans = HstQueryBuilder.create(folder)
            .ofTypes(feedClass)
            .where(constraint("jcr:uuid").notEqualTo(this.getIdentifier()))
            .where(constrain)
            .orderByDescending("website:dateofpublication")
            .build()
            .execute()
            .getHippoBeans();

        return toList(hippoBeans);

    }
}
