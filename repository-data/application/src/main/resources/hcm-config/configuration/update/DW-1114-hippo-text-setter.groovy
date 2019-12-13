package org.hippoecm.frontend.plugins.cms.admin.updater

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


import org.onehippo.repository.update.BaseNodeUpdateVisitor
import org.hippoecm.frontend.editor.plugins.resource.PdfParser

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.tika.Tika;
import org.apache.tika.detect.NameDetector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.mime.MediaType;
import org.onehippo.repository.tika.TikaFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session

import static org.apache.jackrabbit.JcrConstants.JCR_DATA;
import static org.hippoecm.repository.api.HippoNodeType.HIPPO_TEXT;

class UpdaterTemplate extends BaseNodeUpdateVisitor {

  private final boolean reExtract = true;
  private final boolean setEmptyOnFailure = true;
  
  private final Tika tika = TikaFactory.newTika();
  // charset used in converting tika reader to inputstream
  private Charset charset = StandardCharsets.UTF_8;


  boolean doUpdate(Node node) {
    log.debug "Checking node ${node.path}"
    if (node.hasProperty("jcr:mimeType") && node.hasProperty(JCR_DATA)) {
      final String mimeType = node.getProperty("jcr:mimeType").getString();
      final boolean hasExtractedText = node.hasProperty(HIPPO_TEXT);
      log.debug "Found asset node ${node.path} type ${mimeType}";
      if (reExtract || !hasExtractedText) {
        if (isOfficeAsset(mimeType) || isPdfAsset(mimeType)) {
          log.debug "Start text extraction from ${node.path}";
          InputStream tikaInputStream;
          try {
            final Binary binary = node.getProperty(JCR_DATA).getBinary();
            tikaInputStream = new ReaderInputStream(tika.parse(binary.getStream()),charset);
            node.setProperty(HIPPO_TEXT, node.getSession().getValueFactory().createBinary(tikaInputStream));
            log.info "Extracted text and set hippo:text on ${node.path}";
            return true;
          } catch (Throwable e) {
            // catch throwable since we also want to catch OOMEs
            log.warn "Failed to extract text from ${node.path} : ${e}";
            if (setEmptyOnFailure) {
              setEmptyHippoTextBinary(node);
            }
            return false;
          } finally {
            IOUtils.closeQuietly(tikaInputStream);
          }
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
