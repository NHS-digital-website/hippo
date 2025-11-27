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
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Arrays;
import java.util.List;

@HippoEssentialsGenerated(internalName = "website:targetedbanner")
@Node(jcrType = "website:targetedbanner")
public class TargetedBanner extends BaseDocument {


    @HippoEssentialsGenerated(internalName = "website:contentBanner")
    public String getcontentBanner() {
        return getSingleProperty("website:contentBanner");
    }

    @HippoEssentialsGenerated(internalName = "website:targetPaths")
    public List<String> getTargetPaths() {
        String[] paths = getMultipleProperty("website:targetPaths");
        return (paths == null) ? java.util.Collections.emptyList() : Arrays.asList(paths);
    }

}
