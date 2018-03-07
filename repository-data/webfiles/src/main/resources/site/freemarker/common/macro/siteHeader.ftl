<#ftl output_format="HTML">
<#macro siteHeader enableSearch>
<header class="site-header" role="banner">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="grid-row">
            <div class="column column--reset">
                <div class="grid-wrapper grid-wrapper--collapse">
                    <div class="grid-row">
                        <div class="column column--18-75 column--reset">
                            <img src="<@hst.webfile path="/images/nhs-logo.png"/>" alt="NHS Digital logo" class="site-header__logo">
                        </div>

                        <#assign wrapperClassName = "site-header__search-wrapper site-header__search-wrapper--collapse" />
                        <#if enableSearch>
                        <#assign wrapperClassName = "site-header__search-wrapper" />
                        </#if>

                        <div class="column column--81-25  column--reset">
                            <div class="${wrapperClassName}">
                                <#if enableSearch>
                                <#include "../search-strip.ftl">
                                </#if>
                            </div>
                            <nav class="site-header__nav">
                                <ol class="list list--reset list--inline">
                                    <li><a href="/data-and-information">Data and information</a></li>
                                    <li><a href="/services">Systems and services</a></li>
                                    <li><a href="/news-and-events">News and events</a></li>
                                    <li><a href="/about-nhs-digital">About NHS Digital</a></li>
                                </ol>
                            </nav>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</header>
</#macro>