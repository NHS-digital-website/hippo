<#ftl output_format="HTML">
<#include "textSection.ftl">
<#include "websiteSection.ftl">
<#include "imageSection.ftl">
<#include "imagePairSection.ftl">
<#include "chartSection.ftl">
<#include "related-linkSection.ftl">
<#include "emphasisBox.ftl">
<#include "iconList.ftl">
<#include "gallerySection.ftl">
<#include "codeSection.ftl">
<#include "downloadSection.ftl">
<#include "expander.ftl">
<#include "ctaSection.ftl">
<#include "checklist.ftl">
<#include "quoteSection.ftl">
<#include "../component/infoGraphic.ftl">

<!-- This is a load of global setup for the highcharts config -->
<#include "highChartsSetup.ftl">

<!-- Set up equation support from mathjax -->
<script async src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.4/latest.js?config=TeX-AMS_HTML"></script>

<#macro sections sections mainHeadingLevel=2 wrap=false>

    <#assign numberedListCount=0 />
    <#assign isPreviousEmphasisBox = false />
    <#list sections as section>
        <#if wrap>
        <div class="article-section no-border">
        </#if>
            <#if section.sectionType == 'text'>
                <@textSection section=section />
            <#elseif section.sectionType == 'website-section'>
                <#if section.isNumberedList>
                    <#assign numberedListCount++ />
                </#if>
                <@websiteSection section=section isPreviousSectionEmphasisBox=isPreviousEmphasisBox numberedListCount=numberedListCount mainHeadingLevel=mainHeadingLevel />
                <#assign isPreviousEmphasisBox = false />
            <#elseif section.sectionType == 'image'>
                <@imageSection section=section />
            <#elseif section.sectionType == 'imagePair'>
                <@imagePairSection section=section />
            <#elseif section.sectionType == 'relatedLink'>
                <@relatedLinkSection section=section />
            <#elseif section.sectionType == 'chart'>
                <@chartSection section=section type='chart' size='400'/>
            <#elseif section.sectionType == 'map'>
                <@chartSection section=section type='mapChart' size='600'/>
            <#elseif section.sectionType == 'emphasisBox'>
                <#-- set flag to alter styling (no top line between emphasis boxes) -->
                <#assign isPreviousEmphasisBox = true />
                <@emphasisBox section=section />
            <#elseif section.sectionType == 'iconList'>
                <@iconList section=section mainHeadingLevel=mainHeadingLevel />
            <#elseif section.sectionType == 'gallerySection'>
                <@gallerySection section=section mainHeadingLevel=mainHeadingLevel />
            <#elseif section.sectionType == 'code'>
                <@codeSection section=section mainHeadingLevel=mainHeadingLevel />
            <#elseif section.sectionType == 'download'>
                <@downloadSection section=section mainHeadingLevel=mainHeadingLevel />
            <#elseif section.sectionType == 'expander'>
                <@expander section />
            <#elseif section.sectionType == 'ctabutton'>
                <@ctaSection section=section/>
            <#elseif section.sectionType == 'checklist'>
                <@checklist section=section/>
            <#elseif section.sectionType == 'quote'>
                <@quoteSection section=section/>
            <#elseif section.sectionType == 'infographic'>
                <@infoGraphic graphic=section/>
            </#if>
        <#if wrap>
        </div>
        </#if>
    </#list>
</#macro>
