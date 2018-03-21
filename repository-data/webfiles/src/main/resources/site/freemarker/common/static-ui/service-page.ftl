<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/articleSections.ftl">

<article class="article article--service">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="local-header article-header">
            <h1 class="local-header__title">Service article</h1>
        </div>

        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar page-block--sticky-top">
                <#include "shared/section-nav.ftl" />
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <#-- BEGIN mandatory 'summary section' -->
                <section id="section-summary" class="article-section article-section--summary">
                    <h2>Summary</h2>
                    <p>Compellingly plagiarize multimedia based materials via B2C functionalities. Continually conceptualize B2B synergy with ethical catalysts for change. Monotonectally whiteboard proactive potentialities via front-end action items.</p>
                </section>
                <#-- END mandatory 'summary section' -->

                <#-- [FTL-BEGIN] optional list of 'top tasks section' -->
                <section class="article-section article-section--top-tasks">
                    <div class="callout callout--attention">
                        <h2>Top Tasks</h2>
                        <p>Objectively engage turnkey interfaces without impactful architectures.</p>
                        <p>Energistically innovate an expanded array of intellectual capital after covalent ROI.</p>
                        <p>Dynamically disseminate dynamic innovation after real-time users.</p>
                        <p>Efficiently customize global web services after fully researched growth strategies.</p>
                        <p>Seamlessly grow revolutionary quality vectors rather than covalent technology.</p>
                    </div>
                </section>
                <#-- [FTL-END] optional list of 'top tasks section' -->

                <#-- [FTL-BEGIN] optional 'Introduction section' -->
                <div class="article-section article-section--introduction">
                    <p>Monotonectally seize focused users with market positioning sources. Completely deploy front-end total linkage whereas emerging synergy. Compellingly visualize world-class methodologies vis-a-vis B2C leadership. Completely reintermediate world-class technologies rather than customized meta-services. Intrinsicly monetize go forward niche markets through emerging e-services.</p>
                    <p>Holisticly target worldwide <a href="#"><span class="icon icon--pdf"></span>materials without distinctive supply chains</a>. Monotonectally drive 24/365 resources with vertical infrastructures. Monotonectally procrastinate user-centric vortals whereas focused alignments. Competently productivate client-based information and intuitive growth strategies. Enthusiastically transition customer directed internal.</p>
                </div>
                <#-- [FTL-END] optional 'Introduction section' -->

                <#include "shared/sections.ftl"/>
                
                <#include "shared/child-documents.ftl"/>
            </div>
        </div>
    </div>
</article>
