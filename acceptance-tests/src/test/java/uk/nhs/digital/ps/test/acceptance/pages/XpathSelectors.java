package uk.nhs.digital.ps.test.acceptance.pages;

public class XpathSelectors  {

    /**
     * This is the dark gray left menu with links to: Dashboard, Channels, Content, ...
     */
    public static final String TABBED_PANEL = "//div[contains(@class, 'tabbed-panel-layout-left')]";

    /**
     * This is the navigation pane with documents
     */
    public static final String NAVIGATION_CENTRE = "//div[@class='navigator-center-body']";

    /**
     * This is the navigation pane with folders
     */
    public static final String NAVIGATION_LEFT = "//div[@class='navigator-left-body']";

    public static final String EDITOR_BODY =
        "//div[contains(@class, 'browse-perspective-center-body')]"
            + "//div[contains(@style, 'block')]"
            + "//div[contains(@class, 'hippo-editor') and contains(@class, 'perspective')]";

    public static final String TAXONOMY_PICKER = "//div[contains(@class, 'wicket-modal') and contains(@aria-labelledby, 'Taxonomy picker')]";
}
