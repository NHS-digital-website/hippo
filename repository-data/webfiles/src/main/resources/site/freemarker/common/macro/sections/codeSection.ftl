<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Code" -->

<#macro codeSection section>

    <#if section.firstLineNumber != "">
        <#assign  lineCounter = section.firstLineNumber?number - 1 />
    <#else>
        <#assign  lineCounter = 0 />
    </#if>

    <#assign  mainHeading = section.headingLevel == 'Main heading' />

    <div class="article-section${(section.heading?has_content && mainHeading)?then('', '-with-sub-heading')}${section.heading?has_content?then('', '-with-no-heading')}">
        <#if section.heading?has_content>
            <#if mainHeading>
                <h2 data-uipath="website.contentblock.codesection.title">${section.heading}</h2>
            <#else>
                <h3 data-uipath="website.contentblock.codesection.title">${section.heading}</h3>
            </#if>
        </#if>

        <div class="code-block">

            <div class="button-bar">
                <span
                    role="button"
                    aria-label="Copy the code block to the clipboard. Javascript must be enabled"
                    class="button-code-block"
                ><span class="hidden-text">COPY</span></span>
            </div>

            <div class="code-content" itemscope itemtype="https://schema.org/SoftwareSourceCode">
                <meta itemprop="codeSampleType" content="snippet">
                <div class="scroll-box no-top-margin scrollbar">
                    <pre class="${section.lineNumbers?then('line-numbers', 'no-line-numbers')} syntax-highlighted" style="white-space: ${section.wrapLines?then('pre-wrap', 'pre')}; counter-reset: linenum ${lineCounter};">${section.codeTextHighlighted?no_esc}</pre>
                </div>
                <p class="language" itemprop="programmingLanguage">${section.language.label}</p>
                <meta itemprop="runtimePlatform" content="${section.language.label}">
            </div>

        </div>
    </div>
</#macro>
