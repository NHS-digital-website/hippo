package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;


@HippoEssentialsGenerated(internalName = "website:location")
@Node(jcrType = "website:location")
public class Location extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "website:name")
    public String getName() {
        return getProperty("website:name");
    }

    @HippoEssentialsGenerated(internalName = "website:description")
    public String getDescription() {
        return getProperty("website:description");
    }

    @HippoEssentialsGenerated(internalName = "website:locaddress")
    public List<HippoBean> getLocaddress() {
        return getChildBeansByName("website:locaddress");
    }

    @HippoEssentialsGenerated(internalName = "website:onsitecarsparking")
    public OnSiteCarParking getOnsitecarsparking() {
        return getBean("website:onsitecarsparking", OnSiteCarParking.class);
    }

    @HippoEssentialsGenerated(internalName = "website:publictransportstations")
    public List<HippoBean> getPublictransportstations() {
        return getChildBeansByName("website:publictransportstations");
    }

    @HippoEssentialsGenerated(internalName = "website:cyclesparking")
    public CycleParking getCyclesparking() {
        return getBean("website:cyclesparking", CycleParking.class);
    }

    @HippoEssentialsGenerated(internalName = "website:directiontositebycars")
    public DirectionToSiteByCar getDirectiontositebycars() {
        return getBean("website:directiontositebycars", DirectionToSiteByCar.class);
    }

    @HippoEssentialsGenerated(internalName = "website:directiontositebytrains")
    public DirectionToSiteByTrain getDirectiontositebytrains() {
        return getBean("website:directiontositebytrains", DirectionToSiteByTrain.class);
    }

    @HippoEssentialsGenerated(internalName = "website:directiontositebypublictransportes")
    public DirectionToSiteByPublicTransport getDirectiontositebypublictransportes() {
        return getBean("website:directiontositebypublictransportes", DirectionToSiteByPublicTransport.class);
    }

    @HippoEssentialsGenerated(internalName = "website:telephone")
    public String getTelephone() {
        return getProperty("website:telephone");
    }

    @HippoEssentialsGenerated(internalName = "website:emailaddress")
    public String getEmailaddress() {
        return getProperty("website:emailaddress");
    }

    @HippoEssentialsGenerated(internalName = "website:odscode")
    public String getOdscode() {
        return getProperty("website:odscode");
    }

    @HippoEssentialsGenerated(internalName = "website:dunsnumber")
    public String getDunsnumber() {
        return getProperty("website:dunsnumber");
    }

    @HippoEssentialsGenerated(internalName = "website:urgentinformation")
    public HippoHtml getUrgentinformation() {
        return getHippoHtml("website:urgentinformation");
    }

    @HippoEssentialsGenerated(internalName = "website:globallocationnumber")
    public String getGloballocationnumber() {
        return getProperty("website:globallocationnumber");
    }

    @HippoEssentialsGenerated(internalName = "website:opentopublic")
    public Boolean getOpentopublic() {
        return getProperty("website:opentopublic");
    }

    @HippoEssentialsGenerated(internalName = "website:imageofsite")
    public HippoGalleryImageSet getImageofsite() {
        return getLinkedBean("website:imageofsite", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:uniquepropertyreferencenumber")
    public Long getUniquepropertyreferencenumber() {
        return getProperty("website:uniquepropertyreferencenumber");
    }

    @HippoEssentialsGenerated(internalName = "website:nhsdigitalsite")
    public Boolean getNhsdigitalsite() {
        return getProperty("website:nhsdigitalsite");
    }

    @HippoEssentialsGenerated(internalName = "website:suppresssearchengines")
    public Boolean getSuppresssearchengines() {
        return getProperty("website:suppresssearchengines");
    }

    @HippoEssentialsGenerated(internalName = "website:pluscode")
    public String getPluscode() {
        return getProperty("website:pluscode");
    }

    @HippoEssentialsGenerated(internalName = "website:locationwebsite")
    public List<?> getLocationwebsite() {
        return getChildBeansByName("website:locationwebsite");
    }

    @HippoEssentialsGenerated(internalName = "website:threewordskey")
    public String getThreewordskey() {
        return getProperty("website:threewordskey");
    }

    @HippoEssentialsGenerated(internalName = "website:localtaxis")
    public List<HippoBean> getLocaltaxis() {
        return getChildBeansByName("website:localtaxis");
    }

    @HippoEssentialsGenerated(internalName = "website:locopeninghours")
    public List<HippoBean> getLocopeninghours() {
        return getChildBeansByName("website:locopeninghours");
    }

    @HippoEssentialsGenerated(internalName = "website:publictransportroutes")
    public List<HippoBean> getPublictransportroutes() {
        return getChildBeansByName("website:publictransportroutes");
    }

}
