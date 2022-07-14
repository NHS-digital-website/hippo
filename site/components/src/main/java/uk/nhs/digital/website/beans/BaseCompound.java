package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;
import java.util.List;

public abstract class BaseCompound extends HippoCompound {

    public abstract String getTitle();

    @HippoEssentialsGenerated(internalName = "website:categorytitle")
    public String getCategoryTitle() {
        return getSingleProperty("website:categorytitle");
    }

    @HippoEssentialsGenerated(internalName = "website:categorycontent")
    public HippoHtml getCategoryContent() {
        return getHippoHtml("website:categorycontent");
    }

    @HippoEssentialsGenerated(internalName = "website:categorylabel")
    public String getCategoryLabel() {
        return getSingleProperty("website:categorylabel");
    }

    @HippoEssentialsGenerated(internalName = "website:link")
    public String getCategoryPageLink() {
        return getSingleProperty("website:link");
    }

    @HippoEssentialsGenerated(internalName = "website:buttontype")
    public String getButtonType() {
        return getSingleProperty("website:buttontype");
    }

    @HippoEssentialsGenerated(internalName = "website:items")
    public List<?> getItems() {
        return getChildBeansByName("website:items");
    }

    @HippoEssentialsGenerated(internalName = "website:alignment")
    public String getAlignment() {
        return getSingleProperty("website:alignment");
    }

    @HippoEssentialsGenerated(internalName = "website:portfolioCode")
    public String getPortfolioCode() {
        return getSingleProperty("website:portfolioCode");
    }

    @HippoEssentialsGenerated(internalName = "website:responsibleTeam")
    public String getResponsibleTeam() {
        return getSingleProperty("website:responsibleTeam");
    }

    @HippoEssentialsGenerated(internalName = "website:userNeedismet")
    public String[] getUserNeedismet() {
        return getMultipleProperty("website:userNeedismet");
    }

    @HippoEssentialsGenerated(internalName = "website:briefDescription")
    public HippoHtml getBriefDescription() {
        return getHippoHtml("website:briefDescription");
    }

    @HippoEssentialsGenerated(internalName = "website:name")
    public String getName() {
        return getSingleProperty("website:name");
    }

    @HippoEssentialsGenerated(internalName = "website:parametertype")
    public String getParametertype() {
        return getSingleProperty("website:parametertype");
    }

    @HippoEssentialsGenerated(internalName = "website:ismandatory")
    public Boolean getIsmandatory() {
        return getSingleProperty("website:ismandatory");
    }

    @HippoEssentialsGenerated(internalName = "website:path")
    public String getPath() {
        return getSingleProperty("website:path");
    }

    @HippoEssentialsGenerated(internalName = "website:description")
    public HippoHtml getDescription() {
        return getHippoHtml("website:description");
    }

    @HippoEssentialsGenerated(internalName = "website:howtofixthis")
    public HippoHtml getHowtofixthis() {
        return getHippoHtml("website:howtofixthis");
    }

    @HippoEssentialsGenerated(internalName = "website:meaning")
    public String getMeaning() {
        return getSingleProperty("website:meaning");
    }

    @HippoEssentialsGenerated(internalName = "website:httpcode")
    public String getHttpcode() {
        return getSingleProperty("website:httpcode");
    }

    @HippoEssentialsGenerated(internalName = "website:typeoferror")
    public String getTypeoferror() {
        return getSingleProperty("website:typeoferror");
    }

    @HippoEssentialsGenerated(internalName = "website:profbiography")
    public HippoHtml getProfbiography() {
        return getHippoHtml("website:profbiography");
    }

    @HippoEssentialsGenerated(internalName = "website:prevpositions")
    public HippoHtml getPrevpositions() {
        return getHippoHtml("website:prevpositions");
    }

    @HippoEssentialsGenerated(internalName = "website:nonnhspositions")
    public HippoHtml getNonnhspositions() {
        return getHippoHtml("website:nonnhspositions");
    }

    @HippoEssentialsGenerated(internalName = "website:additionalbiography")
    public HippoHtml getAdditionalbiography() {
        return getHippoHtml("website:additionalbiography");
    }

    @HippoEssentialsGenerated(internalName = "website:personalbiography")
    public HippoHtml getPersonalbiography() {
        return getHippoHtml("website:personalbiography");
    }

    @HippoEssentialsGenerated(internalName = "website:linkedinlink")
    public String getLinkedinlink() {
        return getSingleProperty("website:linkedinlink");
    }

    @HippoEssentialsGenerated(internalName = "website:twitteruser")
    public String getTwitteruser() {
        return getSingleProperty("website:twitteruser");
    }

