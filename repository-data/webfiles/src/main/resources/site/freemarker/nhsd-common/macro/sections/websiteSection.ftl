<#ftl output_format="HTML">

<#macro websiteSection section isPreviousSectionEmphasisBox numberedListCount mainHeadingLevel=2 sectionCounter=0 >
    <#assign subHeading = section.headingLevel?has_content && section.headingLevel == 'Sub heading'/>

    <#if section.title?has_content>
        <div id="${slugify(section.title)}" class="nhsd-!t-margin-bottom-6">
            <h${subHeading?then(mainHeadingLevel?int + 1, mainHeadingLevel)} class="${subHeading?then("nhsd-t-heading-l", "nhsd-t-heading-xl")}" data-uipath="website.contentblock.section.title">
                <#if section.isNumberedList>
                    ${numberedListCount}.
                </#if>${section.title}
            </h${subHeading?then(mainHeadingLevel?int + 1, mainHeadingLevel)}>
            <div data-uipath="website.contentblock.section.content">
                <@hst.html hippohtml=section.html contentRewriter=brContentRewriter/>
            </div>
        </div>
    <#else>
        <div class="nhsd-!t-margin-bottom-6">
            <div data-uipath="website.contentblock.section.content">
                <@hst.html hippohtml=section.html contentRewriter=brContentRewriter/>
            </div>
        </div>
    </#if>
</#macro>
