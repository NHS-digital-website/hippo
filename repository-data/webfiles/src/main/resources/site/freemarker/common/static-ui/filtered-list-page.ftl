<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/stickyGroupBlockHeader.ftl">

<article class="article article--filtered-list">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--two-thirds column--reset">
                <div class="article-header article-header--secondary">                    
                    <h1>Filtered list page</h1>
                </div>
            </div>
        </div>

        <div class="grid-row">
            <div class="column column--two-thirds column--reset">
                <#-- [FTL-BEGIN] 'Summary and optional Body' sections -->
                <div id="section-summary" class="article-section article-section--summary">
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="rich-text-content">
                                <p>An alphabetical list and description of systems, services and tools.</p>
                            </div>
                        </div>
                    </div>
                </div>
                <#-- [FTL-END] 'Summary and optional Body' sections -->
            </div>
        </div>

        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar sticky sticky--top">
                <#include "shared/section-filter-nav.ftl" />
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <#-- [FTL-BEGIN] 'Article group' section -->
                <div class="article-section article-section--letter-group">
                    <@stickyGroupBlockHeader "A"></@stickyGroupBlockHeader>

                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="list list--reset cta-list cta-list--sections">
                                <div class="cta">
                                    <h2 class="cta__title"><a href="#">Abdominal Aortic Aneurysm screening</a></h2>
                                    <p class="cta__text">A cohort of eligible males is extracted each year from NHAIS systems and uploaded to our Population Index (PI) database.</p>
                                </div>
                                <div class="cta">  
                                    <h2 class="cta__title"><a href="#">Adoption Registration Service</a></h2>
                                    <p class="cta__text">This service can check for a record of a Civil Death Registration in England, Wales and the Isle of Man on behalf of adoptees and birth relatives to establish if an adoptee or birth relative is recorded as having died. You can also use this service to forward information about hereditary medical conditions to an adoptee's or birth relative's GP. </p>
                                </div>
                                <div class="cta">  
                                    <h2 class="cta__title"><a href="#">Adult Social Care Outcomes Framework (ASCOF)</a></h2>
                                    <p class="cta__text">The Adult Social Care Outcomes Framework (ASCOF) measures how well care and support services achieve the outcomes that matter most to people. The measures are grouped into four domains which are typically reviewed in terms of movement over time. Data is provided at council, regional and national level.</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <#-- [FTL-END] 'Article group' section -->

                <#-- [FTL-BEGIN] 'Article group' section -->
                <div class="article-section article-section--letter-group">
                    <@stickyGroupBlockHeader "B"></@stickyGroupBlockHeader>

                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="list list--reset cta-list cta-list--sections">
                                <div class="cta">  
                                    <h2 class="cta__title"><a href="#">Benefits management: summary</a></h2>
                                    <p class="cta__text">NHS Digital's benefit management team ensures that benefits are considered throughout the lifecycle of every programme and into service.</p>
                                </div>
                                <div class="cta">  
                                    <h2 class="cta__title"><a href="#">Bowel Cancer Screening Services</a></h2>
                                    <p class="cta__text">The Bowel Cancer Screening System (BCSS) identifies eligible men and women to participate in bowel cancer screening.</p>
                                </div>
                                <div class="cta">  
                                    <h2 class="cta__title"><a href="#">Breast screening</a></h2>
                                    <p class="cta__text">The National Health Application and Infrastructure Services (NHAIS) Breast Screening System is used to select women on local NHAIS systems who are eligible for breast screening.</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <#-- [FTL-END] 'Article group' section -->
            </div>
        </div>
    </div>
</article>
