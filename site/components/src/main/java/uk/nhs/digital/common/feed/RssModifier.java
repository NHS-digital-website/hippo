package uk.nhs.digital.common.feed;

import com.rometools.rome.feed.rss.Item;
import org.apache.commons.lang3.StringUtils;
import org.bloomreach.forge.feed.api.modifier.RSS20Modifier;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSetBean;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.jdom2.Element;
import org.jdom2.Namespace;
import uk.nhs.digital.website.beans.Blog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.query.Query;

public class RssModifier extends RSS20Modifier {

    @Override
    public void modifyEntry(final HstRequestContext context, final Item entry, final HippoBean bean) {
        super.modifyEntry(context, entry, bean);
        String scope = "";
        String requestPath = null;
        if (bean instanceof Blog) {
            try {
                requestPath = context.getBaseURL().getRequestPath();

                final String statement = "/jcr:root/content/documents/corporate-website/feeds//*[@jcr:primaryType='feed:rss20descriptor']";
                Session session = context.getSession();
                final Query q = session.getWorkspace().getQueryManager()
                    .createQuery(statement, Query.XPATH);

                final NodeIterator nodes = q.execute().getNodes();
                while (nodes.hasNext()) {
                    Node node = nodes.nextNode();

                    scope = node.getProperty("feed:scope").getString();
                    if (requestPath.contains("/" + scope)) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            final Blog blogBean = (Blog) bean;
            final Date lastUpdated = blogBean.getLastModified();
            if (lastUpdated != null) {
                HstLink hstLink = context.getHstLinkCreator().create(bean, context);
                String blogPath = bean.getPath();
                if (hstLink != null
                    && (StringUtils.isNotBlank(scope)
                    && blogPath.contains("/" + scope))
                    || requestPath.contains("all-blog")
                ) {
                    List<Element> foreignMarkup = entry.getForeignMarkup();

                    Element lmElement = new Element("channel");
                    lmElement.addContent(scope.replace("-", " ").toUpperCase());
                    foreignMarkup.add(lmElement);

                    lmElement = new Element("title");
                    lmElement.addContent(blogBean.getTitle());
                    foreignMarkup.add(lmElement);

                    lmElement = new Element("author");
                    List<String> lsAuthor = new ArrayList<>();
                    for (HippoBean author : blogBean.getAuthors()) {
                        lsAuthor.add(author.getName());
                    }
                    lmElement.addContent(blogBean.getAuthors().size() == 0 ? blogBean.getAuthorName() : lsAuthor.toString());
                    foreignMarkup.add(lmElement);

                    lmElement = new Element("category");
                    lmElement.addContent(blogBean.getCategories().toString());
                    foreignMarkup.add(lmElement);

                    lmElement = new Element("description");
                    lmElement.addContent(blogBean.getAllDescription());

                    foreignMarkup.add(lmElement);

                    lmElement = new Element("guid");
                    lmElement.addContent(blogBean.getGuid());
                    foreignMarkup.add(lmElement);

                    lmElement = new Element("pubDate");

                    SimpleDateFormat format1 = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                    String formatted = format1.format(blogBean.getDateOfPublication().getTime());

                    lmElement.addContent(formatted);
                    foreignMarkup.add(lmElement);

                    lmElement = new Element("lastmodified");
                    lmElement.addContent(format1.format(lastUpdated));
                    foreignMarkup.add(lmElement);

                    lmElement = new Element("link");
                    lmElement.addContent(hstLink.toUrlForm(context, true));
                    foreignMarkup.add(lmElement);

                    final HippoGalleryImageSetBean image = blogBean.getLeadImage();
                    if (image != null) {
                        hstLink = context.getHstLinkCreator().create(image, context);
                        if (hstLink != null) {
                            final Element element = new Element("image", Namespace.getNamespace("image", "http://web.resource.org/rss/1.0/modules/image/"));
                            element.addContent(hstLink.toUrlForm(context, true));
                            foreignMarkup = entry.getForeignMarkup();
                            foreignMarkup.add(element);
                        }
                    }
                }
            }
        }
    }
}
