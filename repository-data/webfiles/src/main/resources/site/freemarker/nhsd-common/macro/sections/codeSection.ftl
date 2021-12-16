<#ftl output_format="HTML">

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

    <article class="nhsd-o-code-viewer nhsd-!t-margin-bottom-6">
        <div class="nhsd-o-code-viewer__tab-content">
            <div class="nhsd-o-code-viewer__code">
                <div id="${section.language.label}-content" class="nhsd-o-code-viewer__code-content" role="tabpanel" aria-labelledby="tab-${section.language.label}-content">
                    <pre class="${section.lineNumbers?then('line-numbers', 'no-line-numbers')} syntax-highlighted" style="white-space: ${section.wrapLines?then('pre-wrap', 'pre')};">${section.codeTextHighlighted?no_esc}</pre>
                </div>
            </div>
        </div>
        <div class="nhsd-o-code-viewer__footer">
            <nav class="nhsd-m-tabs">
                <div role="tablist">
                    <a class="nhsd-a-tab" href="#${section.language.label}-content" id="tab-${section.language.label}-content" data-tab-content="${section.language.label}-content" aria-controls="${section.language.label}-content" aria-selected="true" role="tab">${section.language.label}</a>
                </div>
            </nav>
        </div>
    </article>
</#macro>
