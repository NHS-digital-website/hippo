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

class EximExport extends BaseNodeUpdateVisitor {

  def fileInJson = true
  def documentManager
  def exportTask
  def targetBaseFolder

  void initialize(Session session) {
    if (parametersMap.containsKey("fileInJson")) {
      fileInJson = parametersMap.get("fileInJson")
    }

    def targetBaseFolderPath = StrSubstitutor.replaceSystemProperties(parametersMap.get("targetBaseFolderPath"))
    targetBaseFolder = VFS.getManager().resolveFile(targetBaseFolderPath)

    documentManager = new WorkflowDocumentManagerImpl(session)
    exportTask = new WorkflowDocumentVariantExportTask(documentManager)
    exportTask.setLogger(log)

    // Setting this to zero means all files with be written to disk rather than base64 encoded.
    exportTask.setDataUrlSizeThreashold(0);

    exportTask.start()
  }

  boolean doUpdate(Node node) {
    def record

    try {
      // record instance to store execution status and detail of a unit of migration work item.
      // these record instances will be collected and summarized when #logSummary() invoked later.
      record = exportTask.beginRecord(node.identifier, node.path)

      def handlePath = node.parent.path
      // determine the exported file path based on handle path.
      def relPath = StringUtils.removeStart(ContentPathUtils.removeIndexNotationInNodePath(handlePath), "/")

      // file to export in json (or xml).
      def file
      if (fileInJson) {
        file = targetBaseFolder.resolveFile(relPath + ".json")
      } else {
        file = targetBaseFolder.resolveFile(relPath + ".xml")
      }

      def document = new Document(node.identifier)
      record.setProcessed(true)

      // export document variant content to ContentNode object.
      def contentNode = exportTask.exportVariantToContentNode(document)

      // replace hippo:docbase UUID properties of mirror nodes inside the exported ContentNode by the corresponding node paths.
      ContentNodeUtils.replaceDocbasesByPaths(documentManager.session, contentNode, ContentNodeUtils.MIRROR_DOCBASES_XPATH)

      // replace string docbase UUID properties (e.g, contenteximdemo:newsdocument/@contenteximdemo:relatedarticle property) inside the exported ContentNode by the corresponding node paths as well.
      ContentNodeUtils.replaceDocbasePropertiesByPaths(documentManager.session, contentNode, "properties[@itemName='contenteximdemo:relatedarticle']")

      record.setAttribute("file", file.name.path)

      // write ContentNode object to json (or xml) file.
      if (fileInJson) {
        exportTask.writeContentNodeToJsonFile(contentNode, file)
      } else {
        exportTask.writeContentNodeToXmlFile(contentNode, file)
      }

      log.debug "Exported document from '${handlePath}' to '${file.name.path}'."
      record.setSucceeded(true)
    } catch (e) {
      log.error("Failed to process record.", e)
      record.setErrorMessage(e.toString())
    } finally {
      exportTask.endRecord()
    }

    return false
  }

  boolean undoUpdate(Node node) {
    throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
  }

  void destroy() {
    exportTask.stop()
    exportTask.logSummary()
  }

}
