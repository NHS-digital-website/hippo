<#ftl output_format="HTML">

<#macro iconList section>

<#-- TODO
    CREATE FRONTEND DISPLAY.
    SYNTAX FOR ACCESSING THE OBJECT PROPERTIES BELOW
-->


    <#if section.title?has_content>
        <h3>${section.title}</h3>
    </#if>

    <#list section.iconList as iconListItem>
        HEADING : ${iconListItem.heading}

        DESCRIPTION : <div><@hst.html hippohtml=iconListItem.description contentRewriter=gaContentRewriter /></div>

        ICON:   <@hst.link hippobean=iconListItem.image.original fullyQualified=true var="iconImage" />
                <img src="${iconImage}" />

    </#list>


</#macro>
