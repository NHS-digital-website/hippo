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
import uk.nhs.digital.ps.migration.ResolveLinksHelper

import javax.jcr.Node

class ResolveLinks extends BaseNodeUpdateVisitor {

    ResolveLinksHelper helper = new ResolveLinksHelper()

    boolean doUpdate(Node node) {

        log.debug "Visiting ${node.path}"

        def id = getArticleId(node)

        log.debug(id)

        def content = node.getProperty("hippostd:content").getString()

        if(content){

            log.debug("Content: " + content)

            def pattern = /href="(.*?)"/

            def matcher = content =~ pattern

            if(matcher){

                boolean updateRequired = false

                matcher.each{ match ->

                    def originalLink = match[1]

                    def emailPattern = /^mailto:.*$/
                    def emailMatcher = originalLink =~ emailPattern

                    def filePattern = /^file:.*$/
                    def fileMatcher = originalLink =~ filePattern

                    def linkRefPattern = /^Linkref.*$/
                    def linkRefMatcher = originalLink =~ linkRefPattern

                    if(!emailMatcher && !fileMatcher && !linkRefMatcher){

                        try{
                            def finalLink = helper.resolveFinalDestination(originalLink)

                            if(!originalLink.equals(finalLink)){

                                updateRequired = true

                                content = content.replace(originalLink, finalLink)

                                log.debug("Replaced original link: " + originalLink + " with "+ finalLink)
                            }
                        }catch(Exception e){
                            log.error("Link failed in article path: [" + node.getPath() + "] - Link: " + originalLink)
                            log.error("Error message: " + e.getMessage())
                        }
                    }else{
                        log.debug("Skipping link: " + originalLink)
                    }
                }
                if(updateRequired){

                    node.setProperty("hippostd:content", content)

                    log.debug("Updated content: " + node.getProperty("hippostd:content").getString())

                    return true
                }
            }
        }

        return false

    }

    String getArticleId(Node node) {
        def nodename = node.getName()
        log.debug(nodename)
        def articlePattern = /^(.*)\/${nodename}$/
        log.debug(articlePattern)
        def matcher = node.getPath() =~ articlePattern
        if (matcher) {
            def articlePath = matcher[0][1]
            log.debug(articlePath)
            final javax.jcr.query.QueryManager queryManager = node.getSession().getWorkspace().getQueryManager();
            final javax.jcr.query.Query jcrQuery = queryManager.createQuery(query, "xpath");
            def nodeIterator = jcrQuery.execute().getNodes();
            if (nodeIterator && nodeIterator.nextNode()) {
                return nodeIterator.nextNode().getProperty().toString()
            } else {
                return "unknown"
            }
        }
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

    void destroy() {
    }

}
