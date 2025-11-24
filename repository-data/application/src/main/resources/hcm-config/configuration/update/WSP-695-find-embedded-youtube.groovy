package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor
import javax.jcr.*

class FindYoutubeEmbedsAnywhere extends BaseNodeUpdateVisitor {

    private static final java.util.List<String> NEEDLES =
        java.util.Arrays.asList("youtube.com/embed", "youtube-nocookie.com/embed")

    @Override boolean logSkippedNodePaths() { false }
    @Override boolean skipCheckoutNodes()    { false }

    @Override Node firstNode(Session s) throws RepositoryException { null }
    @Override Node nextNode()  throws RepositoryException { null }

    @Override
    boolean doUpdate(Node supplied) {
        Node handle = supplied.isNodeType("hippo:handle") ? supplied : supplied.getParent()
        if (!handle.isNodeType("hippo:handle")) return false

        if (handleContainsAnyNeedle(handle)) {
            String path = handle.getPath()
            log.info("YouTube-like embed detected in document: {}", path)
        }
        return false
    }

    private boolean handleContainsAnyNeedle(Node root) {
        java.util.ArrayDeque<Node> stack = new java.util.ArrayDeque<>()
        stack.push(root)
        while (!stack.isEmpty()) {
            Node n = stack.pop()
            if (nodeHasAnyNeedle(n)) return true
            for (NodeIterator it = n.getNodes(); it.hasNext(); ) stack.push(it.nextNode())
        }
        return false
    }

    private boolean nodeHasAnyNeedle(Node n) {
        for (PropertyIterator it = n.getProperties(); it.hasNext(); ) {
            Property p = it.nextProperty()
            if (p.getType() != PropertyType.STRING) continue
            if (p.isMultiple()) {
                for (Value v : p.getValues()) if (valueHasAnyNeedle(v)) return true
            } else {
                if (valueHasAnyNeedle(p.getValue())) return true
            }
        }
        return false
    }

    private boolean valueHasAnyNeedle(Value v) {
        try {
            String s = v.getString()
            if (s == null || s.isEmpty()) return false
            for (String needle : NEEDLES) if (s.contains(needle)) return true
            return false
        } catch (RepositoryException e) { return false }
    }

    @Override
    boolean undoUpdate(Node node) { throw new UnsupportedOperationException("Read-only updater") }
}