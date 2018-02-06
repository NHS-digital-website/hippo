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

import org.apache.commons.lang.text.StrSubstitutor
import org.apache.commons.vfs2.VFS
import org.onehippo.forge.content.exim.core.impl.DefaultBinaryImportTask
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentManagerImpl
import org.onehippo.forge.content.exim.core.util.ContentPathUtils
import org.onehippo.forge.content.exim.core.util.HippoBinaryNodeUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.Session

class ImportingAssetOrImageSetFromFileUpdateVisitor extends BaseNodeUpdateVisitor {

    def fileInJson = true
    def documentManager
    def importTask
    def sourceBaseFolder

    void initialize(Session session) {
        if (parametersMap.containsKey("fileInJson")) {
            fileInJson = parametersMap.get("fileInJson")
        }

        def sourceBaseFolderPath = StrSubstitutor.replaceSystemProperties(parametersMap.get("sourceBaseFolderPath"))
        sourceBaseFolder = VFS.getManager().resolveFile(sourceBaseFolderPath)

        documentManager = new WorkflowDocumentManagerImpl(session)
        importTask = new DefaultBinaryImportTask(documentManager)
        importTask.setLogger(log)
        importTask.start()
    }

    boolean doUpdate(Node node) {
        def contentNode
        def binaryPrimaryTypeName
        def binaryFolderPrimaryTypeName
        def binaryFolderFolderTypes
        def binaryFolderGalleryTypes
        def binaryLocation
        def binaryFolderPath
        def binaryName
        def updatedBinaryLocation

        // import input files in json (or xml) format
        def files
        if (fileInJson) {
            files = importTask.findFilesByNamePattern(sourceBaseFolder, "^.+\\.json\$" , 1, 10)
        } else {
            files = importTask.findFilesByNamePattern(sourceBaseFolder, "^.+\\.xml\$" , 1, 10)
        }

        def record

        files.eachWithIndex { file, i ->
            try {
                // read ContentNode from json (or xml) file.
                if (fileInJson) {
                    contentNode = importTask.readContentNodeFromJsonFile(file)
                } else {
                    contentNode = importTask.readContentNodeFromXmlFile(file)
                }

                binaryPrimaryTypeName = contentNode.getPrimaryType()
                // determine the target binary handle node path to create or update binary content from the jcr:path meta property in ContentNode object.
                binaryLocation = contentNode.getProperty("jcr:path").getValue()

                // record instance to store execution status and detail of a unit of migration work item.
                // these record instances will be collected and summarized when #logSummary() invoked later.
                record = importTask.beginRecord("", binaryLocation)
                record.setAttribute("file", file.name.path)

                if (binaryPrimaryTypeName == "hippogallery:imageset" || binaryPrimaryTypeName == "hippogallery:exampleAssetSet") {
                    record.setProcessed(true)
                    // split target folder path and binary handle node name from the binaryLocation.
                    def folderPathAndName = ContentPathUtils.splitToFolderPathAndName(binaryLocation)
                    binaryFolderPath = folderPathAndName[0]
                    binaryName = folderPathAndName[1]

                    // choose proper binary node type, hippostd:foldertype and hippostd:gallerytype values for either gallery image or asset.
                    if (binaryPrimaryTypeName == "hippogallery:imageset") {
                        binaryFolderPrimaryTypeName = "hippogallery:stdImageGallery"
                        binaryFolderFolderTypes = [ "new-image-folder" ] as String[]
                        binaryFolderGalleryTypes = [ "hippogallery:imageset" ] as String[]
                    } else if (binaryPrimaryTypeName == "hippogallery:exampleAssetSet") {
                        binaryFolderPrimaryTypeName = "hippogallery:stdAssetGallery"
                        binaryFolderFolderTypes = [ "new-file-folder" ] as String[]
                        binaryFolderGalleryTypes = [ "hippogallery:exampleAssetSet" ] as String[]
                    }

                    // make sure that the binary target folder exists or created.
                    binaryFolderPath =
                        importTask.createOrUpdateBinaryFolder(binaryFolderPath, binaryFolderPrimaryTypeName,
                            binaryFolderFolderTypes, binaryFolderGalleryTypes)

                    // create or update binary content from contentNode.
                    updatedBinaryLocation =
                        importTask.createOrUpdateBinaryFromContentNode(contentNode, binaryPrimaryTypeName,
                            binaryFolderPath, binaryName)

                    // Extract text from binary if possible (e.g., application/pdf) and save hippo:text property underneath.
                    HippoBinaryNodeUtils.extractTextFromBinariesAndSaveHippoTextsUnderHandlePath(documentManager.session, updatedBinaryLocation)

                    visitorContext.reportUpdated(binaryLocation)
                    log.debug "Imported binary from '${file.name.path}' to '${updatedBinaryLocation}'."
                    record.setSucceeded(true)
                } else {
                    visitorContext.reportSkipped(documentLocation)
                }
            } catch (e) {
                log.error("Failed to process record.", e)
                visitorContext.reportFailed(binaryLocation)
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
