<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<@hst.setBundle basename="homepage.website.labels"/>
<@fmt.message key="about-us.upcomingEventsTitle" var="title"/>
<@fmt.message key="about-us.upcomingEventsButtonLabel" var="buttonLabel"/>

<#if pageable?? && pageable.items?has_content>
    <h3 class="list-title">${title}</h3>
    <ol class="list list--reset cta-list">
    <#list pageable.items as item>
        <li>
            <article class="cta">
                <h2 class="cta__title"><a href="#">${item.title}</a></h2>
                <time class="cta__meta" datetime="2018">Date: 20 January 2018</time>
            </article>
        </li>    
    </#list>
    </ol>

    <a href="/news-and-events/events" class="button">${buttonLabel}</a>
</#if>