    @HippoEssentialsGenerated(internalName = "website:hellomynameis")
    public String getHellomynameis() {
        return getSingleProperty("website:hellomynameis");
    }

    @HippoEssentialsGenerated(internalName = "website:twitterhashtags")
    public HippoHtml getTwitterhashtags() {
        return getHippoHtml("website:twitterhashtags");
    }

    @HippoEssentialsGenerated(internalName = "website:othersocialmedias")
    public List<HippoBean> getOthersocialmedias() {
        return getChildBeansByName("website:othersocialmedias");
    }

    @HippoEssentialsGenerated(internalName = "website:heading")
    public String getHeading() {
        return getSingleProperty("website:heading");
    }

    @HippoEssentialsGenerated(internalName = "website:headingType")
    public String getHeadingType() {
        return getSingleProperty("website:headingType");
    }

    @HippoEssentialsGenerated(internalName = "website:tableFormat")
    public String getTableFormat() {
        return getSingleProperty("website:tableFormat");
    }

    @HippoEssentialsGenerated(internalName = "website:introduction")
    public HippoHtml getIntroduction() {
        return getHippoHtml("website:introduction");
    }

    @HippoEssentialsGenerated(internalName = "website:property")
    public String getPropertyDetails() {
        return getSingleProperty("website:property");
    }

    @HippoEssentialsGenerated(internalName = "website:userContactPhone")
    public String getUserContactPhone() {
        return getSingleProperty("website:userContactPhone");
    }

    @HippoEssentialsGenerated(internalName = "website:userContactemail")
    public String getUserContactemail() {
        return getSingleProperty("website:userContactemail");
    }

    @HippoEssentialsGenerated(internalName = "website:userContactname")
    public String getUserContactname() {
        return getSingleProperty("website:userContactname");
    }

    @HippoEssentialsGenerated(internalName = "website:userContactprimarycontact")
    public Boolean getUserContactprimarycontact() {
        return getSingleProperty("website:userContactprimarycontact");
    }

    @HippoEssentialsGenerated(internalName = "website:userContactnotes")
    public HippoHtml getUserContactnotes() {
        return getHippoHtml("website:userContactnotes");
    }

    @HippoEssentialsGenerated(internalName = "website:prefix")
    public String getPrefix() {
        return getSingleProperty("website:prefix");
    }

    @HippoEssentialsGenerated(internalName = "website:suffix")
    public String getSuffix() {
        return getSingleProperty("website:suffix");
    }

    @HippoEssentialsGenerated(internalName = "website:trend")
    public String getTrend() {
        return getSingleProperty("website:trend");
    }

    @HippoEssentialsGenerated(internalName = "website:headlineDescription")
    public HippoHtml getHeadlineDescription() {
        return getHippoHtml("website:headlineDescription");
    }

    @HippoEssentialsGenerated(internalName = "website:furtherQualifyingInformation")
    public HippoHtml getFurtherQualifyingInformation() {
        return getHippoHtml("website:furtherQualifyingInformation");
    }

    @HippoEssentialsGenerated(internalName = "website:quote")
    public HippoHtml getQuote() {
        return getHippoHtml("website:quote");
    }

    @HippoEssentialsGenerated(internalName = "website:person")
    public String getPerson() {
        return getSingleProperty("website:person");
    }

    @HippoEssentialsGenerated(internalName = "website:role")
    public String getRole() {
        return getSingleProperty("website:role");
    }

    @HippoEssentialsGenerated(internalName = "website:organisation")
    public String getOrganisation() {
        return getSingleProperty("website:organisation");
    }

    @HippoEssentialsGenerated(internalName = "website:status")
    public String getStatus() {
        return getSingleProperty("website:status");
    }

    @HippoEssentialsGenerated(internalName = "website:completeddate")
    public Calendar getCompletedDate() {
        return getSingleProperty("website:completeddate");
    }

    @HippoEssentialsGenerated(internalName = "website:supersededdate")
    public Calendar getSupersededDate() {
        return getSingleProperty("website:supersededdate");
    }

    @HippoEssentialsGenerated(internalName = "website:progress")
    public String getProgress() {
        return getSingleProperty("website:progress");
    }

    @HippoEssentialsGenerated(internalName = "website:responseitem")
    public String getResponseitem() {
        return getSingleProperty("website:responseitem");
    }

    @HippoEssentialsGenerated(internalName = "website:customdatatype")
    public String getCustomdatatype() {
        return getSingleProperty("website:customdatatype");
    }

    @HippoEssentialsGenerated(internalName = "website:datatype")
    public String getDatatype() {
        return getSingleProperty("website:datatype");
    }

