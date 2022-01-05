<#ftl output_format="HTML">

<#include "../../include/imports.ftl">
<#include "component/showAll.ftl">

<#assign itemsMaxCount=7/>
<#assign itemsMaxCountWithJS=3/>


<#macro latestblogs blogs fromDoctype='Blog' idsuffix='latest-blogs' title="Latest blogs" centred=true>

 <#assign hasLatestBlogs = blogs?has_content && blogs?size &gt; 0 />
  <#if hasLatestBlogs>
  <div class="nhsd-o-card-list nhsd-!t-margin-bottom-4" data-max-count="${itemsMaxCount}" data-state="short">
    <div class="nhsd-t-grid nhsd-t-grid--nested">
        <#if title?has_content>
            <div class="nhsd-t-row">
                <div class="nhsd-t-col">
                    <h2 id="${slugify(title)}" class="nhsd-t-heading-xl ${centred?then('nhsd-t-text-align-center', '')} nhsd-!t-margin-top-6">${title}</h2>
                </div>
            </div>
        </#if>

        <div class="nhsd-t-row nhsd-o-card-list__items">
            <#list blogs as latest>
                <#if latest?counter <= itemsMaxCount >
                    <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4 ${idsuffix}">
                <#else>
                    <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4 nhsd-!t-display-hide ${idsuffix}">
                </#if>
                    <article class="nhsd-m-card">
                    <@hst.link hippobean=latest var="link"/>
                        <a href="${link}" class="nhsd-a-box-link" onClick="logGoogleAnalyticsEvent('Link click','${fromDoctype}','${link}');" onKeyUp="return vjsu.onKeyUp(event)" aria-label="About NHS Digital - More about what we do" title="${latest.title}" >
                            <div class="nhsd-a-box nhsd-a-box--bg-light-grey">
                                <#assign image = '' />
                                <#assign alttext = '' />
                                <#if latest.leadimagesection?? && latest.leadimagesection?has_content && latest.leadimagesection.leadImage?has_content >
                                    <#assign image = latest.leadimagesection.leadImage />
                                    <#assign alttext = latest.leadimagesection.alttext />
                                <#elseif latest.leadImage?? && latest.leadImage?has_content >
                                    <#assign image = latest.leadImage />
                                    <#assign alttext = latest.leadImageAltText />
                                </#if>
                                <#if image?has_content>
                                    <@hst.link hippobean=image.original fullyQualified=true var="leadImage" />
                                    <figure class="nhsd-a-image nhsd-a-image--maintain-ratio" style="height:max-content">
                                        <picture class="nhsd-a-image__picture">
                                            <img src="${leadImage}" alt="${alttext}"  />
                                        </picture>
                                    </figure>
                                <#else>
                                    <figure class="nhsd-a-image">
                                        <picture class="nhsd-a-image__picture">
                                            <img src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="NHS Digital article" />
                                        </picture>
                                    </figure>
                                </#if>

                                <div class="nhsd-m-card__content-box">
                                    <#if latest.authors?? && latest.authors?has_content>
                                        By <#list latest.authors as author>${author.title}<#sep>, </#list>.
                                    </#if>
                                    <span class="nhsd-m-card__date">
                                        <#assign pubtime = '' />
                                        <#if latest.dateOfPublication?has_content>
                                            <#assign pubtime = latest.dateOfPublication.time />
                                        <#elseif latest.publisheddatetime?has_content>
                                            <#assign pubtime = latest.publisheddatetime.time />
                                        </#if>

                                        <#if ! pubtime?is_string >
                                            <@fmt.formatDate value=pubtime type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" />
                                        </#if>
                                    </span>
                                    <h1 class="nhsd-t-heading-s">${latest.title}</h1>
                                    <p class="nhsd-t-body-s">${latest.shortsummary}</p>
                                </div>

                                <div class="nhsd-m-card__button-box">
                                    <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-icon--size-s nhsd-a-icon--col-black">
                                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                            <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                                        </svg>
                                    </span>
                                </div>
                            </div>
                        </a>
                    </article>
                </div>
            </#list>
        </div>
        <#if (blogs?size &gt; 3) >
            <div class="nhsd-t-row">
                <div class="nhsd-t-col">
                    <nav class="nhsd-m-button-nav">
                    <#if idsuffix?contains("news")>
                        <#if (blogs?size >= itemsMaxCount) >
                        <a id="btn-view-all" class="nhsd-a-button" href="<@hst.link siteMapItemRefId="newsfeed"/>" >
                        <#else>
                        <a id="btn-view-all" onClick="toggleCards(${itemsMaxCountWithJS}, ${blogs?size}, '${idsuffix?js_string}'); return false;" class="nhsd-a-button" href="#">
                        </#if>
                            <span class="nhsd-a-button__label">View all news</span>
                    <#elseif idsuffix?contains("event") >
                        <#if (blogs?size >= itemsMaxCount) >
                        <a id="btn-view-all" class="nhsd-a-button" href="<@hst.link siteMapItemRefId="eventsfeed"/>">
                        <#else>
                        <a id="btn-view-all" onClick="toggleCards(${itemsMaxCountWithJS}, ${blogs?size}, '${idsuffix?js_string}'); return false;" class="nhsd-a-button" href="#">
                        </#if>
                            <span class="nhsd-a-button__label">View all events</span>
                    <#else>
                        <#if (blogs?size >= itemsMaxCount) >
                        <a id="btn-view-all" class="nhsd-a-button" href="<@hst.link siteMapItemRefId="newshomepage"/>">
                        <#else>
                        <a id="btn-view-all" onClick="toggleCards(${itemsMaxCountWithJS}, ${blogs?size}, '${idsuffix?js_string}'); return false;" class="nhsd-a-button" href="#">
                        </#if>
                            <span class="nhsd-a-button__label">View all</span>
                    </#if>
                        </a>
                    </nav>
                </div>
            </div>
        </#if >
    </div>
  </div>
  </#if>

  <script>
    (function() {
          function init() {
            var cards = document.getElementsByClassName('${idsuffix}');
            for (var i=0; i < cards.length; i++) {
                if (i >= ${itemsMaxCountWithJS} && i < ${blogs?size}) {
                    cards.item(i).classList.add('nhsd-!t-display-hide');
                }
            }
          }
        init();
    }());

    function toggleCards(itemsMaxCountWithJS, numberOfCards, blogType) {
        var cards = document.getElementsByClassName(blogType);
        for (var i=0; i < cards.length; i++) {
            if (i >= itemsMaxCountWithJS && i < numberOfCards) {
                if (cards.item(i).classList.contains('nhsd-!t-display-hide')) {
                cards.item(i).classList.remove('nhsd-!t-display-hide');
                } else {
                    cards.item(i).classList.add('nhsd-!t-display-hide');
                }
            }
        }
    }
  </script>
</#macro>
