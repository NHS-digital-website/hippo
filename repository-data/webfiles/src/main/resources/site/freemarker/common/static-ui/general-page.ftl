<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/sectionNav.ftl">

<article class="article article--general">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <#assign links = [ {"title": "Link 1", "url": "#"}, {"title": "Link 2", "url": "#"}, {"title": "Link 3", "url": "#"}, {"title": "Link 4", "url": "#"}, {"title": "Link 5", "url": "#"}, {"title": "Link 6", "url": "#"}, {"title": "Link 7", "url": "#"}, {"title": "Link 8", "url": "#"}, {"title": "Link 9", "url": "#"}, {"title": "Link 10", "url": "#"}, {"title": "Link 11", "url": "#"}, {"title": "Link 12", "url": "#"}, {"title": "Link 13", "url": "#"}, {"title": "Link 14", "url": "#"}, {"title": "Link 15", "url": "#"}, {"title": "Link 16", "url": "#"}, {"title": "Link 17", "url": "#"}, {"title": "Link 18", "url": "#"}, {"title": "Link 19", "url": "#"}, {"title": "Link 20", "url": "#"} ] />
                    <@sectionNav links />
                </div>
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
