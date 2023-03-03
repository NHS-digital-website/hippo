<#ftl output_format="HTML">

<#assign codeViewerId = 0 />
<#assign codeViewerTabs = [] />

<#macro nhsdCodeViewerCodeContent tab="" codeTags=true>
    <#assign codeViewerTabs += [tab] />
    <#local tabId = slugify(tab) + "-content-" + codeViewerId />

    <div id="${tabId}" class="nhsd-o-code-viewer__tab-content" role="tabpanel" aria-labelledby="tab-${tabId}">
        <#if tab?has_content>
            <p id="tab-${tabId}" class="nhsd-t-heading-s nhsd-!t-margin-3" data-hide-tab-header>${tab}</p>
        </#if>
        <div class="nhsd-o-code-viewer__code">
            <div class="nhsd-o-code-viewer__code-content">
                <#if codeTags>
                    <pre><code><#nested /></code></pre>
                <#else>
                    <#nested />
                </#if>
            </div>
        </div>
    </div>
</#macro>

<#macro nhsdCodeViewerExampleContent tab="">
    <#assign codeViewerTabs += [tab] />
    <#local tabId = slugify(tab) + "-content-" + codeViewerId />

    <div id="${tabId}" class="nhsd-o-code-viewer__tab-content" role="tabpanel" aria-labelledby="tab-${tabId}">
        <#if tab?has_content>
            <p id="tab-${tabId}" class="nhsd-t-heading-s nhsd-!t-margin-3" data-hide-tab-header>${tab}</p>
        </#if>
        <div class="nhsd-o-code-viewer__example">
            <#nested />
        </div>
    </div>
</#macro>

<#macro nhsdCodeViewer exampleLink="">
    <#assign codeViewerId++ />
    <#assign codeViewerTabs = [] />

    <article class="nhsd-o-code-viewer">
        <#if exampleLink?has_content>
            <div class="nhsd-o-code-viewer__header">
                <a class="nhsd-a-link" href="${exampleLink?no_esc}" target="_blank">
                    Preview this example <span class="nhsd-t-sr-only">(opens in a new tab)</span>
                </a>
            </div>
        </#if>
        <#nested />
        <#if codeViewerTabs?size gt 1>
            <div class="nhsd-o-code-viewer__footer">
                <nav class="nhsd-m-tabs">
                    <div role="tablist">
                        <#list codeViewerTabs as tab>
                            <#local tabId = slugify(tab) + "-content-" + codeViewerId />
                            <a class="nhsd-a-tab" href="#${tabId}" data-tab-content="${tabId}" aria-controls="${tabId}" aria-selected="true" role="tab">${tab}</a>
                        </#list>
                    </div>
                </nav>
            </div>
        </#if>
    </article>
</#macro>
