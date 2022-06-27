package uk.nhs.digital.arc.transformer.abs;

import uk.nhs.digital.arc.json.ArcDoctype;

/**
 * This class is the parent of transformer classes that work on Json classes that will go on to appear as 'pages' in the
 * Publication System in the CMS.
 *
 * Examples of these 'pages' include {@link uk.nhs.digital.arc.json.Publication}, {@link uk.nhs.digital.arc.json.Archive},
 * {@link uk.nhs.digital.arc.json.PublicationPage} and {@link uk.nhs.digital.arc.json.Dataset}.
 *
 * Each of those Json classes will be referenced by a Transformer class descended from this parent class (See, for example, the implementation
 * of {@link uk.nhs.digital.arc.transformer.impl.pagelevel.PublicationPageTransformer} for more information)
 */
public abstract class AbstractPageLevelTransformer extends AbstractTransformer {
    protected ArcDoctype doctype;

    public void setDoctype(ArcDoctype doctype) {
        this.doctype = doctype;
    }
}
