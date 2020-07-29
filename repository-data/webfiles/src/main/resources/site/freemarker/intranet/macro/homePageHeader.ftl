<#ftl output_format="HTML">

<#macro homePageHeader>

    <#if username?has_content>
        <#assign userName = username/>
    <#else>
        <#assign userName = "" />
    </#if>

    <div class="intra-home-header">
        <div class="intra-home-header__inner">
            <div class="intra-home-header__inner-contents">
                <div class="intra-home-header__greeting">
                    <h1><span class="intra-home-header__greeting-welcome">Welcome</span> <span>${userName}</span></h1>
                </div>
            </div>
        </div>
    </div>

</#macro>
