<#ftl output_format="HTML">
<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Roadmap" -->

<#include "../include/imports.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/roadmapItemBox.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/contentPixel.ftl">
<#include "macro/heroes/hero.ftl">
<#include "macro/heroes/hero-options.ftl">
<#include "macro/toolkit/organisms/filterMenu.ftl">
<#include "macro/toolkit/molecules/roadmap.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/stickyNavTags.ftl">

<#assign inArray="uk.nhs.digital.freemarker.utils.InArray"?new() />
<#assign byStartDate="uk.nhs.digital.freemarker.roadmap.RoadmapItemSorterByStartDate"?new() />
<#assign byEndDate="uk.nhs.digital.freemarker.roadmap.RoadmapItemSorterByEndDate"?new() />
<#assign getIntersection="uk.nhs.digital.freemarker.roadmap.CategoryListIntersectionHelper"?new() />
<#assign queryHelper="uk.nhs.digital.freemarker.utils.QueryStringHelper"?new() />

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="publicationsystem.headers"/>
<#assign interval = document.granularity />
<#assign quartersOfTheYear = ["January - March", "April - June", "July - September", "October - December"] />

<#assign groupedDatesHash = {} />
<#assign quarterGroupHash = {} />
<#assign monthYearGroupHash = {} />
<#assign yearGroupHash = {} />
<#assign futureGroup = [] />

<#assign filters = {} />

<#list (orderBy == 'startDate')?then(byStartDate(document.item),byEndDate(document.item)) as item>
    <#if !item.roadmapItemStatuses?? || (item.roadmapItemStatuses?has_content && item.roadmapItemStatuses.status?lower_case != 'archive')>

        <#if item.categoryLink?has_content>
            <#assign category = item.categoryLink />
            <#assign categoryKey = slugify(category) />
            <#assign filters += {  categoryKey : (filters[categoryKey]![]) + [category] } />
        </#if>

        <#if !item.effectiveDate.dateScale?has_content || item.effectiveDate.dateScale?lower_case != 'future'>
            <#if orderBy == 'startDate'>
                <@fmt.formatDate value=item.effectiveDate.startDate.time type="Date" pattern="yyyy-MM" var="monthYear" timeZone="${getTimeZone()}" />
                <@fmt.formatDate value=item.effectiveDate.startDate.time type="Date" pattern="yyyy" var="year" timeZone="${getTimeZone()}" />
                <@fmt.formatDate value=item.effectiveDate.startDate.time type="Date" pattern="MM" var="month" timeZone="${getTimeZone()}" />
            <#else>
                <@fmt.formatDate value=item.effectiveDate.endDate.time type="Date" pattern="yyyy-MM" var="monthYear" timeZone="${getTimeZone()}" />
                <@fmt.formatDate value=item.effectiveDate.endDate.time type="Date" pattern="yyyy" var="year" timeZone="${getTimeZone()}" />
                <@fmt.formatDate value=item.effectiveDate.endDate.time type="Date" pattern="MM" var="month" timeZone="${getTimeZone()}" />
            </#if>

            <#if selectedTypes?size == 0 || item.categoryLink?has_content && selectedTypes?seq_contains(slugify(item.categoryLink))>
                <#assign quarter = year + "Q" + ((month?number-1) / 3 +1)?int />
                <#assign monthYearGroupHash += {  monthYear : (monthYearGroupHash[monthYear]![]) + [ item ] } />
                <#assign yearGroupHash += {  year : (yearGroupHash[year]![]) + [ item ] } />
                <#assign quarterGroupHash += {  quarter : (quarterGroupHash[quarter]![]) + [ item ] } />
            </#if>
        <#else>
            <#if selectedTypes?size == 0 || item.categoryLink?has_content && selectedTypes?seq_contains(slugify(item.categoryLink))>
                <#assign futureGroup += [item] />
            </#if>
        </#if>
    </#if>
</#list>


<#if interval == "Monthly" || interval == "Precise date">
    <#assign groupedDatesHash = monthYearGroupHash />
<#elseif interval == "Quarterly">
    <#assign groupedDatesHash = quarterGroupHash />
<#elseif interval == "Yearly">
    <#assign groupedDatesHash = yearGroupHash />
</#if>

<#function getCurrentQuarter>
    <@fmt.formatDate value=.now type="Date" pattern="yyyy-MM" var="currentMonthYear" timeZone="${getTimeZone()}" />
    <#assign year = currentMonthYear?keep_before("-") />
    <#assign month = currentMonthYear?keep_after("-") />
    <#assign quarter = (month?number / 3)?int />
    <#return quartersOfTheYear[quarter] + " " + year />
