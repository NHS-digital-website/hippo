package uk.nhs.digital.website.beans;
/*
 * Copyright 2014 Hippo B.V. (http://www.onehippo.com)
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

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:bannerdocument")
@Node(jcrType = "website:bannerdocument")
public class Banner extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getSingleProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:content")
    public HippoHtml getContent() {
        return getHippoHtml("website:content");
    }

    @HippoEssentialsGenerated(internalName = "website:image")
    public CorporateWebsiteImageset getImage() {
        return getLinkedBean("website:image", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:link")
    public HippoBean getLink() {
        return getLinkedBean("website:link", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:seosummary"   )
    public HippoHtml getSeosummary() {
        return getHippoHtml("website:seosummary");
    }

}
