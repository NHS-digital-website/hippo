<#ftl output_format="HTML">

<#macro websiteSection section isPreviousSectionEmphasisBox numberedListCount mainHeadingLevel=2 >

    <#assign articleSectionClass = 'article-section' />
    <#assign subHeading = section.headingLevel?has_content && section.headingLevel == 'Sub heading'/>
    <#if subHeading>
        <#assign articleSectionClass = 'article-section-with-sub-heading' />
    </#if>

    <#if section.title?has_content>
    <div id="${slugify(section.title)}" class="${articleSectionClass} <#if isPreviousSectionEmphasisBox>article-section--highlighted</#if>">

        <#if subHeading>
            <#assign subHeadingLevel = mainHeadingLevel?int + 1 />
            <#assign subHeadingTag = "h" + subHeadingLevel />
            <${subHeadingTag} data-uipath="website.contentblock.section.title" id="${slugify(section.title)}" class="navigationMarker"><#if section.isNumberedList>${numberedListCount}. </#if>${section.title}</${subHeadingTag}>
        <#else>
            <#assign mainHeadingTag = "h" + mainHeadingLevel />
            <${mainHeadingTag} data-uipath="website.contentblock.section.title" id="${slugify(section.title)}" class="navigationMarker"><#if section.isNumberedList>${numberedListCount}. </#if>${section.title}</${mainHeadingTag}>
        </#if>

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
