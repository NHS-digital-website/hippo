package uk.nhs.digital.arc.process;

import org.onehippo.forge.content.pojo.binder.ContentNodeBindingItemFilter;
import org.onehippo.forge.content.pojo.binder.jcr.DefaultJcrContentNodeBinder;
import org.onehippo.forge.content.pojo.common.ContentValueConverter;
import org.onehippo.forge.content.pojo.model.ContentItem;
import org.onehippo.forge.content.pojo.model.ContentNode;
import org.onehippo.forge.content.pojo.model.ContentProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.arc.transformer.abs.AbstractTransformer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

public class ArcJcrContentNodeBinder extends DefaultJcrContentNodeBinder {

    private static final Logger log = LoggerFactory.getLogger(ArcJcrContentNodeBinder.class);
    private List<ExternalStorageReference> references;

    @Override
    protected Value[] createJcrValuesFromContentProperty(Node jcrNode, ContentProperty contentProp, ContentValueConverter<Value> valueConverter) throws RepositoryException {
        return super.createJcrValuesFromContentProperty(jcrNode, contentProp, valueConverter);
    }

    @Override
    protected void removeSubNodes(Node jcrDataNode, ContentNode contentNode, ContentNodeBindingItemFilter<ContentItem> itemFilter) throws RepositoryException {
        findExternalStorageReferenceNodes(jcrDataNode);
        super.removeSubNodes(jcrDataNode,contentNode,itemFilter);
    }

    private void findExternalStorageReferenceNodes(Node jcrDataNode) throws RepositoryException {
        Set<String> subNodeNames = this.getChildExtAttachmentNodes(jcrDataNode);
        String[] nameGlobs = (String[]) subNodeNames.toArray(new String[subNodeNames.size()]);
        NodeIterator nodeIt = jcrDataNode.getNodes(nameGlobs);

        while (nodeIt.hasNext()) {
            Node nextNode = nodeIt.nextNode();

            NodeIterator resourceNodeIterator = nextNode.getNodes(AbstractTransformer.PUBLICATIONSYSTEM_ATTACHMENTRESOURCE);

            if (resourceNodeIterator.hasNext()) {
                //* Check for possible resourceNode and it's dependent children
                while (resourceNodeIterator.hasNext()) {
                    Node next = resourceNodeIterator.nextNode();
                    PropertyIterator props = next.getProperties(AbstractTransformer.EXTERNALSTROAGE_REFERENCE);

                    while (props.hasNext()) {
                        Property innerProp = props.nextProperty();
                        String propName = innerProp.getName();
                        String propValue = innerProp.getValue().getString();
                        String nodePath = next.getPath();

                        log.debug("The property: {} has a value of: {} and is found here: {}", propName, propValue, nodePath);
                        ExternalStorageReference externalStorageReference = new ExternalStorageReference(nodePath, propValue);

                        getExternalStorageReferences().add(externalStorageReference);
                    }
                }
            } else {
                findExternalStorageReferenceNodes(nextNode);
            }
        }
    }

    private Set<String> getChildExtAttachmentNodes(Node jcrDataNode) throws RepositoryException {
        Set<String> compoundNodeNames = new HashSet();
        NodeIterator nodeIter = jcrDataNode.getNodes();

        while (nodeIter.hasNext()) {
            Node subNode = nodeIter.nextNode();
            if (this.isExtAttachmentType(subNode)) {
                compoundNodeNames.add(subNode.getName());
            }
        }

        return compoundNodeNames;
    }

    protected boolean isExtAttachmentType(Node node) throws RepositoryException {
        return node.isNodeType(AbstractTransformer.PUBLICATIONSYSTEM_EXTATTACHMENT);
    }

    public List<ExternalStorageReference> getExternalStorageReferences() {
        if (references == null) {
            references = new ArrayList<>();
        }

        return references;
    }
}
