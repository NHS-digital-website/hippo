package uk.nhs.digital.common.relevance;

import com.onehippo.cms7.targeting.frontend.plugin.CollectorPlugin;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.session.UserSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wicketstuff.js.ext.util.ExtClass;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.Query;

@ExtClass("Relevance.PageViewsCollectorPlugin")
public class PageViewsCollectorPlugin extends CollectorPlugin {

    private List<Pattern> excludes;

    private static final JavaScriptResourceReference DOCTYPE_JS =
        new JavaScriptResourceReference(PageViewsCollectorPlugin.class,
            "PageViewsCollectorPlugin.js");

    public PageViewsCollectorPlugin(final IPluginContext context,
                                 final IPluginConfig config) {
        super(context, config);
        final String[] excludesConfig = config.getStringArray("excludes");
        excludes = new ArrayList<Pattern>();
        if (excludesConfig != null) {
            for (String exclude : excludesConfig) {
                excludes.add(Pattern.compile(exclude));
            }
        }
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
        try {
            properties.put("pageViews", fetchPageViews());
        } catch (RepositoryException e) {
            throw new JSONException(e);
        }
    }

    private List<JSONObject> fetchPageViews() throws RepositoryException, JSONException {
        final Session session = UserSession.get().getJcrSession();

        final StringBuilder statement = new StringBuilder();
        statement.append("/jcr:root/targeting:targeting/targeting:characteristics/pageviews/element");
        statement.append("(*, ").append("targeting:targetgroup").append(")");
        statement.append(" order by @targeting:name");

        final Query q = session.getWorkspace().getQueryManager()
                           .createQuery(statement.toString(), Query.XPATH);

        final List<JSONObject> pageViews = new ArrayList<JSONObject>();
        final NodeIterator nodes = q.execute().getNodes();
        while (nodes.hasNext()) {
            Node node = nodes.nextNode();
            final String label = node.getProperty("targeting:name").getString();
            if (!isExcluded(label)) {
                final Value[] urlValues = node.getProperty("targeting:propertynames").getValues();
                JSONArray urls = new JSONArray(); 
                for (Value url : urlValues) {
                    urls.put(url.getString());
                }
                pageViews.add(createPageView(label, urls));
            }
        }

        return pageViews;
    }

    private JSONObject createPageView(String label, JSONArray urls)
        throws JSONException {
        JSONObject pageView = new JSONObject();
        pageView.put("label", label);
        pageView.put("urls", urls);
        return pageView;
    }

    private boolean isExcluded(final String label) {
        for (Pattern exclude : excludes) {
            if (exclude.matcher(label).matches()) {
                return true;
            }
        }
        return false;
    }
}
