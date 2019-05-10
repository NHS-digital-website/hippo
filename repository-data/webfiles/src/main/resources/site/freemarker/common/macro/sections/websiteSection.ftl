<#ftl output_format="HTML">

<#macro websiteSection section isPreviousSectionEmphasisBox numberedListCount>
    <#if section.title?has_content>
        <div id="${slugify(section.title)}" class="article-section <#if isPreviousSectionEmphasisBox>article-section--highlighted</#if>">
            <h2 data-uipath="website.contentblock.section.title"><#if section.isNumberedList>${numberedListCount}. </#if>${section.title}</h2>
            <div data-uipath="website.contentblock.section.content" class="rich-text-content">
                <@hst.html hippohtml=section.html contentRewriter=gaContentRewriter/>
            </div>
        </div>
    <#else>
        <div class="article-section-with-no-heading">
            <div data-uipath="website.contentblock.section.content" class="rich-text-content">
                <@hst.html hippohtml=section.html contentRewriter=gaContentRewriter/>
            </div>
        </div>
    </#if>
</#macro>
