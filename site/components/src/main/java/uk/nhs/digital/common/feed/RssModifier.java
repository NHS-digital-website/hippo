package uk.nhs.digital.common.feed;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Item;
import org.apache.commons.lang3.StringUtils;
import org.bloomreach.forge.feed.api.modifier.RSS20Modifier;
import org.bloomreach.forge.feed.beans.RSS20FeedDescriptor;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.exceptions.FilterException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSetBean;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.repository.util.DateTools;
import org.jdom2.Element;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.beans.Publication;
import uk.nhs.digital.ps.beans.Series;
import uk.nhs.digital.website.beans.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class RssModifier extends RSS20Modifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(RssModifier.class);

    @Override
    public void modifyFeed(HstRequestContext context, Channel feed, RSS20FeedDescriptor descriptor) {

    }

    @Override
    public void modifyHstQuery(final HstRequestContext context, final HstQuery query, final RSS20FeedDescriptor descriptor) {
        try {
            String strQuery = query.getQueryAsString(true);

            if (strQuery.contains("jcr:primaryType=\'website:news\'")) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -3);
                Filter filter = query.createFilter();
                try {
                    filter.addGreaterOrEqualThan("website:publisheddatetime", calendar, DateTools.Resolution.DAY);
                    query.setFilter(filter);
                } catch (final FilterException exception) {
                    exception.printStackTrace();
                }
            } else if (strQuery.contains("jcr:primaryType=\'publicationsystem:publication\'")) {
                Filter filter = query.createFilter();
                try {
                    filter.addEqualTo("publicationsystem:PubliclyAccessible", true);
                    query.setFilter(filter);
                } catch (final FilterException exception) {
                    exception.printStackTrace();
                }
            }
        } catch (QueryException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifyEntry(final HstRequestContext context, final Item entry, final HippoBean bean) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        SimpleDateFormat lastUpdatedDateFormat = new SimpleDateFormat("dd MMM yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        if (bean instanceof Blog) {
            final Blog blogBean = (Blog) bean;
            final Date lastUpdated = blogBean.getLastModified();

            if (lastUpdated != null) {
                HstLink hstLink = context.getHstLinkCreator().create(bean, context);
                if (hstLink != null) {

                    List<Element> foreignMarkup = entry.getForeignMarkup();

                    foreignMarkup.add(getElement("title", blogBean.getTitle()));

                    Element lmElement = new Element("author");
                    String emailAddress = "enquiries@nhsdigital.nhs.uk";

                    StringBuilder strPerson = new StringBuilder();
                    if (blogBean.getAuthors() != null && blogBean.getAuthors().size() > 0) {
                        Person person = (Person) blogBean.getAuthors().get(0);
                        if (person.getRoles() != null && person.getRoles().getContactdetails() != null) {
                            final ContactDetail contactdetails = person.getRoles().getContactdetails();
                            if (StringUtils.isBlank(contactdetails.getEmailaddress())) {
                                strPerson.append(emailAddress);
                            } else {
                                strPerson.append(contactdetails.getEmailaddress());
                            }
                            strPerson.append(" (").append(contactdetails.getName()).append(")");
                        }
                    } else {
                        strPerson.append(emailAddress);
                        if (StringUtils.isNotBlank(blogBean.getAuthorName())) {
                            strPerson.append(" (").append(blogBean.getAuthorName()).append(")");
                        } else {
                            strPerson.append("(NHS Digital)");
                        }
                    }

                    lmElement.addContent(StringUtils.isNotBlank(strPerson.toString()) ? strPerson.toString() : "enquiries@nhsdigital.nhs.uk (NHS Digital)");

                    foreignMarkup.add(lmElement);
                    String category = blogBean.getCategories();
                    if (StringUtils.isNotBlank(category)) {
                        foreignMarkup.add(getElement("category", category));
                    }

                    foreignMarkup.add(getElement("description", blogBean.getAllDescription()));

                    Element guid = getElement("guid", blogBean.getGuid());
                    guid.setAttribute("isPermaLink", "false");
                    foreignMarkup.add(guid);

                    foreignMarkup.add(getElement("pubDate", dateFormat.format(lastUpdated)));

                    foreignMarkup.add(getElement("link", hstLink.toUrlForm(context, true)));
                    Element source = getElement("source", "NHS Digital");
                    source.setAttribute("url", "https://digital.nhs.uk");
                    foreignMarkup.add(source);

                    final HippoGalleryImageSetBean image = blogBean.getLeadImage();
                    if (image != null) {
                        hstLink = context.getHstLinkCreator().create(image, context);
                        if (hstLink != null) {
                            foreignMarkup = entry.getForeignMarkup();
                            String imageUrl = hstLink.toUrlForm(context, true);
                            final Element element = getImageElement(imageUrl);
                            foreignMarkup.add(element);
                        }
                    }
                }
            }
        } else if (bean instanceof News) {
            final News newsBean = (News) bean;
            List<Element> foreignMarkup = entry.getForeignMarkup();
            foreignMarkup.add(getElement("title", newsBean.getTitle()));
            ContactDetail contactDetail = newsBean.getMediacontact();
            String author = "NHS Digital";
            String emailAddress = "enquiries@nhsdigital.nhs.uk";
            if (contactDetail != null && StringUtils.isNotBlank(contactDetail.getName())) {
                author = contactDetail.getName();
                emailAddress = contactDetail.getEmailaddress();
            }
            foreignMarkup.add(getElement("author", (emailAddress + " (" + author + ")")));
            StringBuilder content = new StringBuilder();
            for (HippoBean sre : newsBean.getSections()) {
                String tempContent = new String();
                if (sre instanceof Section) {
                    tempContent = ((Section) sre).getHtmlJson();
                } else if (sre instanceof EmphasisBox) {
                    tempContent = ((EmphasisBox) sre).getBodyJson();
                } else if (sre instanceof Expander) {
                    tempContent = ((Expander) sre).getContent().getContent();
                } else if (sre instanceof Quote) {
                    tempContent = ((Quote) sre).getQuote().getContent();
                }
                content.append(tempContent);
            }
            foreignMarkup.add(getElement("description", content.toString()));

            String category = getNewsCategory(newsBean);
            if (StringUtils.isNotBlank(category)) {
                foreignMarkup.add(getElement("category", category));
            }
            Element guid = getElement("guid", newsBean.getCanonicalUUID());
            guid.setAttribute("isPermaLink", "false");
            foreignMarkup.add(guid);

            String formatted = dateFormat.format(newsBean.getLastModified());
            foreignMarkup.add(getElement("pubDate", formatted));
            HstLink hstLink = context.getHstLinkCreator().create(bean, context);
            String urlNews = hstLink.toUrlForm(context, true);
            foreignMarkup.add(getElement("link", urlNews));

            String tempUrl = urlNews.substring(0, urlNews.indexOf(context.getBaseURL().getHostName()));
            String finalUrl = tempUrl + context.getBaseURL().getHostName() + context.getHstLinkCreator().getBinariesPrefix();
            final LeadImageSection leadimagesection = newsBean.getLeadimagesection();
            if (leadimagesection != null && leadimagesection.getLeadImage() != null) {
                String imageUrl = finalUrl + leadimagesection.getLeadImage().getCanonicalHandlePath();
                Element imageElement = getImageElement(imageUrl);
                foreignMarkup.add(imageElement);
            }
            Element source = getElement("source", "NHS Digital");
            source.setAttribute("url", "https://digital.nhs.uk");
            foreignMarkup.add(source);

        } else if (bean instanceof Publication) {
            List<Element> foreignMarkup = entry.getForeignMarkup();
            final Publication publicationBean = (Publication) bean;
            String docPath = bean.getPath();
            if (docPath.contains("/publication-system/")) {
                foreignMarkup.add(getElement("title", publicationBean.getTitle()));
                foreignMarkup.add(getElement("author", "enquiries@nhsdigital.nhs.uk (NHS Digital)"));
                StringBuilder description = new StringBuilder();
                for (uk.nhs.digital.ps.beans.structuredText.Element tempDesc : publicationBean.getSummary().getElements()) {
                    description.append(tempDesc);
                }
                foreignMarkup.add(getElement("description", description.toString()));

                String category = getCategory(publicationBean);
                if (StringUtils.isNotBlank(category)) {
                    foreignMarkup.add(getElement("category", category));
                }

                Element guid = getElement("guid", publicationBean.getCanonicalUUID());
                guid.setAttribute("isPermaLink", "false");
                foreignMarkup.add(guid);
                String formatted = dateFormat.format(publicationBean.getLastModified());
                foreignMarkup.add(getElement("pubDate", formatted));
                HstLink hstLink = context.getHstLinkCreator().create(bean, context);
                String url = hstLink.toUrlForm(context, true);
                foreignMarkup.add(getElement("link", url));

                String tempUrl1 = url.substring(0, url.indexOf(context.getBaseURL().getHostName()));
                String finalUrl = tempUrl1 + context.getBaseURL().getHostName() + context.getHstLinkCreator().getBinariesPrefix();
                for (Infographic test : publicationBean.getKeyFactInfographics()) {
                    if (test.getIcon() != null) {
                        foreignMarkup.add(getImageElement(finalUrl + test.getIcon().getCanonicalHandlePath()));
                    }
                }

                Element source = getElement("source", "NHS Digital");
                source.setAttribute("url", "https://digital.nhs.uk");
                foreignMarkup.add(source);

            }
        } else if (bean instanceof CyberAlert) {
            List<Element> foreignMarkup = entry.getForeignMarkup();
            final CyberAlert cyberAlertBean = (CyberAlert) bean;
            foreignMarkup.add(getElement("title", cyberAlertBean.getThreatId() + " - " + cyberAlertBean.getTitle()));
            foreignMarkup.add(getElement("category", cyberAlertBean.getSeverity()));
            foreignMarkup.add(getElement("author", "enquiries@nhsdigital.nhs.uk (NHS Digital)"));

            foreignMarkup.add(getElement("description",
                "<p>Severity: " + cyberAlertBean.getSeverity() + "</p>"
                    + " " + cyberAlertBean.getSummary().getContent()
                    + "<p> " + cyberAlertBean.getShortsummary()
                    + "</p><p> Updated: " + lastUpdatedDateFormat.format(cyberAlertBean.getLastModified()) + "</p>"));

            Element guid = getElement("guid", cyberAlertBean.getCanonicalUUID());
            guid.setAttribute("isPermaLink", "false");
            foreignMarkup.add(guid);

            String pubDate = dateFormat.format(cyberAlertBean.getPublishedDate().getTime());
            foreignMarkup.add(getElement("pubDate", pubDate));

            HstLink hstLink = context.getHstLinkCreator().create(bean, context);
            String url = hstLink.toUrlForm(context, true);
            foreignMarkup.add(getElement("link", url));
        }

    }

    @NotNull
    private Element getImageElement(String imageUrl) {
        final Element element
            = new Element("thumbnail", "media", "http://search.yahoo.com/mrss/");
        element.setAttribute("url", imageUrl);
        return element;
    }

    private String getNewsCategory(News newsBean) {
        StringBuilder category = new StringBuilder();

        if (newsBean.getTopics() != null && newsBean.getTopics().length > 0) {
            category.append(Arrays.toString(newsBean.getTopics()));
        }
        if (newsBean.getRelateddocuments() != null && newsBean.getRelateddocuments().size() > 0) {
            for (HippoBean bean : newsBean.getRelateddocuments()) {
                if (bean.getSingleProperty("website:title") != null) {
                    category.append(",").append(bean.getSingleProperty("website:title").toString());
                }
            }

        }
        if (newsBean.getPeoplementioned() != null && newsBean.getPeoplementioned().size() > 0) {
            for (HippoBean tempBean : newsBean.getPeoplementioned()) {
                Person person = (Person) tempBean;
                if (person.getPersonalinfos() != null) {
                    category.append(",").append(person.getPersonalinfos().getFirstname());
                }
            }
        }
        return category.toString();
    }

    private String getCategory(Publication publicationBean) {
        HippoBean parentDocument = publicationBean.getParentDocument();
        if (parentDocument instanceof Series) {
            Series series = (Series) parentDocument;
            StringBuilder category = new StringBuilder();
            try {
                if (series.getFullTaxonomyList() != null && series.getFullTaxonomyList().size() > 0) {
                    category.append(Arrays.toString(series.getFullTaxonomyList().toArray()));
                }
            } catch (Exception ex) {
                LOGGER.warn(" Failed to get the Full Taxonomy.", ex.getMessage());
            }
            if (series.getInformationType() != null && series.getInformationType().length > 0) {
                category.append(Arrays.toString(series.getInformationType()));
            }
            if (series.getGranularity() != null && series.getGranularity().length > 0) {
                category.append(Arrays.toString(series.getGranularity()));
            }
            if (series.getGeographicCoverage() != null && series.getGeographicCoverage().length > 0) {
                category.append(Arrays.toString(Arrays.stream(series.getGeographicCoverage()).toArray()));
            }
            return category.toString();
        } else {
            return "";
        }
    }

    @NotNull
    private Element getElement(String name, String value) {
        Element lmElement;
        lmElement = new Element(name);
        lmElement.addContent(value);

        return lmElement;
    }

}
