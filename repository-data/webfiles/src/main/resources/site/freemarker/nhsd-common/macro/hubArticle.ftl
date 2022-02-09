<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "metaTags.ftl">
<#include "component/showAll.ftl">
<#include "contentPixel.ftl">
<#include "../macro/card.ftl">
<#include "../macro/gridColumnGenerator.ftl">
<#include "../helpers/author-info.ftl">

<#macro hubArticles latestArticles wide=false>
    <#assign cardSize = "nhsd-t-col-xs-12 nhsd-t-col-s-6 nhsd-t-col-l-4 nhsd-!t-margin-bottom-6" />
    <#if wide>
        <#assign cardSize = "nhsd-t-col nhsd-!t-margin-bottom-6"/>
    </#if>
    <div class="nhsd-t-grid nhsd-t-grid--nested">
        <div class="nhsd-t-row">
            <#list latestArticles as latest>
                <#if latest?is_first>
                    <div class="nhsd-t-col nhsd-!t-margin-bottom-6">
                        <@hubArticleItem item=latest feature=true />
                    </div>
                <#else>
                    <#assign hideItem = (latest?index gte 13)?then('js-hide-article', '') />
                    <div class="${hideItem} ${cardSize}">
                        <@hubArticleItem item=latest />
                    </div>

                    <#if latest?index == 13>
                        <div class="nhsd-!t-display-hide js-show-all-articles-btn nhsd-t-col nhsd-!t-margin-bottom-6 nhsd-!t-text-align-center">
                            <button class="nhsd-a-button">Show All (${latestArticles?size})</button>
                        </div>
                    </#if>
                </#if>
            </#list>
            <#if latestArticles?size gte 13>
                <div class="nhsd-!t-display-hide js-show-less-articles-btn nhsd-t-col nhsd-!t-margin-bottom-6 nhsd-!t-text-align-center">
                    <button class="nhsd-a-button">Show Less</button>
                </div>
            </#if>
        </div>
    </div>
</#macro>

<#macro hubArticleItem item feature=false>
    <#assign cardClass = "" />
    <#assign imgClass = "" />
    <#if feature>
        <#assign cardClass = "nhsd-m-card--image-position-adjacent" />
        <#assign imgClass = "nhsd-a-image--cover" />
    </#if>

    <#assign authors = authorInfo(item) />
    <@hst.link var="cardLink" hippobean=item/>

    <#assign cardData = {
        'title': item.title,
        'link': cardLink,
        'shortsummary': item.shortsummary?has_content?then(item.shortsummary, ''),
        'background': 'light-grey',
        'authorsInfo': authors,
        'featured': feature,
        'cardClass': 'nhsd-m-card--full-height',
        'contentType': getDocTypeName(item.class.name)
    } />
    
    <#if hst.isBeanType(item, 'uk.nhs.digital.website.beans.CyberAlert')>
    	<#assign tag1 = {'text': item.threatId}/>
    	<#assign tag2 = {'text': item.severity, 'colour': 'nhsd-a-tag--bg-light-red'}/>
    	<#assign cardData += {
    		'background': 'white',
    		'tags': [tag1, tag2],
    		'threat', item.threatType,
    		'lastModified': item.lastModified,
    		'publishedDate': item.publishedDate
    	} />
    </#if>

    <#-- DW-2686 if there is a thumbnail image use it else use the lead image -->
    <#if item.thumbnailImage?has_content>
        <#assign cardData += {
            'image': item.thumbnailImage
        } />
    <#elseif item.leadImage?has_content>
        <#assign cardData += {
            'image': item.leadImage
        } />
    </#if>

    <@card cardData />
</#macro>
