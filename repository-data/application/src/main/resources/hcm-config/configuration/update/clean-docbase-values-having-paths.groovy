/*
 * Copyright 2015-2017 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hippoecm.frontend.plugins.cms.admin.updater

import org.apache.commons.lang.StringUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

class CleaningDocbasesHavingPathsUpdateVisitor extends BaseNodeUpdateVisitor {

    boolean doUpdate(Node node) {
        log.debug "Visiting ${node.path}"
        def docbasePath = node.getProperty("hippo:docbase").getString()
        def docbase

        if (StringUtils.startsWith(docbasePath, "/") && node.session.nodeExists(docbasePath)) {
            docbase = node.session.getNode(docbasePath).getIdentifier()
            node.setProperty("hippo:docbase", docbase)
            log.info "Reset ${node.path}/hippo:docbase to '${docbase}' from '${docbasePath}'."
            return true
        }

        return false

    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

    void destroy() {
    }

}
