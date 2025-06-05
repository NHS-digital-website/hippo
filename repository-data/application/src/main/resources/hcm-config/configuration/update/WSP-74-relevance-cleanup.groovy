package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor
import javax.jcr.Node

class UpdaterTemplate extends BaseNodeUpdateVisitor {

  boolean doUpdate(Node node) {
      log.info "Removing node ${node.path}"
      node.remove();
      return true;
  }

  boolean undoUpdate(Node node) {
    throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
  }
}