    @HippoEssentialsGenerated(internalName = "website:definition")
    public HippoHtml getDefinition() {
        return getHippoHtml("website:definition");
    }

    @HippoEssentialsGenerated(internalName = "website:external")
    public String getExternal() {
        return getSingleProperty("website:external");
    }

    @HippoEssentialsGenerated(internalName = "website:internal")
    public HippoBean getInternal() {
        return getLinkedBean("website:internal", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:backgroundcolor")
    public String getBackgroundcolor() {
        return getSingleProperty("website:backgroundcolor");
    }

    @HippoEssentialsGenerated(internalName = "website:iconcolor")
    public String getIconcolor() {
        return getSingleProperty("website:iconcolor");
    }

    @HippoEssentialsGenerated(internalName = "website:fontcolor")
    public String getFontcolor() {
        return getSingleProperty("website:fontcolor");
    }

    @HippoEssentialsGenerated(internalName = "website:icon")
    public CorporateWebsiteImageset getIcon() {
        return getLinkedBean("website:icon", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:publicationStyle")
    public String getPublicationStyle() {
        return getSingleProperty("website:publicationStyle");
    }

    @HippoEssentialsGenerated(internalName = "website:button")
    public String getButton() {
        return getSingleProperty("website:button");
    }

    @HippoEssentialsGenerated(internalName = "website:banneralttext")
    public String getImageAltText() {
        return getSingleProperty("website:banneralttext");
    }

    @HippoEssentialsGenerated(internalName = "website:bannerimage")
    public CorporateWebsiteImageset getBannerImage() {
        return getLinkedBean("website:bannerimage", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:department")
    public String getDepartment() {
        return getSingleProperty("website:department");
    }

    @HippoEssentialsGenerated(internalName = "website:email")
    public String getEmail() {
        return getSingleProperty("website:email");
    }


    @HippoEssentialsGenerated(internalName = "website:telephone")
    public String getTelephone() {
        return getSingleProperty("website:telephone");
    }

    @HippoEssentialsGenerated(internalName = "website:body")
    public String getBody() {
        return getSingleProperty("website:body");
    }

    @HippoEssentialsGenerated(internalName = "website:referencenumber")
    public String getReferenceNumber() {
        return getSingleProperty("website:referencenumber");
    }

    @HippoEssentialsGenerated(internalName = "website:weblink")
    public String getWebLink() {
        return getSingleProperty("website:weblink");
    }

    @HippoEssentialsGenerated(internalName = "website:available")
    public boolean getAvailable() {
        return getSingleProperty("website:available");
    }

    @HippoEssentialsGenerated(internalName = "website:numberofspaces")
    public Long getNumberofspaces() {
        return getSingleProperty("website:numberofspaces");
    }

    @HippoEssentialsGenerated(internalName = "website:details")
    public String getDetails() {
        return getSingleProperty("website:details");
    }

    public String getSectionType() {
        return "cycleparking";
    }

    @HippoEssentialsGenerated(internalName = "website:publictransporttype")
    public String getPublictransportType() {
        return getSingleProperty("website:publictransporttype");
    }

    @HippoEssentialsGenerated(internalName = "website:bypublictransporttext")
    public HippoHtml getBypublictransporttext() {
        return getHippoHtml("website:bypublictransporttext");
    }

    @HippoEssentialsGenerated(internalName = "website:publictransportstations")
    public List<HippoBean> getPublicTransportStations() {
        return getChildBeansByName("website:publictransportstations");
    }

    @HippoEssentialsGenerated(internalName = "website:content")
    public HippoHtml getContent() {
        return getHippoHtml("website:content");
    }

    @HippoEssentialsGenerated(internalName = "website:sections")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:sections");
    }


    @HippoEssentialsGenerated(internalName = "website:lawfulbasisfield")
    public HippoHtml getLawfulbasisfield() {
        return getHippoHtml("website:lawfulbasisfield");
    }

    @HippoEssentialsGenerated(internalName = "website:suppressdata")
    public String getSuppressdata() {
        return getSingleProperty("website:suppressdata");
    }

    @HippoEssentialsGenerated(internalName = "website:emailaddress", allowModifications = false)
    public String getEmailaddress() {
        return getSingleProperty("website:emailaddress");
    }

    @HippoEssentialsGenerated(internalName = "website:phonenumber", allowModifications = false)
    public String getPhonenumber() {
        return getSingleProperty("website:phonenumber");
    }
    
    @HippoEssentialsGenerated(internalName = "website:roleinteam")
    public String getRoleInTeam() {
        return getSingleProperty("website:roleinteam");
    }

}
