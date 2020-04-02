<#ftl output_format="HTML">

<#-- Note Page must implement uk.nhs.digital.pagination.Paginated -->
<#macro chapterNav page currentPageTitlePrefix="">
    <#assign chapterPagination = page.paginate() />
    <#if chapterPagination??>
        <nav class="chapter-pagination" aria-label="Chapter Navigation">
            <ul class="chapter-pagination__list grid-row">
                <#if chapterPagination.previous??>
                    <li class="chapter-pagination__list-item chapter-pagination-item--previous">
                        <p>
                            <span class="chapter-pagination__direction"
                                  aria-hidden="true">
                                Previous chapter
                            </span>
                            <a class="chapter-pagination__link"
                               href="<@hst.link hippobean=chapterPagination.previous.linkedBean />">
                                <span class="chapter-pagination__link-direction visually-hidden">Previous chapter: </span>
                                <span class="chapter-pagination__link-arrow"><img
                                            aria-hidden="true" alt="Left Arrow"
                                            src="<@hst.webfile path="/images/chapter-navigation/left-arrow.svg"/>"/></span><!--
   No space between elements --><span class="chapter-pagination__link-page">${chapterPagination.previous.title}</span>
                            </a>
                        </p>
                    </li>
                <#else>
                    <li class="chapter-pagination__list-item"></li>
                </#if>
                <li class="chapter-pagination__list-item chapter-pagination-item--current column-one-third">
                    <p>
                            <span class="chapter-pagination__direction">
                                ${currentPageTitlePrefix}${page.title}
                            </span>
                        <a class="chapter-pagination__link"
                           href="#chapter-index">
                            <span class="chapter-pagination__link-direction visually-hidden"></span>
                            <span class="chapter-pagination__link-arrow"><img
                                        aria-hidden="true" alt="Down Arrow"
                                        src="<@hst.webfile path="/images/chapter-navigation/down-arrow.svg"/>"/></span><!--
No space between elements --><span class="chapter-pagination__link-page">View all chapters</span>
                        </a>
                    </p>

                </li>
                <#if chapterPagination.next??>
                    <li class="chapter-pagination__list-item chapter-pagination-item--next column-one-third">
                        <p>
                            <span class="chapter-pagination__direction"
                                  aria-hidden="true">
                                Next chapter
                            </span>
                            <a class="chapter-pagination__link"
                               href="<@hst.link hippobean=chapterPagination.next.linkedBean />">
                                <span class="chapter-pagination__link-direction visually-hidden">Next chapter: </span>
                                <span class="chapter-pagination__link-page">${chapterPagination.next.title}</span><!--
   No space between elements --><span class="chapter-pagination__link-arrow"><img
                                            aria-hidden="true" alt="Right Arrow"
                                            src="<@hst.webfile path="/images/chapter-navigation/right-arrow.svg"/>"/></span>
                            </a>
                        </p>
                    </li>
                <#else>
                    <li class="chapter-pagination__list-item"></li>
                </#if>
            </ul>
        </nav>
    </#if>
</#macro>

<#-- Footer index navigation for chaptered documents -->
<#macro chapterIndexNav documents={} title="Navigation title">
    <#assign splitChapters = splitHash(documents) />
    
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" id="chapter-index">
        <div class="chapter-nav">
            <div class="grid-wrapper">

                <div class="grid-row">
                    <div class="column column--reset">
                        <h2 class="chapter-nav__title">${title}</h2>
                    </div>
                </div>

                <div class="grid-row">
                    <div class="column column--one-half column--left">
                        <ol class="list list--reset cta-list chapter-index">
                            <#list splitChapters.left as chapter>
                                <#if chapter.id == document.identifier>
                                    <li class="chapter-index__item chapter-index__item--current">
                                        <p class="chapter-index__current-item">${chapter.title}</p>
                                    </li>
                                <#else>
                                    <li class="chapter-index__item">
                                        <a class="chapter-index__link" href="${chapter.link}" title="${chapter.title}">${chapter.title}</a>
                                    </li>
                                </#if>
                            </#list>
                        </ol>
                    </div>

                    <#if splitChapters.right?size gte 1>
                        <div class="column column--one-half column--right">
                            <ol class="list list--reset cta-list chapter-index" start="${splitChapters.left?size + 1}">
                                <#list splitChapters.right as chapter>
                                    <#if chapter.id == document.identifier>
                                        <li class="chapter-index__item chapter-index__item--current">
                                            <p class="chapter-index__current-item">${chapter.title}</p>
                                        </li>
                                    <#else>
                                        <li class="chapter-index__item">
                                            <a class="chapter-index__link" href="${chapter.link}" title="${chapter.title}">${chapter.title}</a>
                                        </li>
                                    </#if>
                                </#list>
                            </ol>
                        </div>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</#macro>
