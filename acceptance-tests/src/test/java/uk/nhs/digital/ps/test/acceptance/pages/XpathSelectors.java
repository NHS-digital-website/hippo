package uk.nhs.digital.ps.test.acceptance.pages;

public class XpathSelectors  {

    /** This is the dark gray left menu with links to: Dashboard, Channels, Content, ... */
    public final static String TABBED_PANEL = "//div[contains(@class, 'tabbed-panel-layout-left')]";

    /** This is the navigation pane with documents */
    public final static String NAVIGATION_CENTRE = "//div[@class='navigator-center-body']";

    /** This is the navigation pane with folders */
    public final static String NAVIGATION_LEFT = "//div[@class='navigator-left-body']";

    public final static String EDITOR_BODY = "//div[contains(@class, 'browse-perspective-center-body')]//div[contains(@style, 'block')]//div[contains(@class, 'hippo-editor') and contains(@class, 'perspective')]";
    public final static String TAXONOMY_PICKER = "//div[contains(@class, 'wicket-modal') and contains(@aria-labelledby, 'Taxonomy picker')]";
}