</#function>

<#function getCurrentMonthYear>
    <@fmt.formatDate value=.now type="Date" pattern="MMMM yyyy" var="currentMonthYear" timeZone="${getTimeZone()}" />
    <#return currentMonthYear />
</#function>

<#function getCurrentYear>
    <@fmt.formatDate value=.now type="Date" pattern="yyyy" var="currentYear" timeZone="${getTimeZone()}" />
    <#return currentYear />
</#function>

<#function getDisplayDate dateString>
    <#if interval == "Monthly" || interval == "Precise date">
        <#assign year= dateString?keep_before("-") />
        <#assign month = monthsOfTheYear()[dateString?keep_after("-")?number-1] />
        <#assign monthYear = month + " " + year />
        <#return (monthYear == getCurrentMonthYear())?then("Current changes", monthYear) />
    <#elseif interval == "Quarterly">
        <#assign year= dateString?keep_before("Q") />
        <#assign quarterString = quartersOfTheYear[dateString?keep_after("Q")?number-1] + " " + year />
        <#return (quarterString == getCurrentQuarter())?then("Current changes", quarterString) />
    <#else>
        <#return (dateString == getCurrentYear())?then("Current changes", dateString) />
    </#if>
</#function>


<#assign sectionTitlesFound = countSectionTitles(document.item) />
<@fmt.message key="headers.about-this-publication" var="aboutThisPublicationHeader" />

<#macro roadmapItem item>
    <#assign itemData = { "title": item.title, "text": item.shortsummary } />

    <@hst.link hippobean=item var="itemLink" />
    <#assign itemData += { "link": itemLink } />

    <@fmt.formatDate value=item.effectiveDate.startDate.time type="Date" pattern="MMM-yyyy" var="startdate" timeZone="${getTimeZone()}" />
    <@fmt.formatDate value=item.effectiveDate.endDate.time type="Date" pattern="MMM-yyyy" var="enddate" timeZone="${getTimeZone()}" />

    <#if orderBy == 'startDate'>
        <@fmt.formatDate value=item.effectiveDate.startDate.time type="Date" pattern="yyyy-MM-dd" var="datetime" timeZone="${getTimeZone()}" />
        <@fmt.formatDate value=item.effectiveDate.startDate.time type="Date" pattern="MMMM yyyy" var="datelabel" timeZone="${getTimeZone()}" />
    <#else>
        <@fmt.formatDate value=item.effectiveDate.endDate.time type="Date" pattern="yyyy-MM-dd" var="datetime" timeZone="${getTimeZone()}" />
        <@fmt.formatDate value=item.effectiveDate.endDate.time type="Date" pattern="MMMM yyyy" var="datelabel" timeZone="${getTimeZone()}" />
    </#if>

    <#if item.categoryLink?has_content>
        <#assign itemData += { "category": item.categoryLink } />
    </#if>
    <#assign itemData += { "monthsDuration": item.effectiveDate.getMethodNames(startdate, enddate)} />
    <#assign itemData += { "datetime": datetime } />
    <#assign itemData += { "datelabel": datelabel} />
    <#if item.roadmapItemStatuses?? >
        <#assign itemData += { "status": item.roadmapItemStatuses.status } />
    </#if>
    <#if orderBy == "startDate">
        <#assign itemData += { "order": "start"} />
    <#else>
        <#assign itemData += { "order": "end"} />
    </#if>
    <#assign  itemData += {"showDate":  (item.effectiveDate.dateScale?lower_case != 'future') } />

    <@roadmapItemBox itemData></@roadmapItemBox>
