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

import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.Value

class GdprTransparencyFacet extends BaseNodeUpdateVisitor {

    boolean doUpdate(Node node) {
        log.debug "Creating a facet node below ${node.getPath()}"
        def facetnode = node.addNode("gdpr-transparency", "hippofacnav:facetnavigation")
        facetnode.setProperty("hippo:docbase", "12345678-1234-1234-1234-123456789abc")

        Value[] facetnodenameValues = new Value[1];
        facetnodenameValues[0] = facetnode.session.getValueFactory().createValue("Title Initial Letter");
        facetnode.setProperty("hippofacnav:facetnodenames", facetnodenameValues)

        Value[] facetsValues = new Value[1];
        facetsValues[0] = facetnode.session.getValueFactory().createValue("website:titleinitialletter");
        facetnode.setProperty("hippofacnav:facets", facetsValues)

        log.info "Adding ${facetnode.getPath()}"
        return true


    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

    void destroy() {
    }

}
