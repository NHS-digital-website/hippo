<#ftl output_format="HTML">

<#--
Helper functions to automatically set hero options from a document.

Example use:
<@hero getHeroOptions(document) />
-->
<#function getHeroOptions document isPolicy=false>
    <#assign options = {
    "title": document.title
    }>
    <#assign content="">
    <#if document.summary?has_content>
        <#assign content = document.summary/>
    <#elseif document.shortsummary?has_content>
        <#assign content = document.shortsummary/>
    <#elseif document.content?has_content>
        <#assign content = document.content/>
    <#elseif document.richContent?has_content>
        <#assign content = document.richContent/>
    </#if>
    <#assign options += { "summary": content }/>

    <#assign categoryInfo="">
    <#if document.categoryInfo?has_content>
        <#assign categoryInfo = document.categoryInfo/>
    </#if>
    <#assign options += { "categoryInfo": categoryInfo }/>

    <#if document.image?has_content || document.leadImage?has_content || document.bannerImage?has_content && document.bannerImage.pageHeaderHeroModule?has_content
    || (document.publicationStyle?has_content && document.publicationStyle.bannerImage?has_content)>
        <#assign alt = ""/>
        <#if document.leadImageAltText?has_content>
            <#assign alt = document.leadImageAltText/>
        <#elseif document.altText?has_content>
            <#assign alt = document.altText/>
        <#elseif document.image?has_content && document.image.description?has_content>
            <#assign alt = document.image.description/>
        <#elseif document.bannerImageAltText?has_content>
            <#assign alt = document.bannerImageAltText/>
        <#elseif isPolicy && document.publicationStyle.bannerImage?has_content>
            <#assign alt = document.publicationStyle.imageAltText/>
        </#if>

        <#if document.image?has_content>
            <@hst.link hippobean=document.image.original fullyQualified=true var="imageSrc" />
            <#assign image = {
            "src": imageSrc,
            "alt": alt
            }/>
        <#elseif document.leadImage?has_content>
            <@hst.link hippobean=document.leadImage.original fullyQualified=true var="imageSrc" />
            <#assign image = {
            "src": imageSrc,
            "alt": alt
            }/>
        <#elseif document.bannerImage?has_content && document.bannerImage.pageHeaderHeroModule?has_content>
            <@hst.link hippobean=document.bannerImage.pageHeaderHeroModule fullyQualified=true var="imageSrc" />
            <#assign image = {
            "src": imageSrc,
            "alt": alt
            }/>
        <#elseif isPolicy && document.publicationStyle.bannerImage?has_content>
            <@hst.link hippobean=document.publicationStyle.bannerImage.pageHeaderSlimBannerSmall2x fullyQualified=true var="imageSrc" />
            <#assign image = {
            "src": imageSrc,
            "alt": alt
            }/>
        </#if>
        <#if image?has_content>
            <#assign options += { "image": image }/>
        </#if>
    </#if>

    <#return options/>
</#function>

<#function getHeroOptionsWithMetaData document isPolicy=false>

    <#assign options = getHeroOptions(document,isPolicy) />

    <#assign metadata = [] />

    <#assign datePublishedLabel = "Date Published" />
    <#if document.publishedDate?has_content>
        <@fmt.formatDate value=document.publishedDate.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" var="date" />
        <#assign metadata += [{
        "title": datePublishedLabel,
        "value": date
        }]/>
    <#elseif document.publicationDate?has_content>
        <@fmt.formatDate value=document.publicationDate.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" var="date" />
        <#assign metadata += [{
        "title": datePublishedLabel,
        "value": date
        }]/>
    </#if>

    <#assign options += { "metaData": metadata }/>
    <#return options/>
</#function>