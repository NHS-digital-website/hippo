<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/sectionNav.ftl">

<article class="article article--general">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar sticky sticky--top">
                <#assign links = [{ "url": "#section-summary", "title": "Summary" }, { "url": "#section-1", "title": "Section One" },{ "url": "#section-2", "title": "Section Two" },{ "url": "#section-3", "title": "Section Three" }] />
                    <@sectionNav links></@sectionNav>
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-header article-header--secondary">
                    <h1>General document</h1>
                </div>
                
                <#-- BEGIN mandatory 'summary section' -->
                <div id="section-summary" class="article-section article-section--summary article-section--reset-top">
                    <p>Compellingly plagiarize multimedia based materials via B2C functionalities. Continually conceptualize B2B synergy with ethical catalysts for change. Monotonectally whiteboard proactive potentialities via front-end action items.</p>
                </div>
                <#-- END mandatory 'summary section' -->
                
                <#include "shared/sections.ftl"/>

                <#include "shared/child-documents.ftl"/>
            </div>
        </div>
    </div>
</article>
