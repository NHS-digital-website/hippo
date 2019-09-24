<#ftl output_format="HTML">

<#macro sectionNav links title="Page contents">
<div class="article-section-nav-wrapper">
    <div class="article-section-nav">
        <h2 class="article-section-nav__title">${title}</h2>
        <#assign numberedListCount=0 />
        <#if links??>
            <nav>
                <ol class="article-section-nav__list">
                    <#list links as link>
                        <#assign label = "Scroll to '${link.title}'" />
                        <#if link["aria-label"]??>
                            <#assign label = link["aria-label"] />
                        </#if>
                        <#if link.isNumberedList?has_content && link.isNumberedList == "true">
                            <#assign numberedListCount++ />
                            <#assign numberedLinkTitle = numberedListCount + ". " + link.title>
                            <li class="section-numbered">
                                <a href="${link.url}" aria-label="${label}" title="${label}">${numberedLinkTitle}</a>
                            </li>
                        <#else>
                            <li>
                                <a href="${link.url}" aria-label="${label}" title="${label}">${link.title}</a>
                            </li>
                        </#if>
                    </#list>
                </ol>
            </nav>
        </#if>
    </div>
</div>
</#macro>

<#-- Gather section nav links in a hash -->
<#function getSectionNavLinks options>
    <@hst.setBundle basename="rb.generic.headers"/>

    <#assign links = [] />
    <#if options??>
        <#if options.includeTopLink?? && options.includeTopLink>
            <#assign links = [{ "url": "#top", "title": "Top of page" }] />
        </#if>

        <#if options.ignoreSummary?? && options.ignoreSummary>
        <#else>
            <@fmt.message key="headers.summary" var="summaryHeader" />
            <#assign links += [{ "url": "#summary", "title": summaryHeader }] />
        </#if>

        <#if options.document??>
            <#if options.document.sections?has_content>
                <#list options.document.sections as section>
                    <#if includeInSideNav(section)>
                        <#assign isNumberedList = false />
                        <#if section.isNumberedList??>
                            <#assign isNumberedList = section.isNumberedList />
                        </#if>
                        <#if section.title?has_content>
                            <#assign links += [{ "url": "#" + slugify(section.title), "title": section.title, "isNumberedList": isNumberedList?c }] />
                        <#elseif section.heading?has_content>
                            <#assign links += [{ "url": "#" + slugify(section.heading), "title": section.heading, "isNumberedList": isNumberedList?c }] />
                        </#if>
                    </#if>
                </#list>
            </#if>
            <#if options.document.contactdetails?? && options.document.contactdetails.content?has_content>
                <@fmt.message key="headers.contact-details" var="contactDetailsHeader" />
                <#assign links += [{ "url": "#contact-details", "title": contactDetailsHeader }] />
            </#if>
        </#if>
        <#if options.childPages?? && options.childPages?has_content>
            <@fmt.message key="headers.further-information" var="furtherInformationHeader" />
            <#assign links += [{ "url": "#further-information", "title": furtherInformationHeader }] />
        </#if>
        <#if options.links??>
            <#assign links += options.links />
        </#if>
    </#if>

    <#return links />
</#function>

<#function includeInSideNav section>
    <#return (section.title?has_content || section.heading?has_content) &&
    (
        (
            section.sectionType == 'website-section'  &&
            (
                !section.headingLevel?has_content ||
                section.headingLevel == 'Main heading'
            )
        )
        ||
        (
            (
                section.sectionType == 'gallerySection' ||
                section.sectionType == 'iconList' ||
                section.sectionType == 'code' ||
                section.sectionType == 'download'
            )
            && section.headingLevel == 'Main heading'
        )
    ) />
</#function>

<#-- Get section links in multiple elements -->
<#function getNavLinksInMultiple sectionCompounds idprefix=''>
    <#assign links = [] />

    <#list sectionCompounds as compound>
        <#if compound.title?has_content>
            <#assign links += [{ "url": "#${idprefix}" + slugify(compound.title), "title": compound.title}] />
            <#if compound.sections?has_content>
                <#list compound.sections as section>
                    <#if section.title?has_content>
                        <#assign links += [{ "url": "#" + slugify(section.title), "title": section.title}] />
                    </#if>
                </#list>
            </#if>
        </#if>
    </#list>

    <#return links />
</#function>

<#-- Count the sections in multiple elements -->
<#function countSectionTitlesInMultiple sectionCompounds>
    <#local titlesFound = 0 />
    <#list sectionCompounds as compound>
        <#if compound.sections??>
            <#list compound.sections as section>
                <#if section.title?has_content>
                    <#local titlesFound += 1 />
                </#if>
            </#list>
        </#if>
    </#list>
    <#return titlesFound />
</#function>

<#-- Count the sections with titles available -->
<#function countSectionTitles sections>
    <#local titlesFound = 0 />
    <#if sections??>
        <#list sections as section>
            <#if section.title?has_content>
                <#local titlesFound += 1 />
            </#if>
        </#list>
    </#if>
    <#return titlesFound />
</#function>
