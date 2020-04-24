package uk.nhs.digital.intranet.beans;

import static org.apache.commons.collections.IteratorUtils.toList;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.and;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.or;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.repository.util.DateTools.Resolution;
import uk.nhs.digital.website.beans.LeadImageSection;

import java.util.Calendar;
import java.util.List;

@Node(jcrType = "intranet:newsinternal")
public class NewsInternal extends BaseDocument {

    public List<HippoBean> getRelatedDocuments() {
        return getLinkedBeans("intranet:relateddocuments", HippoBean.class);
    }

    public Calendar getPublicationDate() {
        return getProperty("intranet:publicationdate");
    }

    public String getType() {
        return getProperty("intranet:typeofnews");
    }

    public Calendar getExpiryDate() {
        return getProperty("intranet:expirydate");
    }

    public HippoHtml getOptionalIntroductoryText() {
        return getHippoHtml("intranet:optionalintroductorytext");
    }

    public LeadImageSection getLeadImageSection() {
        return getBean("intranet:leadimagesection", LeadImageSection.class);
    }

    public List<HippoBean> getSections() {
        return getChildBeansByName("intranet:sections");
    }

    public String[] getKeys() {
        return getProperty("hippotaxonomy:keys");
    }

    public List<NewsInternal> getLatestNews() throws QueryException {
        HippoBean folder = RequestContextProvider.get().getSiteContentBaseBean();

        HippoBeanIterator hippoBeans = HstQueryBuilder.create(folder)
            .ofTypes(NewsInternal.class)
            .where(and(
                constraint("jcr:uuid").notEqualTo(this.getIdentifier()),
                or(
                    constraint("intranet:typeofnews").equalTo("permanent"),
                    constraint("intranet:expirydate")
                        .greaterOrEqualThan(Calendar.getInstance(), Resolution.HOUR)
                ))
            )
            .orderByDescending("intranet:publicationdate")
            .limit(5)
            .build()
            .execute()
            .getHippoBeans();

        return toList(hippoBeans);
    }
}
