package org.hippoecm.frontend.plugins.cms.admin.updater

/*
 * Copyright 2018 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 * A lot of urlrewriter rules like
 *
 * ^/article/2338(/.*)?$
 * ^/article/2442(/.*)?$
 * ^/article/2478(/.*)?$
 * ... x 50
 *
 * pointing to same url:
 * https://digital.nhs.uk/National-casemix-office/downloads-groupers-and-tools
 *
 * So this could be optimized and combined into one single rule.
 */


import com.google.common.base.Joiner
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.NodeIterator
import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.query.Query
import javax.jcr.query.QueryManager
import javax.jcr.query.QueryResult
import java.util.regex.Pattern

class UrlRewriterMergeRules extends BaseNodeUpdateVisitor {

    private Session session
    public static final Multimap<String, Wrapper> MAPPINGS = HashMultimap.create()

    final static Pattern ARTICLE_PATTERN = Pattern.compile("\\^/article/[0-9]{1,10}\\(/.*\\)\\?\\\$")
    final static Pattern URL_PATTERN = Pattern.compile("\\^[a-zA-Z0-9-/]*\\\$")
    private boolean done = false;

    @Override
    void initialize(final Session session) throws RepositoryException {

        super.initialize(session)
        this.session = session


        final QueryManager manager = session.getWorkspace().getQueryManager();
        final QueryResult result = manager.createQuery("/jcr:root/content/urlrewriter/rules//element(*, urlrewriter:basedocument)[@hippo:availability='live']", Query.XPATH).execute()
        final NodeIterator nodes = result.getNodes()
        while (nodes.hasNext()) {
            Node node = nodes.nextNode()
            String from = JcrUtils.getStringProperty(node, "urlrewriter:rulefrom", null)
            String to = JcrUtils.getStringProperty(node, "urlrewriter:ruleto", null)
            if (from != null && to != null) {
                Wrapper wrapper = new Wrapper()
                wrapper.node = node
                wrapper.pattern = from;
                MAPPINGS.put(to, wrapper)
            }
        }
    }

    boolean doUpdate(Node node) {
        if (done) {
            return false
        }
        // run only once...
        done = true;

        // print duplicates
        Set<String> keys = MAPPINGS.keySet()
        for (String key : keys) {
            Collection<Wrapper> urlMap = MAPPINGS.get(key)
            if (urlMap.size() > 1) {

                List<String> articles = allArticle(urlMap)
                if (articles.size() > 1) {
                    //all nodes are ^/article/723(/.*)?$ form, we can merge them:
                    String pattern = "(" + Joiner.on("|").join(articles) + ")"
                    log.info("Merged pattern:  {}", pattern);
                    boolean first = true
                    for (Wrapper wrapper : urlMap) {
                        if (first) {
                            processNode(wrapper, pattern)
                            first = false
                            continue
                        }
                        // remove other nodes.
                        log.debug "### deleting node  ${wrapper.node.path}"
                        wrapper.node.getParent().remove()
                    }
                    JcrUtils.ensureIsCheckedOut(node)
                    session.save()
                }
            }
        }
        return true
    }

    def processNode(final Wrapper wrapper, final String pattern) {
        Node root = wrapper.node.parent
        log.debug "Updating pattern for node  ${root.path}"
        NodeIterator nodes = root.getNodes()
        while (nodes.hasNext()) {
            Node node = nodes.nextNode()
            if (node.hasProperty("urlrewriter:rulefrom")) {
                JcrUtils.ensureIsCheckedOut(node)
                node.setProperty("urlrewriter:rulefrom", pattern)
            }
        }
    }

    List<String> allArticle(final Collection<Wrapper> wrappers) {
        final List<String> result = new ArrayList<>()
        for (Wrapper w : wrappers) {
            if (ARTICLE_PATTERN.matcher(w.pattern).matches()) {
                result.add(w.pattern)
            } else {
                if (URL_PATTERN.matcher(w.pattern).matches()) {
                    result.add(w.pattern);
                }else{
                    return Collections.emptyList()
                }
            }
        }

        return result;
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }


    private class Wrapper {
        Node node;
        String pattern;

        boolean equals(final o) {
            if (this.is(o)) {
                return true
            }
            if (getClass() != o.class) {
                return false
            }

            final Wrapper wrapper = (Wrapper) o

            if (pattern != wrapper.pattern) {
                return false
            }

            return true
        }

        int hashCode() {
            return (pattern != null ? pattern.hashCode() : 0)
        }
    }

}
