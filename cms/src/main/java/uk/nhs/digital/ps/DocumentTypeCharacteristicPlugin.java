package uk.nhs.digital.ps;

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

@ExtClass("Example.DocumentTypeCharacteristicPlugin")
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
            "uk/nhs/digital/ps/documents-icon.png");
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
        documentTypes.add(createDocumentType("News",
            "myhippoproject:newsdocument"));
        documentTypes.add(createDocumentType("Simple Content",
            "myhippoproject:contentdocument"));
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
