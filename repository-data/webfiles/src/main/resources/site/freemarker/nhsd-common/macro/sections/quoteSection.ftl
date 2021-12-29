<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Quote" -->

<#macro quoteSection section>
    <div class="nhsd-m-quote nhsd-!t-margin-bottom-6">
        <div itemscope itemtype="https://schema.org/Quotation" class="nhsd-a-box nhsd-a-box--bg-light-grey">
            <figure>
                <blockquote class="nhsd-t-heading-l">
                    <span class="nhsd-a-icon nhsd-a-icon--size-xl nhsd-a-icon--col-blue">
                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                            <path d="M7.5,16H0V7.5c0-4.4,3.3-6.4,6.4-6.4v3.2c-0.8,0-3.2,0.2-3.2,3.2v1.1h4.3C7.5,8.5,7.5,16,7.5,16z M16,16H8.5V7.5 c0-4.4,3.3-6.4,6.4-6.4v3.2c-0.8,0-3.2,0.2-3.2,3.2v1.1H16V16z"/>
                        </svg>
                    </span>
                    <@hst.html hippohtml=section.quote contentRewriter=stripTagsWithLinksContentRewriter/>
                </blockquote>

                <figcaption class="nhsd-m-quote__meta">
                    <#assign hasPerson = section.person?has_content />
                    <#assign hasRole = section.role?has_content />
                    <#assign hasOrganisation = section.organisation?has_content />
          
                    <span itemprop="author" itemscope itemtype="https://schema.org/Person">
                        <#if hasPerson><span itemprop="name">${section.person}</span><#if hasRole || hasOrganisation>,</#if></#if>
                        <#if hasRole><span itemprop="jobtitle">${section.role}</span><#if hasOrganisation>,</#if></#if>
                        <#if hasOrganisation>
                            <span itemprop="worksfor" itemscope itemtype="https://schema.org/Organization">
                                <span itemprop="name">${section.organisation}</span>
                            </span>
                        </#if>
                    </span>
                </figcaption>
            </figure>
        </div>
    </div>
</#macro>
