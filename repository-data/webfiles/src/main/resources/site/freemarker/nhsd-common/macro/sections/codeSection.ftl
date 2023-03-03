<#ftl output_format="HTML">

<#include "../toolkit/organisms/codeViewer.ftl">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Code" -->

<#macro codeSection section mainHeadingLevel=2 >
    <#assign mainHeading = section.headingLevel == 'Main heading' />

    <#if section.heading?has_content>
        <#if mainHeading>
            <#assign headingTag = "h" + mainHeadingLevel />
        <#else>
            <#assign subHeadingLevel = mainHeadingLevel?int + 1 />
            <#assign headingTag = "h" + subHeadingLevel />
        </#if>
        <${headingTag} class="nhsd-t-heading-m" data-uipath="website.contentblock.codesection.title">${section.heading}</${headingTag}>
    </#if>

    <@nhsdCodeViewer>
        <@nhsdCodeViewerCodeContent section.language.label false>
            <pre class="${section.lineNumbers?then('line-numbers', 'no-line-numbers')} syntax-highlighted" style="white-space: ${section.wrapLines?then('pre-wrap', 'pre')};">${section.codeTextHighlighted?no_esc}</pre>
        </@nhsdCodeViewerCodeContent>
    </@nhsdCodeViewer>
</#macro>
