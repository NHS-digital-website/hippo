package uk.nhs.digital.common.dynamicdropdown;

import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.forge.selection.frontend.model.ListItem;
import org.onehippo.forge.selection.frontend.model.ValueList;
import org.onehippo.forge.selection.frontend.plugin.DynamicDropdownPlugin;
import org.onehippo.forge.selection.frontend.utils.SelectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.RowIterator;


public class RoadmapItemDynamicDropDownPlugin extends DynamicDropdownPlugin {

    private static final Logger LOG = LoggerFactory.getLogger(RoadmapItemDynamicDropDownPlugin.class);
    private static final String NODE_TYPE_ROADMAP_CATEGORIES = "website:roadmapCategories";
    private static final String NODE_TYPE_ROADMAP_CATEGORIES_NAME = "website:categoryName";

    public RoadmapItemDynamicDropDownPlugin(IPluginContext context, IPluginConfig config) {
        super(context, config);

    }

    @Override
    protected ValueList getValueList(String valueListName) {
        try {
            return populateCategoryList();
        } catch (Exception e) {
            LOG.error(" Can not retrieve categories {}", e.getMessage());
        }
        LOG.debug(" No categories defined ");
        return new ValueList();
    }

    private ValueList populateCategoryList() throws
        RepositoryException {
        ValueList dropDownListValues = new ValueList();
        final HstRequestContext context = RequestContextProvider.get();
        QueryManager jcrQueryManager = context.getSession().getWorkspace().getQueryManager();
        Query jcrQuery = jcrQueryManager.createQuery(generateQuery(getDocumentHandleId().getParent().getPath(), getDocumentHandleId().getIdentifier()), "xpath");
        LOG.debug("Value of Query {} ", jcrQuery.getStatement());
        QueryResult queryResult = jcrQuery.execute();
        RowIterator rowIterator = queryResult.getRows();
        while (rowIterator.hasNext()) {
            Node node = rowIterator.nextRow().getNode();
            if (node.getParent().getProperty("hippostd:state").getValue().getString().equals("published")) {
                NodeIterator nodeIterator = node.getParent().getNodes(NODE_TYPE_ROADMAP_CATEGORIES);

                while (nodeIterator.hasNext()) {
                    Node parentNode = nodeIterator.nextNode();
                    String category = parentNode.getProperty(NODE_TYPE_ROADMAP_CATEGORIES_NAME).getValue().getString();
                    ListItem item1 = new ListItem(category, category);
                    dropDownListValues.add(item1);
                }
            }
        }
        return dropDownListValues;
    }

    private String generateQuery(String contentPath, String documentId) {
        return "/jcr:root/" + contentPath + "//*[@hippo:docbase='" + documentId + "']";
    }

    private Node getDocumentHandleId() throws RepositoryException {
        return SelectionUtils.getNode(getModel()).getParent();
    }
}
