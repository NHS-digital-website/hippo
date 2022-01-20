<#ftl output_format="HTML">

<#macro taskChapterNav previousTask="" currentTask="" nextTask="">
    <div class="nhsd-o-chapter-navigation nhsd-!t-bg-pale-grey-80-tint">
        <div class="nhsd-a-box">

            <#if previousTask?has_content>

                <div class="nhsd-o-chapter-navigation__previous-chapter">
                    <a class="nhsd-m-chapter"
                       href="${previousTask.link}"
                       onClick="${getOnClickMethodCall(document.class.name,previousTask.link )}"
                       aria-label="Previous Chapter"
                    >
                        <div class="nhsd-m-chapter__content">
                                <span class="nhsd-m-chapter__icon">
                                    <span class="nhsd-a-icon nhsd-a-icon--size-xs">
                                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                            <path d="M7.5,15L1,8l6.5-7L9,2.5L4.8,7H15v2H4.8L9,13.5L7.5,15z"/>
                                        </svg>
                                    </span>
                                </span>
                            <p class="nhsd-t-heading-xs nhsd-!t-margin-bottom-0"><@fmt.message key="labels.task-chapter-nav.previous" /></p>
                        </div>
                        <p class="nhsd-m-chapter__link nhsd-t-body-s">${previousTask.title}</p>
                    </a>
                </div>
            </#if>
            <div class="nhsd-o-chapter-navigation__current-chapter">
                <p class="nhsd-t-heading-xs nhsd-!t-margin-bottom-0">Current task chapter</p>
                <div class="nhsd-!t-margin-bottom-0 nhsd-t-body-s">
                    <a class="nhsd-a-link" href="#chapter-index"><@fmt.message key="labels.task-chapter-nav.view-all" />
                        <span class="nhsd-t-sr-only"></span>
                    </a>
                </div>
            </div>
            <#if nextTask?has_content>
                <div class="nhsd-o-chapter-navigation__next-chapter">
                    <a class="nhsd-m-chapter nhsd-m-chapter--right"
                       href="${nextTask.link}"
                       onClick="${getOnClickMethodCall(document.class.name, nextTask.link)}"
                       aria-label="Next Chapter"
                    >
                        <div class="nhsd-m-chapter__content">
                            <p class="nhsd-t-heading-xs nhsd-!t-margin-bottom-0"><@fmt.message key="labels.task-chapter-nav.next" /></p>
                            <span class="nhsd-m-chapter__icon">
                                    <span class="nhsd-a-icon nhsd-a-icon--size-xs">
                                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                            <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                                        </svg>
                                    </span>
                                </span>
                        </div>
                        <p class="nhsd-m-chapter__link nhsd-t-body-s">${nextTask.title}</p>
                    </a>
                </div>
            </#if>
        </div>
    </div>
</#macro>

<#-- Footer index navigation for chaptered tasks -->
<#macro taskChapterIndexNav documents=[] title="">
    <#assign splitChapters = splitHash(documents) />

    <div id="chapter-index" class="nhsd-t-grid nhsd-t-grid--nested nhsd-!t-margin-bottom-6">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-12">
                <hr class="nhsd-a-horizontal-rule"/>
                <@fmt.message key="headers.publication-chapters" var="publicationChapters" />
                <h2 class="nhsd-t-heading-xl">${title}</h2>
                <div class="nhsd-m-publication-chapter-navigation nhsd-m-publication-chapter-navigation--split" data-uipath="ps.publication.pages">
                    <ol class="nhsd-t-list nhsd-t-list--number nhsd-t-list--loose">
                        <#list documents as page>
                            ${page.identifier}
                            <#assign isActive = document.identifier == page.id/>
                            <li ${isActive?then('class=nhsd-m-publication-chapter-navigation--active', '')} itemprop="hasPart" itemscope itemtype="http://schema.org/WebPage">
                                    <span itemprop="name"><a itemprop="url" href="${ page.link }" ${isActive?then('', 'data-print-article')}>${ page.title }</a></span>
                            </li>
                        </#list>
                    </ol>
                </div>
            </div>
        </div>
    </div>
</#macro>