</#macro>

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article>
    <#assign heroCategories><#list document.roadmapCategories as roadmapCategory>${roadmapCategory.name}<#sep>,<br /></#sep></#list></#assign>

    <#assign heroType = "default" />
    <#assign heroOptions = getHeroOptions(document) />
    <#assign heroOptions += {
        "metaData": [{
            "title": "Categories",
            "value": heroCategories
        }],
        "colour": "darkBlue"
    }/>
    <@hero heroOptions heroType />

    <div class="nhsd-t-grid nhsd-!t-margin-top-4">
        <div class="nhsd-t-row nhsd-!t-display-m-show nhsd-!t-display-hide">
            <div class="nhsd-t-col nhsd-!t-margin-top-2">
                <nav class="nhsd-m-tabs" data-uipath="ps.service-tabs">
                    <div role="tablist">
                        <a class="nhsd-a-tab nhsd-a-tab__active" href="#" aria-selected="false" role="tab" data-tab-content="roadmap-list-view">List View</a>
                        <a class="nhsd-a-tab " href="#" aria-selected="false" role="tab" data-tab-content="roadmap-gantt-view">Gantt Chart</a>
                    </div>
                </nav>
                <hr class="nhsd-a-horizontal-rule nhsd-!t-margin-top-0">
            </div>
        </div>
        <div class="nhsd-t-row" id="roadmap-list-view">
            <div class="nhsd-t-col-m-3 nhsd-t-col-xs-12 nhsd-!t-margin-top-4">
                <form id="roadmap-form" method="get">
                    <div class="nhsd-!t-display-hide nhsd-!t-display-m-show nhsd-!t-margin-bottom-8">
                        <span id="sticky-nav-header" class="nhsd-t-heading-xs nhsd-!t-margin-bottom-0">Filters</span>

                        <#if filters?has_content>
                            <#assign tags = [] />
                            <#list filters?keys as key>
                                <#assign typeCount = filters[key]?size />
                                <#assign tags += [{ "key" : key, "title": filters[key][0]?cap_first + " (${typeCount})" }] />
                            </#list>
                            <#assign affix = "" />
                            <#if orderBy == 'startDate'>
                                <#assign affix += "&order-by=start-date" />
                            </#if>

                            <@stickyNavTags tags affix "Filter by type" "type" selectedTypes></@stickyNavTags>
                        </#if>

                        <div id="nhsd-filter-menu">
                            <div class="nhsd-m-filter-menu-section nhsd-m-filter-menu-section--active" data-active-menu="sortBy">
                                <div class="nhsd-m-filter-menu-section__accordion-heading">
                                    <button class="nhsd-m-filter-menu-section__menu-button" type="button" aria-expanded="true">
                                            <span class="nhsd-m-filter-menu-section__heading-text nhsd-t-body-s">
                                                <span>
                                                    <span class="nhsd-a-icon nhsd-a-icon--size-xxs">
                                                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
                                                            <path d="M8,12L1,5.5L2.5,4L8,9.2L13.5,4L15,5.5L8,12z"></path>
                                                        </svg>
                                                    </span>
                                                </span>
                                                Sort by
                                            </span>
                                    </button>
                                    <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-xxs">

                                    <div class="nhsd-m-filter-menu-section__accordion-panel nhsd-!t-margin-top-2 nhsd-!t-margin-bottom-2">
                                        <div class="nhsd-m-filter-menu-section__option-row">
                                            <span class="nhsd-a-checkbox">
                                                <label>
                                                    Start Date
                                                    <input name="order-by" type="radio" value="start-date" onchange="document.getElementById('roadmap-form').submit()" ${(orderBy=="startDate")?then('checked', '')}>
                                                    <span class="nhsd-a-checkbox__label">Start Date</span>
                                                    <span class="checkmark"></span>
                                                </label>
                                            </span>
                                        </div>
                                    </div>
                                    <div class="nhsd-m-filter-menu-section__accordion-panel nhsd-!t-margin-top-2 nhsd-!t-margin-bottom-2">
                                        <div class="nhsd-m-filter-menu-section__option-row">
                                            <span class="nhsd-a-checkbox">
                                                <label>
                                                    End Date
                                                    <input name="order-by" type="radio" value="end-date" onchange="document.getElementById('roadmap-form').submit()" ${(orderBy=="endDate")?then('checked', '')}>
                                                    <span class="nhsd-a-checkbox__label">End Date</span>
                                                    <span class="checkmark"></span>
                                                </label>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div>
                        <#assign sections = [] />
                        <#list groupedDatesHash?keys?sort as k>
                            <#assign displayDate= getDisplayDate(k) />
                            <#assign sections += [{ "url": "#" + slugify(displayDate), "title": displayDate, "aria-label": "Jump to items starting in ${displayDate}" }] />
                        </#list>
                        <#if futureGroup?size != 0>
                            <#assign sections += [{ "url": "#future", "title": "Future", "aria-label": "Jump to items starting in future" }] />
                        </#if>
                        <#if sections?size != 0>
                            <#assign links = getStickySectionNavLinks({ "document": document, "sections": sections, "includeTopLink": false }) />
                            <@stickyNavSections links></@stickyNavSections>
                        </#if>
                    </div>
                </form>
            </div>

            <div class="nhsd-t-col-m-8 nhsd-t-col-xs-12 nhsd-t-off-m-1 nhsd-!t-margin-top-4">
                <#list groupedDatesHash?keys?sort as key>
                    <div id="${slugify(getDisplayDate(key))}" class="nhsd-!t-margin-bottom-8">
                        <h2 class="nhsd-t-heading-l">${getDisplayDate(key)}</h2>
                        <#list groupedDatesHash[key] as item>
                            <#if item.roadmapItemStatuses?? && inArray(item.roadmapItemStatuses.status?lower_case, ['complete'])>
                                <@roadmapItem item />
                                <hr class="nhsd-a-horizontal-rule">
                            </#if>
                        </#list>
                        <#list groupedDatesHash[key] as item>
                            <#if !item.roadmapItemStatuses?? || (item.roadmapItemStatuses?? && !inArray(item.roadmapItemStatuses.status?lower_case, ['complete', 'cancelled', 'superseded']))>
                                <@roadmapItem item />
                                <hr class="nhsd-a-horizontal-rule">
                            </#if>
                        </#list>
                        <#list groupedDatesHash[key] as item>
                            <#if item.roadmapItemStatuses?? && inArray(item.roadmapItemStatuses.status?lower_case, ['cancelled', 'superseded'])>
                                <@roadmapItem item />
                                <hr class="nhsd-a-horizontal-rule">
                            </#if>
                        </#list>
                    </div>
                </#list>
                <#if futureGroup?size != 0>
                    <div id="future" class="nhsd-!t-margin-bottom-8">
                        <h2 class="nhsd-t-heading-l">Future</h2>
                        <#list futureGroup as item>
                            <@roadmapItem item />
                            <hr class="nhsd-a-horizontal-rule">
                        </#list>
                    </div>
                </#if>
            </div>
        </div>
        <div class="nhsd-t-row" id="roadmap-gantt-view">
            <div class="nhsd-t-col">
                <h2 class="nhsd-t-heading-l">Gantt Chart</h2>

                <#assign roadmapOptions = {} />
                <#assign roadmapDateList = [] />
                <#list roadmapDates as roadmapDate>
                    <@fmt.formatDate value=roadmapDate.time type="Date" pattern="yyyy" var="roadmapYear" />
                    <#if document.granularity == "Monthly" || document.granularity == "Precise date">
                        <@fmt.formatDate value=roadmapDate.time type="Date" pattern="MMMM" var="roadmapMonth" />
                        <#assign roadmapDateList += [roadmapMonth + " " + roadmapYear] />
                    <#elseif document.granularity == "Quarterly">
                        <@fmt.formatDate value=roadmapDate.time type="Date" pattern="MM" var="roadmapMonth" />
                        <#assign roadmapQuarter = 1 />
                        <#if roadmapMonth?number gt 3 && roadmapMonth?number lte 6>
                            <#assign roadmapQuarter = 2 />
                        <#elseif roadmapMonth?number gt 6 && roadmapMonth?number lte 9>
                            <#assign roadmapQuarter = 3 />
                        <#elseif roadmapMonth?number gt 9 && roadmapMonth?number lte 12>
                            <#assign roadmapQuarter = 4 />
                        </#if>
                        <#assign roadmapDateList += ["Q" + roadmapQuarter + " " + roadmapYear] />
                    <#elseif document.granularity == "Yearly">
                        <#assign roadmapDateList += [roadmapYear] />
                    </#if>
                </#list>

                <#assign categorisedItems = [] />
                <#list document.roadmapCategories as category>
                    <#assign categoryItemsList = [] />
                    <#list roadmapDates as roadmapDate>
                        <#assign dateCategoryItems = [] />
                        <#list document.item as item>
                            <@fmt.formatDate value=roadmapDate.time type="Date" pattern="yyyyMM" var="roadmapDateString" />
                            <#if !item?is_last>
                                <@fmt.formatDate value=roadmapDates[roadmapDate?index + 1].time type="Date" pattern="yyyyMM" var="roadmapEndDateString" />
                            </#if>
                            <@fmt.formatDate value=item.effectiveDate.startDate.time type="Date" pattern="yyyyMM" var="itemDateString" />
                            <#if itemDateString?number gte roadmapDateString?number && (roadmapDate?is_last || itemDateString?number lt roadmapEndDateString?number) && item.categoryLink == category.name>
                                <#assign dateCategoryItems += [{
                                    "options": roadmapItemsOptions[item.id],
                                    "item": item
                                }] />
                            </#if>
                        </#list>
                        <#assign categoryItemsList += [dateCategoryItems] />
                    </#list>
                    <#assign categorisedItems += [{
                        "name": category.name,
                        "items": categoryItemsList
                    }] />
                </#list>

                <@nhsdRoadmap { "dates": roadmapDateList, "items": categorisedItems } />
            </div>
        </div>
    </div>
</article>
