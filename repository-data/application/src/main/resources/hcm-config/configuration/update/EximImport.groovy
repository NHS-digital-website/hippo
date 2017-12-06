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

import java.io.*
import javax.jcr.*
import org.apache.commons.io.*
import org.apache.commons.lang.*
import org.apache.commons.lang.text.*
import org.apache.commons.vfs2.*
import org.hippoecm.repository.api.*
import org.onehippo.repository.update.BaseNodeUpdateVisitor
import org.onehippo.forge.content.pojo.model.*
import org.onehippo.forge.content.exim.core.*
import org.onehippo.forge.content.exim.core.impl.*
import org.onehippo.forge.content.exim.core.util.*

class EximImport extends BaseNodeUpdateVisitor {

    def fileInJson = true
    DocumentManager documentManager
    WorkflowDocumentVariantImportTask importTask
    def sourceBaseFolder

    void initialize(Session session) {
        if (parametersMap.containsKey("fileInJson")) {
            fileInJson = parametersMap.get("fileInJson")
        }

        def sourceBaseFolderPath = StrSubstitutor.replaceSystemProperties(parametersMap.get("sourceBaseFolderPath"))
        sourceBaseFolder = VFS.getManager().resolveFile(sourceBaseFolderPath)

        documentManager = new WorkflowDocumentManagerImpl(session)
        importTask = new WorkflowDocumentVariantImportTask(documentManager)
        importTask.setLogger(log)
        importTask.start()
    }

    boolean doUpdate(Node node) {
        // This method is called for each node in the jcr,
        // we obviously only want to do the import once so only do it for the root directory
        if (!node.getPath().equals("/")) {
            return false
        }

        def contentNode
        def primaryTypeName
        def documentLocation
        def updatedDocumentLocation
        def locale
        def localizedName

        // find all the *.json (or *.xml) files under the sourceBaseFoler.
        FileObject[] files
        if (fileInJson) {
            files = importTask.findFilesByNamePattern(sourceBaseFolder, "^.+\\.json\$" , 1, 10)
        } else {
            files = importTask.findFilesByNamePattern(sourceBaseFolder, "^.+\\.xml\$" , 1, 10)
        }

        if (files == null) {
            log.error "No files found to import in ${sourceBaseFolder}. Aborting with no attempt of importing anything having been made."
            return false; // indicates nothing was changed
        } else {
            log.info "Files to import found: ${files.length}"
        }

        // sort found files by name - lexicographically, in ascending order
        files = files.toSorted { left, right -> left.getName().compareTo(right.getName()) }

        def record

        files.eachWithIndex { file, i ->

            log.info "Processing file [${i + 1}/${files.length}]: ${file.getName()}"

            try {

                // read ContentNode from the json (or xml) file.
                if (fileInJson) {
                    contentNode = importTask.readContentNodeFromJsonFile(file)
                } else {
                    contentNode = importTask.readContentNodeFromXmlFile(file)
                }

                primaryTypeName = contentNode.getPrimaryType()
                if ("hippostd:folder".equals(primaryTypeName)) {
                    log.warn "Folder files are not supported yet; skipping: ${file.getName()}"
                    return
                }

                // determine the target document handle node path to create or update content from the jcr:path meta property in ContentNode object.
                documentLocation = contentNode.getProperty("jcr:path").getValue()

                // record instance to store execution status and detail of a unit of migration work item.
                // these record instances will be collected and summarized when #logSummary() invoked later.
                record = importTask.beginRecord("", documentLocation)
                record.setAttribute("file", file.name.path)
                record.setProcessed(true)

                locale = (contentNode.hasProperty("hippotranslation:locale")) ? contentNode.getProperty("hippotranslation:locale").getValue() : null
                // find localized document name if jcr:localizedName meta property exists in the ContentNode object.
                localizedName = contentNode.getProperty("jcr:localizedName").getValue()

                // create or update document at documentLocation from contentNode with localized name.
                updatedDocumentLocation =
                    importTask.createOrUpdateDocumentFromVariantContentNode(contentNode, primaryTypeName, documentLocation, locale, localizedName)

                // By default, the created or updated document is left as preview status.
                // Optionally, if you want, you can publish the document right away here by uncommenting the following line.
                documentManager.publishDocument(updatedDocumentLocation)

                visitorContext.reportUpdated(documentLocation)
                log.debug "Imported document from '${file.name.path}' to '${updatedDocumentLocation}'."
                record.setSucceeded(true)
            } catch (e) {
                log.error("Failed to process record.", e)
                visitorContext.reportFailed(documentLocation)
                record.setErrorMessage(e.toString())
            } finally {
                importTask.endRecord()
            }
        }

        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

    void destroy() {
        importTask.stop()
        importTask.logSummary()
    }
}
