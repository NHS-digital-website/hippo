package uk.nhs.digital.arc.transformer.abs;

import uk.nhs.digital.arc.storage.ArcStorageManager;

import javax.jcr.Session;

public abstract class AbstractSectionTransformer extends AbstractTransformer {

    public static final String WEBSITE = "website";
    public static final String WEBSITE_DESCRIPTION = "website:description";
    public static final String WEBSITE_EXTERNALLINK = "website:externallink";
    public static final String WEBSITE_HEADING = "website:heading";
    public static final String WEBSITE_HEADINGLEVEL = "website:headinglevel";
    public static final String WEBSITE_ICONLIST = "website:iconlist";
    public static final String WEBSITE_ICONLISTITEM = "website:iconlistitem";
    public static final String WEBSITE_ICONLISTITEMS = "website:iconlistitems";
    public static final String WEBSITE_INTERNALLINK = "website:internallink";
    public static final String WEBSITE_ITEMLINK = "website:itemlink";
    public static final String WEBSITE_LINK = "website:link";
    public static final String WEBSITE_INTRODUCTION = "website:introduction";
    public static final String WEBSITE_TITLE = "website:title";
    public static final String WEBSITE_HTML = "website:html";
    public static final String WEBSITE_SECTION = "website:section";
    public static final String WEBSITE_SHORTSUMMARY = "website:shortsummary";

    public static final String WEBSITE_EXPLANATORYLINE = "website:explanatoryLine";
    public static final String WEBSITE_QUALIFYINGINFORMATION = "website:qualifyingInformation";
    public static final String WEBSITE_SECTIONS = "website:sections";
    public static final String WEBSITE_INFOGRAPHIC = "website:infographic";
    public static final String WEBSITE_IMAGE = "website:image";
    public static final String WEBSITE_COLOUR = "website:colour";
    public static final String WEBSITE_HEADLINE = "website:headline";

    protected AbstractSectionTransformer(Session session) {
        super.setSession(session);
    }

    protected AbstractSectionTransformer(Session session, String docbase, ArcStorageManager storageManager) {
        super.setSession(session);
        super.setDocbase(docbase);
        super.setStorageManager(storageManager);
    }
}
