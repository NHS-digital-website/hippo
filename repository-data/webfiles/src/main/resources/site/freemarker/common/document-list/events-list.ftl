<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<@hst.setBundle basename="homepage.website.labels"/>
<@fmt.message key="about-us.upcomingEventsTitle" var="title"/>
<@fmt.message key="about-us.upcomingEventsButtonLabel" var="buttonLabel"/>

<#if pageable?? && pageable.items?has_content>
    <div class="cta-list">
        <h3 class="list-title">${title}</h3>
        <ol class="list list-reset list-of-articles">
        <#list pageable.items as item>
            <li>
                <article class="cta">
                    <h2 class="cta__title"><a href="#">${item.title}</a></h2>
                    <time class="cta__meta" datetime="#">Date: 20 January 2018</time>
                    <#-- <#assign nomimaldate = item.getNominalPublicationDate()/>
                    <#if !nomimaldate.isRestricted()>
                    <#assign pubdate = nomimaldate.dayOfMonth + " " + nomimaldate.month +" "+ nomimaldate.year?c/>
                        <time class="cta__meta" datetime="${pubdate?date?iso_utc}T08:00">Date: ${pubdate?date}</time>
                    </#if> -->
                </article>
            </li>    
        </#list>
        </ol>

        <a href="#" class="button">${buttonLabel}</a>
    </div>
</#if>