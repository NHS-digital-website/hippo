<#ftl output_format="HTML">

<#macro websiteSection section isPreviousSectionEmphasisBox numberedListCount>

    <#assign articleSectionClass = 'article-section' />
    <#assign subHeading = section.headingLevel?has_content && section.headingLevel == 'Sub heading'/>
    <#if subHeading>
      <#assign articleSectionClass = 'article-section-with-sub-heading' />
    </#if>

    <#if section.title?has_content>
        <div id="${slugify(section.title)}" class="${articleSectionClass} <#if isPreviousSectionEmphasisBox>article-section--highlighted</#if>">

            <#if subHeading>
              <h3 data-uipath="website.contentblock.section.title"><#if section.isNumberedList>${numberedListCount}. </#if>${section.title}</h3>
            <#else>
              <h2 data-uipath="website.contentblock.section.title"><#if section.isNumberedList>${numberedListCount}. </#if>${section.title}</h2>
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
