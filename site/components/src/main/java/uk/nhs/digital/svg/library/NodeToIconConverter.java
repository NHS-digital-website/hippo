package uk.nhs.digital.svg.library;

import org.hippoecm.hst.core.request.HstRequestContext;

import java.util.Arrays;
import java.util.function.BiFunction;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class NodeToIconConverter {

    public static final BiFunction<HstRequestContext, Node, String> urlPathFinder = (rc, node) -> rc.getHstLinkCreator().create(node, rc).getPath();

    public static Icon convert(final Node node, final String path) throws RepositoryException {
        Icon icon = makeIcon(node, path);
        if (node.hasProperty("hippogallery:description")) {
            icon.addKeywords(Arrays.asList(node.getProperty("hippogallery:description").getValue().getString().trim().split("\\s*,\\s*")));
        }
        return icon;
    }

    private static Icon makeIcon(Node node, String path) throws RepositoryException {
        if (node.getParent().hasProperty("hippo:name")) {
            return new Icon(node.getIdentifier(), node.getParent().getProperty("hippo:name").getValue().getString(), path, getCategory(node));
        } else {
            return new Icon(node.getIdentifier(), node.getName(), path, getCategory(node));
        }
    }

    private static String getCategory(Node node) throws RepositoryException {
        if (node.getParent().getParent().isNodeType("hippostd:folder") && node.getParent().getParent().hasProperty("hippo:name")) {
            return node.getParent().getParent().getProperty("hippo:name").getValue().getString();
        }
        return "";
    }

}
