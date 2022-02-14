<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "./shareThisPage.ftl">

<#macro shareSection document>
    <h2 class="nhsd-t-heading-xl">Share this page</h2>
    <#-- Use UTF-8 charset for URL escaping from now: -->
    <#setting url_escaping_charset="UTF-8">

    <div class="nhsd-t-grid nhsd-!t-margin-bottom-4 nhsd-!t-no-gutters">
        <#--  Facebook  -->
        <#assign facebookUrl = "http://www.facebook.com/sharer.php?u=${getDocumentUrl()?url}"/>
        <#assign facebookIconPath = "/images/icon/rebrand-facebook.svg" />
        <@shareThisPage document "Facebook" facebookUrl facebookIconPath/>

        <#--  Twitter  -->
        <#assign hashtags ='' />
        <#if document.twitterHashtag?has_content>
            <#list document.twitterHashtag as tag>
                <#assign hashtags = hashtags + tag?starts_with("#")?then(tag?keep_after('#'), tag) + tag?is_last?then('', ',')>
            </#list>
        </#if>
        <#assign twitterUrl = "https://twitter.com/intent/tweet?via=nhsdigital&url=${getDocumentUrl()?url}&text=${document.title?url}&hashtags=${hashtags?url}"/>
        <#assign twitterIconPath = "/images/icon/rebrand-twitter.svg" />
        <@shareThisPage document "Twitter" twitterUrl twitterIconPath/>

        <#--  LinkedIn  -->
        <#assign linkedInUrl = "http://www.linkedin.com/shareArticle?mini=true&url=${getDocumentUrl()?url}&title=${document.title?url}&summary=${document.shortsummary?url}"/>
        <#assign linkedInIconPath = "/images/icon/rebrand-linkedin.svg" />
        <@shareThisPage document "LinkedIn" linkedInUrl linkedInIconPath/>
    </div>
</#macro>