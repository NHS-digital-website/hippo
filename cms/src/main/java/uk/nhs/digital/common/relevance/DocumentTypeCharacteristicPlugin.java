package uk.nhs.digital.common.relevance;

import com.onehippo.cms7.targeting.frontend.plugin.CharacteristicPlugin;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.json.JSONException;
import org.json.JSONObject;
import org.wicketstuff.js.ext.util.ExtClass;

import java.util.ArrayList;
import java.util.List;


/**
 * Add a Document Type Characteristic UI Plugin
 * https://documentation.bloomreach.com/trails/relevance-trail/3-add-a-document-type-characteristic.html
 */
@ExtClass("Relevance.DocumentTypeCharacteristicPlugin")
public class DocumentTypeCharacteristicPlugin extends CharacteristicPlugin {

    private static final JavaScriptResourceReference DOCTYPE_JS =
        new JavaScriptResourceReference(DocumentTypeCharacteristicPlugin.class,
            "DocumentTypeCharacteristicPlugin.js");

    public DocumentTypeCharacteristicPlugin(IPluginContext context,
                                            IPluginConfig config) {
        super(context, config);
    }

    @Override
    protected ResourceReference getIcon() {
        return new PackageResourceReference(DocumentTypeCharacteristicPlugin.class,
            "documents-icon.png");
    }

    @Override
    public void renderHead(final Component component, final IHeaderResponse response) {
        super.renderHead(component, response);
        response.render(JavaScriptHeaderItem.forReference(DOCTYPE_JS));
    }

    @Override
    protected void onRenderProperties(final JSONObject properties)
        throws JSONException {
        super.onRenderProperties(properties);

        List<JSONObject> documentTypes = new ArrayList<JSONObject>();
        documentTypes.add(createDocumentType("Series / Collection","publicationsystem:series"));
        documentTypes.add(createDocumentType("Publication","publicationsystem:publication"));
        documentTypes.add(createDocumentType("Dataset","publicationsystem:dataset"));
        documentTypes.add(createDocumentType("Archive","publicationsystem:archive"));
        documentTypes.add(createDocumentType("Legacy publication","publicationsystem:legacypublication"));
        documentTypes.add(createDocumentType("Publication page","publicationsystem:publicationsystem:publicationPage"));
        documentTypes.add(createDocumentType("Indicator","publicationsystem:indicator"));

        documentTypes.add(createDocumentType("API endpoint","website:apiendpoint"));
        documentTypes.add(createDocumentType("API master","website:apimaster"));
        documentTypes.add(createDocumentType("API Specification","website:apispecification"));
        documentTypes.add(createDocumentType("Banner","website:bannerdocument"));
        documentTypes.add(createDocumentType("Blog","website:blog"));
        documentTypes.add(createDocumentType("Blog hub","website:bloghub"));
        documentTypes.add(createDocumentType("Call to action","website:calltoaction"));
        documentTypes.add(createDocumentType("Event","website:event"));
        documentTypes.add(createDocumentType("Feature","website:feature"));
        documentTypes.add(createDocumentType("GDPR Summary","website:gdprsummary"));
        documentTypes.add(createDocumentType("GDPR Transparency","website:gdprtransparency"));
        documentTypes.add(createDocumentType("General","website:general"));
        documentTypes.add(createDocumentType("General non searchable","website:generalnonsearch"));
        documentTypes.add(createDocumentType("Glossary list","website:glossarylist"));
        documentTypes.add(createDocumentType("Graphic block","website:graphicblock"));
        documentTypes.add(createDocumentType("Hub","website:hub"));
        documentTypes.add(createDocumentType("Links list","website:componentlist"));
        documentTypes.add(createDocumentType("Location","website:location"));
        documentTypes.add(createDocumentType("News","website:news"));
        documentTypes.add(createDocumentType("Person","website:person"));
        documentTypes.add(createDocumentType("Published work","website:publishedwork"));
        documentTypes.add(createDocumentType("Published work chapter","website:publishedworkchapter"));
        documentTypes.add(createDocumentType("Roadmap","website:roadmap"));
        documentTypes.add(createDocumentType("Roadmap item","website:roadmapitem"));
        documentTypes.add(createDocumentType("Service","website:service"));
        documentTypes.add(createDocumentType("Visual hub","website:visualhub"));

        properties.put("documentTypes", documentTypes);
    }

    JSONObject createDocumentType(String name, String jcrType)
        throws JSONException {
        JSONObject documentType = new JSONObject();
        documentType.put("type", jcrType);
        documentType.put("name", name);
        return documentType;
    }

}
