<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#include "../macro/stickyNavSections.ftl">
<#include "../macro/stickyNavYears.ftl">
<#include "../macro/hubBox.ftl">
<#include "../macro/stickyGroupBlockHeader.ftl">

<#assign relatedLinks = [{ "title": "Hospital Admitted Care Activity 2018-19", "url": "#"}, { "title": "Distinctively maintained standards", "url": "#"}, { "title": "Completely transition market-driven", "url": "#"}]/>
<#assign pageable = {
    "totalPages": 2,
    "pageNumbersArray": [1, 2],
    "currentPage": 1,
    "previous": true,
    "items": [{
            "title": "Systems integration to boost NHS communication",
            "date": "21 January 2020",
            "relatedLinks": [{
                "title": "Hospital Admitted Care Activity 2018-19",
                "url": "#"
            }],
            "text": "NHS staff will be able to communicate with each other through better integrated systems, thanks to new plans announced by NHS Digital.",
            "link": "#",
            "events": [{
                "startdatetime": {
                    "time": "1557822600000"
                },
                "enddatetime": {
                    "time": "1557822600000"
                }
            }]
        }, {
            "title": "NHS Digital apps and wearables team hosts Swedish delegation",
            "date": "16 February 2019",
            "relatedLinks": relatedLinks,
            "text": "Representatives from Sweden's key health organisations visited NHS Digital this week to share knowledge and experience of the apps and digital healthcare landscape.",
            "link": "#",
            "events": [{
                "startdatetime": {
                    "time": "1557822600000"
                },
                "enddatetime": {
                    "time": "1557822600000"
                }
            }]
        }, {
            "title": "Adult social care workforce statistics released by NHS Digital today",
            "date": "7 March 2018",
            "relatedLinks": relatedLinks,
            "text": "NHS Digital has today published the latest information on the number of adult social care staff who work for local authorities in England.",
            "link": "#",
            "events": [{
                "startdatetime": {
                    "time": "1557822600000"
                },
                "enddatetime": {
                    "time": "1557822600000"
                }
            }]
        }, {
            "title": "Systems integration to boost NHS communication",
            "date": "14 May 2019",
            "relatedLinks": [{
            "title": "Hospital Admitted Care Activity 2018-19",
            "url": "#"}],
            "text": "NHS staff will be able to communicate with each other through better integrated systems, thanks to new plans announced by NHS Digital.",
            "link": "#",
            "events": [{
                "startdatetime": {
                    "time": 1557822600000
                },
                "enddatetime": {
                    "time": 1557822600000
                }
            }]
        }, {
            "title": "NHS communication",
            "date": "20 May 2019",
            "relatedLinks": [{
            "title": "Hospital Admitted Care Activity 2018-19",
            "url": "#"}],
            "text": "NHS staff will be able to communicate with each other through better integrated systems, thanks to new plans announced by NHS Digital.",
            "link": "#",
            "events": [{
            "startdatetime": {
            "time": 1557822600000
            },
            "enddatetime": {
            "time": 1557822600000
            }
            }]
        }]
    } >
<#function getFilterYearLinks>
    <#assign yearLinks = [] />
    <#assign years = {'2019':'3', '2020':'2'} />
    <#assign selectedYear = '2020' />

    <#list years?keys as key>
        <#assign typeCount = years?size />
        <#assign yearLinks += [{ "key" : key, "title": key }] />
    </#list>
    <#return yearLinks />
</#function>

<#assign eventGroupHash = {
    "January": [pageable.items[0]],
    "February": [pageable.items[1]],
    "March": [pageable.items[2]],
    "May": [pageable.items[3], pageable.items[4]]
} />

<article class="article article--supplementary-info-hub" aria-label="Document Header">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="local-header article-header article-header--with-icon">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <div class="grid-row">
                        <div class="column--one-third column--reset">
                            <img src="<@hst.webfile path="images/icon-article.png" fullyQualified=true/>" alt="News article">
                        </div>
                        <div class="column--two-thirds column--reset">
                            <h1 class="local-header__title" data-uipath="document.title">Supplementary Information</h1>
                            <p class="article-header__subtitle">The latest supplementary information from NHS Digital.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--one-quarter page-block page-block--sidebar">
                <div id="sticky-nav">
                    <div class="article-section-nav-wrapper">
                        <div class="article-section-nav">
                            <h2 class="article-section-nav__title">Search</h2>
                            <script>
                                const onType = () => {
                                    const $searchInput = document.getElementById('query_sup_info');
                                    console.log('hit', $searchInput.value);


                                    console.log(document.getElementById('supplementary-results-list').children.length > 0 ? document.getElementById('supplementary-results-list').children : 'No results');
                                };
                                document.getElementById('filter_sup_info').setAttribute('onkeyup', 'return onType();');
                            </script>

                            <#assign searchLink = "" />
                            <#assign searchId = "query_sup_info" />
                            <#assign searchFormId = "filter_sup_info" />
                            <#assign buttonLabel = "Filter" />
                            <#include "../../include/search-strip.ftl">
                        </div>

                    </div>

                    <#if getFilterYearLinks()?size gt 0>
                        <#assign affix = selectedTypes?has_content?then("&type=" + selectedTypes?join("&type="), "") />
                        <@stickyNavYears getFilterYearLinks() affix></@stickyNavYears>
                    </#if>

                    <#assign links = [] />
                    <#list monthsOfTheYear() as month>
                        <#if eventGroupHash[month]??>
                            <#assign links += [{ "url": "#" + slugify(month), "title": month, "aria-label": "Jump to events starting in ${month}" }] />
                        </#if>
                    </#list>
                    <@stickyNavSections links></@stickyNavSections>
                </div>
            </div>

            <div class="column column--three-quarters page-block page-block--main">
                <#if eventGroupHash?has_content>
                    <#list monthsOfTheYear() as month>
                        <#if eventGroupHash[month]??>
                            <div class="article-section article-section--letter-group" id="${slugify(month)}">
                                <@stickyGroupBlockHeader month></@stickyGroupBlockHeader>
                                <div class="grid-row">
                                    <div class="column column--reset">
                                        <div class="hub-box-list" id="supplementary-results-list">
                                            <#list eventGroupHash[month] as item>
                                                <@hubBox item ></@hubBox>
                                            </#list>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </#if>
                    </#list>
                </#if>


                <#if pageable.totalPages gt 1>
                    <div class="article-section no-border">
                        <#--                        <#include "../../include/pagination.ftl">-->
                    </div>
                </#if>
            </div>

            <div class="column column--one-third">
                <div class="article-section">
                    <div class="grid-row">
                        <div class="column column--reset">
                            <@hst.include ref="component-rightpane"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</article>

