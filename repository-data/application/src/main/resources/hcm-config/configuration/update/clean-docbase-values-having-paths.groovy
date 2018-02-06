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

import org.apache.commons.lang.ArrayUtils
import org.apache.commons.lang.StringUtils
import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

class CleaningDocbasesHavingPathsUpdateVisitor extends BaseNodeUpdateVisitor {

    boolean doUpdate(Node node) {
        log.debug "Visiting ${node.path}"
        def propsUpdated = false
        def docbase

        parametersMap.docbasePropNames.eachWithIndex { docbasePropName, i ->
            def valuesUpdated = false
            String [] docbaseValues = JcrUtils.getMultipleStringProperty(node, docbasePropName, ArrayUtils.EMPTY_STRING_ARRAY)

            docbaseValues.eachWithIndex { docbaseValue, j ->
                if (StringUtils.startsWith(docbaseValue, "/") && node.session.nodeExists(docbaseValue)) {
                    docbase = node.session.getNode(docbaseValue).getIdentifier()
                    docbaseValues[j] = docbase
                    log.info "Reset ${node.path}/${docbasePropName} value at position ${i} to '${docbase}' from '${docbaseValue}'."
                    valuesUpdated = true
                }
            }

            if (valuesUpdated) {
                node.setProperty(docbasePropName, docbaseValues)
                propsUpdated = true
            }
        }

        return propsUpdated

    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

    void destroy() {
    }

}
