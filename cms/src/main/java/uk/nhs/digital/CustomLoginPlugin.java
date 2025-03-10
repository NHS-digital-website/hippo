package uk.nhs.digital;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;

public class CustomLoginPlugin extends org.onehippo.forge.resetpassword.login.CustomLoginPlugin {

    public CustomLoginPlugin(IPluginContext context, IPluginConfig config) {
        super(context, config);

        // Check if the message should be displayed based on configuration
        boolean showMessage = config.getAsBoolean("show.message", false);

        // Create a container for the alert banner
        WebMarkupContainer alertBanner = new WebMarkupContainer("alertBanner");
        alertBanner.setVisible(showMessage);

        // Only add the title and body labels if showMessage is true
        if (showMessage) {
            String title = config.get("message.title").toString();
            String body = config.get("message.body").toString();

            Label titleLabel = new Label("alertBannerMessageTitle", Model.of(title));
            titleLabel.add(AttributeModifier.replace("class", "alert-banner-title"));

            Label bodyLabel = new Label("alertBannerMessageBody", Model.of(body));
            bodyLabel.add(AttributeModifier.replace("class", "alert-banner-body"));

            alertBanner.add(titleLabel);
            alertBanner.add(bodyLabel);
        }

        // Add the alert banner container to the page
        add(alertBanner);
    }
}
