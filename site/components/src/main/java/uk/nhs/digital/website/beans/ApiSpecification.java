package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@Node(jcrType = "website:apispecification")
@HippoEssentialsGenerated(internalName = "website:apispecification", allowModifications = false)
public class ApiSpecification extends CommonFieldsBean {

    /**
     * Compatibility override for legacy callers that still expect taxonomy via
     * {@code getKeys()} from the CommonFieldsBean hierarchy.
     *
     * <p>ApiSpecification taxonomy is now stored in
     * {@code website:taxonomyClassificationField}. New development should use
     * the doctype-specific taxonomy field directly rather than this legacy
     * accessor.
     *
     * @deprecated use {@link #getTaxonomyClassificationField()} for new code
     */
    @Deprecated
    @Override
    public String[] getKeys() {
        return getTaxonomyClassificationField();
    }

    public String[] getTaxonomyClassificationField() {
        return getMultipleProperty("website:taxonomyClassificationField");
    }

    public String getSpecificationId() {
        return getSingleProperty("website:specification_id");
    }

    public String getHtml() {
        return getSingleProperty("website:html");
    }

    public String getJson() {
        return getSingleProperty("website:json");
    }
}
