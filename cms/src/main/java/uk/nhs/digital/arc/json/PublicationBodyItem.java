package uk.nhs.digital.arc.json;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemChartSection;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemImagesection;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemMapsection;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemTextsection;
import uk.nhs.digital.arc.json.website.*;

/**
 * This class maps all the sub-types that we might expect to load in the body element of a publication page
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "section_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PublicationsystemImagesection.class, name = "publicationsystem_imagesection"),
        @JsonSubTypes.Type(value = PublicationsystemChartSection.class, name = "publicationsystem_chartsection"),
        @JsonSubTypes.Type(value = WebsiteDynamicChartSection.class, name = "website_dynamicchartsection"),
        @JsonSubTypes.Type(value = PublicationsystemMapsection.class, name = "publicationsystem_mapsection"),
        @JsonSubTypes.Type(value = PublicationsystemTextsection.class, name = "publicationsystem_textsection"),
        @JsonSubTypes.Type(value = WebsiteSection.class, name = "website_section"),
        @JsonSubTypes.Type(value = WebsiteChecklist.class, name = "website_checklist"),
        @JsonSubTypes.Type(value = WebsiteCtabutton.class, name = "website_ctabutton"),
        @JsonSubTypes.Type(value = WebsiteDownload.class, name = "website_download"),
        @JsonSubTypes.Type(value = WebsiteEmphasis.class, name = "website_emphasis"),
        @JsonSubTypes.Type(value = WebsiteExpander.class, name = "website_expander"),
        @JsonSubTypes.Type(value = WebsiteGalleryItem.class, name = "website_galleryitem"),
        @JsonSubTypes.Type(value = WebsiteGallerySection.class, name = "website_gallerysection"),
        @JsonSubTypes.Type(value = WebsiteIconList.class, name = "website_iconlist"),
        @JsonSubTypes.Type(value = WebsiteIconListItem.class, name = "website_iconlistitem"),
        @JsonSubTypes.Type(value = WebsiteInfographic.class, name = "key_facts_infographics"),
        @JsonSubTypes.Type(value = WebsiteInfographic.class, name = "website_infographic"),
        @JsonSubTypes.Type(value = WebsiteInternalLink.class, name = "website_internallink"),
        @JsonSubTypes.Type(value = WebsiteExternalLink.class, name = "website_externallink"),
        @JsonSubTypes.Type(value = WebsiteAssetlink.class, name = "website_assetlink"),
        @JsonSubTypes.Type(value = WebsiteNavigation.class, name = "website_navigation"),
        @JsonSubTypes.Type(value = WebsiteNavigationtile.class, name = "website_navigationtile"),
        @JsonSubTypes.Type(value = WebsiteStatistics.class, name = "website_statistics"),
        @JsonSubTypes.Type(value = WebsiteStatisticsFeedItem.class, name = "website_statisticsfeeditem"),
        @JsonSubTypes.Type(value = WebsiteTableau.class, name = "website_tableau")
    })

public class PublicationBodyItem extends BasicBodyItem{
}
