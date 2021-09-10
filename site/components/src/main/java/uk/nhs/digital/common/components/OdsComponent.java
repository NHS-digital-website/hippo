package uk.nhs.digital.common.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;

import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.digital.model.OdsOrganisation;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;


public class OdsComponent extends CommonComponent {
    private List<OdsOrganisation> odsResults = null;

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        HstRequestContext requestContext = request.getRequestContext();
        if (odsResults == null) {
            try {
                QueryManager jcrQueryManager = requestContext.getSession().getWorkspace().getQueryManager();
                Query jcrQuery = jcrQueryManager.createQuery("/jcr:root/content/assets//*[@jcr:primaryType='externalstorage:resource']", "xpath");
                QueryResult queryResult = jcrQuery.execute();
                NodeIterator nodeIterator = queryResult.getNodes();
                while (nodeIterator.hasNext()) {
                    Node node = nodeIterator.nextNode();
                    if (node.getPath().contains("/content/assets/ODS_Data")) {
                        Value val = node.getProperty("jcr:data").getValue();
                        // convert JSON array to Java List
                        odsResults = new ObjectMapper().readValue(val.getString().replace("\n", ""), new TypeReference<List<OdsOrganisation>>() {
                        });
                        break;
                    }
                }
            } catch (RepositoryException | JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        if (odsResults != null) {
            request.setAttribute("odsResults", odsResults);
        }

    }
}
