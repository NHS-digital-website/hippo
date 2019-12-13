package org.hippoecm.frontend.plugins.cms.admin.updater

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session

import static org.apache.jackrabbit.JcrConstants.JCR_DATA;
import static org.hippoecm.repository.api.HippoNodeType.HIPPO_TEXT;

class UpdaterTemplate extends BaseNodeUpdateVisitor {

  private final boolean overwriteExisting = true;


  boolean doUpdate(Node node) {
    log.debug "Checking node ${node.path}"
    if (node.hasProperty("jcr:mimeType") && node.hasProperty(JCR_DATA)) {
      final String mimeType = node.getProperty("jcr:mimeType").getString();
      final boolean hasExtractedText = node.hasProperty(HIPPO_TEXT);
      log.debug "Found asset node ${node.path} type ${mimeType}";
      if (overwriteExisting || !hasExtractedText) {
        if (isOfficeAsset(mimeType) || isPdfAsset(mimeType)) {
          log.debug "Start setting empty hippo:text on ${node.path}";
          setEmptyHippoTextBinary(node);
          return true;
        }
      }
    }
    return false;
  }
  
  private boolean isPdfAsset(final String mimeType) {
    if (mimeType != null && mimeType.contains("pdf")) {
         return true;
    }
    return false;
  } 
  
  private boolean isOfficeAsset(final String mimeType) {
    if (mimeType != null && 
         (mimeType.contains("excel") || 
          mimeType.contains("msword") || 
          mimeType.contains("ms-powerpoint") || 
          mimeType.contains("officedocument"))) {
         return true;
    }
    return false;
  } 

  private void setEmptyHippoTextBinary(final Node node) {
    String nodePath = null;
    try {
      nodePath = node.getPath();
      final ByteArrayInputStream emptyByteArrayInputStream = new ByteArrayInputStream(new byte[0]);
      node.setProperty(HIPPO_TEXT, node.getSession().getValueFactory().createBinary(emptyByteArrayInputStream));
      log.info "Empty hippo:text set on ${node.path}";
    } catch (RepositoryException e) {
      log.error "Unable to store empty hippo:text for node ${nodePath} : ${e}";
    }
  }

  boolean undoUpdate(Node node) {
    throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
  }
  
  boolean logSkippedNodePaths() {
    return false // don't log skipped node paths
  }

  boolean skipCheckoutNodes() {
    return false // return true for readonly visitors and/or updates unrelated to versioned content
  }
  
}
