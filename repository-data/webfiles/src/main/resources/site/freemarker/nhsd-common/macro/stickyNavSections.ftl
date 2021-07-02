<#ftl output_format="HTML">

<#assign isStickySectionMainHeading="uk.nhs.digital.freemarker.indices.IsStickySectionMainHeading"?new() />

<#macro stickyNavSections links title="Page contents">
    <div class="nhsd-m-sticky-navigation nhsd-!t-display-sticky nhsd-!t-display-sticky--offset-2 nhsd-!t-margin-bottom-6">
        <span id="sticky-nav-header" class="nhsd-t-heading-xs nhsd-!t-margin-bottom-2">${title}</span>
        <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-xs nhsd-!t-margin-bottom-2"/>

        <#assign numberedListCount=0 />
        <#if links??>
           <nav aria-labelledby="sticky-nav-header">
                <ul>
                    <#list links as link>
                        <#assign label = "Scroll to '${link.title}'" />
                        <#if link["aria-label"]??>
                            <#assign label = link["aria-label"] />
                        </#if>
                        <#if link.isNumberedList?? && link.isNumberedList>
                            <#assign numberedListCount++ />
                            <#assign numberedLinkTitle = numberedListCount + ". " + link.title>
                            <li class="nhsd-m-sticky-navigation__item nhsd-m-sticky-navigation__item--nested" data-nav-content="${slugify(link.title)}">
                                <a href="${link.url}" aria-label="${label}" title="${label}" class="nhsd-a-link">${numberedLinkTitle}</a>
                            </li>
                        <#else>
                            <li class="nhsd-m-sticky-navigation__item" data-nav-content="${slugify(link.title)}">
                                <a href="${link.url}" aria-label="${label}" title="${label}" class="nhsd-a-link">${link.title}</a>
                            </li>
                        </#if>
                    </#list>
                </ul>
            </nav>
        </#if>
    </div>
</#macro>

<#-- Gather section nav links in a hash -->
<#function getStickySectionNavLinks options>

    <#assign bundles = ["rb.generic.headers"] />
    <#if options??>
        <#if options.keepBundles??>
            <#assign bundles += options.keepBundles />
        </#if>
    </#if>
    <#assign var1 = bundles?join(",") />
    <@hst.setBundle basename="${var1}" />

    <#assign links = [] />
    <#if options??>
        <#if options.includeTopLink?? && options.includeTopLink>
            <#assign links = [{ "url": "#top", "title": "Top of page" }] />
        </#if>

        <#if options.includeSummary?? && options.includeSummary>
            <@fmt.message key="headers.summary" var="summaryHeader" />
            <#assign links += [{ "url": "#summary", "title": summaryHeader }] />
        </#if>

        <#if options.sections??>
            <#assign links += options.sections />
        </#if>

        <#if options.document??>
            <#if options.document.sections?has_content>
                <#list options.document.sections as section>
                    <#if isStickySectionMainHeading(section)>
                        <#if section.isNumberedList??>
                          <#assign links += [{ "url": "#" + slugify(section.heading), "title": section.heading, "isNumberedList": section.isNumberedList}] />
                        <#else>
                          <#assign links += [{ "url": "#" + slugify(section.heading), "title": section.heading, "isNumberedList": false}] />
                        </#if>
                    </#if>
                </#list>
            </#if>
            <#if options.document.contactdetails?? && options.document.contactdetails.content?has_content>
                <@fmt.message key="headers.contact-details" var="contactDetailsHeader" />
                <#assign links += [{ "url": "#contact-details", "title": contactDetailsHeader }] />
            </#if>
        </#if>
        <#if document?? && document != "simulating_doc" && document.relatedNews?? && (!document.latestNews?? || !document.latestNews?has_content) && document.relatedNews?has_content >
            <@fmt.message key="headers.related-news" var="relatedNewsHeader" />
            <#assign links += [{ "url": "#" + slugify(relatedNewsHeader), "title": relatedNewsHeader }] />
        </#if>
        <#if options.childPages?? && options.childPages?has_content>
            <@fmt.message key="headers.further-information" var="furtherInformationHeader" />
            <#assign links += [{ "url": "#further-information", "title": furtherInformationHeader }] />
        </#if>

        <#if options.appendSections?has_content>
            <#list options.appendSections as section>
                <#assign links += [{ "url": "#" + slugify(section.title), "title": section.title, "isNumberedList": false}] />
            </#list>
        </#if>
    </#if>

    <#return links />
</#function>
