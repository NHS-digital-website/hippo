<#ftl output_format="HTML">

<#macro quoteHeroContent options>
    <#assign quoteColourClass="nhsd-a-icon--col-blue">
    <#assign isTall=options.isTall>
    <#assign style="">
    <#if options.colour?has_content>
        <#if options.colour == "Blue grey">
            <#assign bgClass="nhsd-!t-bg-bright-blue-10-tint">
        <#elseif options.colour == "Black">
            <#assign bgClass="nhsd-!t-bg-black">
            <#assign textClass += " nhsd-o-hero--light-text nhsd-!t-col-white">
        <#elseif options.colour == "Dark Blue Multicolour">
            <#assign bgClass="nhsd-!t-bg-blue">
            <#assign quoteColourClass="nhsd-a-icon--col-yellow">
            <#assign style="fill:#fae100">
            <#assign textClass += "nhsd-o-hero--light-text nhsd-!t-col-white">
        <#elseif options.colour == "Mid blue">
            <#assign bgClass="nhsd-!t-bg-accessible-blue">
            <#assign textClass += " nhsd-o-hero--light-text nhsd-!t-col-white">
        <#elseif options.colour == "Light Blue" || options.colour == "blue">
            <#assign bgClass="nhsd-!t-bg-bright-blue-20-tint">
        <#elseif options.colour == "Yellow" || options.colour == "yellow">
            <#assign bgClass="nhsd-!t-bg-yellow-20-tint">
        </#if>
    </#if>

    <div itemscope itemtype="https://schema.org/Quotation">
        <figure>
            <p class="nhsd-t-body ${textClass}">${options.categoryInfo}
            <blockquote class="nhsd-t-heading-l ${textClass}" style="overflow: visible; -webkit-line-clamp: 30000;">
                    <span class="nhsd-a-icon nhsd-a-icon--size-xl ${quoteColourClass}">
                            <svg xmlns="http://www.w3.org/2000/svg"
                                 preserveAspectRatio="xMidYMid meet" aria-hidden="true"
                                 focusable="false" viewBox="0 0 16 16" width="100%"
                                 height="100%" style="${style}">
                            <path d="M7.5,16H0V7.5c0-4.4,3.3-6.4,6.4-6.4v3.2c-0.8,0-3.2,0.2-3.2,3.2v1.1h4.3C7.5,8.5,7.5,16,7.5,16z M16,16H8.5V7.5 c0-4.4,3.3-6.4,6.4-6.4v3.2c-0.8,0-3.2,0.2-3.2,3.2v1.1H16V16z"/>
                        </svg>
                    </span>
                <#if isTall?has_content>
                    ${options.quote}
                    <#else>
                        <div class="nhsd-t-heading-s nhsd-!t-margin-top-6 nhsd-!t-margin-bottom-0 ${textClass}" data-uipath="${uiPath}.summary">
                            ${options.quote}
                        </div>
                </#if>

            </blockquote>
            <figcaption class="nhsd-m-quote__meta">
                <#assign hasCatInfo = document.categoryInfo?has_content />
                <#assign hasName = document.name?has_content />
                <#assign hasRole = document.jobRole?has_content />
                <#assign hasOrganisation = document.organisation?has_content />

                <span itemprop="author" itemscope itemtype="https://schema.org/Person" class="${textClass}">
                        <#if hasName>
                            <span itemprop="name">${document.name}</span><#if hasRole || hasOrganisation>,</#if></#if>
                    <#if hasRole><span
                            itemprop="jobtitle">${document.jobRole}</span><#if hasOrganisation>,</#if></#if>
                    <#if hasOrganisation>
                        <span itemprop="worksfor" itemscope
                              itemtype="https://schema.org/Organization">
                                        <span itemprop="name">${document.organisation}</span>
                            </span>
                    </#if>
                    </span>
            </figcaption>
        </figure>
        </p>
    </div>
</#macro>
