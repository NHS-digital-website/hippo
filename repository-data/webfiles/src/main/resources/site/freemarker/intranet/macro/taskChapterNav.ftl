<#ftl output_format="HTML">

<#macro taskChapterNav previousTask="" currentTask="" nextTask="">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide grid-wrapper--chapter-pagination">
        <div class="chapter-pagination-wrapper">
            <div class="grid-wrapper">
                <div class="grid-row chapter-pagination-wrapper__skip visually-hidden">
                    <div class="column column--reset">
                        <a href="#document-content"><@fmt.message key="labels.skip-to-content" /></a>
                    </div>
                </div>

                <nav class="chapter-pagination" aria-label="Chapter Navigation">
                    <ul class="chapter-pagination__list grid-row">
                        <#if previousTask?has_content>
                            <li class="chapter-pagination__list-item chapter-pagination-item--previous">
                                <p>
                                    <span class="chapter-pagination__direction" aria-hidden="true"><@fmt.message key="labels.task-chapter-nav.previous" /></span>
                                    <a class="chapter-pagination__link"
                                       href="${previousTask.link}">
                                        <span class="chapter-pagination__link-direction visually-hidden"><@fmt.message key="labels.task-chapter-nav.previous" />: </span>
                                        <span class="chapter-pagination__link-arrow"><img
                                                    aria-hidden="true" alt="Left Arrow"
                                                    src="<@hst.webfile path="/images/chapter-navigation/left-arrow.svg"/>"/></span><!--
            No space between elements --><span class="chapter-pagination__link-page">${previousTask.title}</span>
                                    </a>
                                </p>
                            </li>
                        <#else>
                            <li class="chapter-pagination__list-item"></li>
                        </#if>
                        <li class="chapter-pagination__list-item chapter-pagination-item--current column-one-third">
                            <p>
                                    <span class="chapter-pagination__direction">${currentTask.title}</span>
                                <a class="chapter-pagination__link"
                                   href="#chapter-index">
                                    <span class="chapter-pagination__link-direction visually-hidden"></span>
                                    <span class="chapter-pagination__link-arrow"><img
                                                aria-hidden="true" alt="Down Arrow"
                                                src="<@hst.webfile path="/images/chapter-navigation/down-arrow.svg"/>"/></span><!--
            No space between elements --><span class="chapter-pagination__link-page"><@fmt.message key="labels.task-chapter-nav.view-all" /></span>
                                </a>
                            </p>

                        </li>
                        <#if nextTask?has_content>
                            <li class="chapter-pagination__list-item chapter-pagination-item--next column-one-third">
                                <p>
                                    <span class="chapter-pagination__direction"
                                          aria-hidden="true">
                                        <@fmt.message key="labels.task-chapter-nav.next" />
                                    </span>
                                    <a class="chapter-pagination__link"
                                       href="${nextTask.link}">
                                        <span class="chapter-pagination__link-direction visually-hidden"><@fmt.message key="labels.task-chapter-nav.next" />: </span>
                                        <span class="chapter-pagination__link-page">${nextTask.title}</span><!--
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

            </div>
        </div>
    </div>
</#macro>

<#-- Footer index navigation for chaptered tasks -->
<#macro taskChapterIndexNav documents={} title="">
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
