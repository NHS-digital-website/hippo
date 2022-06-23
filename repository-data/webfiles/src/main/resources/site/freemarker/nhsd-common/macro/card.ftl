<#ftl output_format="HTML">

<#macro card cardProperties>
    <#local bgColor = cardProperties.background?has_content?then(cardProperties.background, "white") />
    <#if bgColor == "white">
        <#local borderColor = cardProperties.border?has_content?then(cardProperties.border, "grey") />
    </#if>

    <#assign cardClass = ""/>
    <#if cardProperties.featured?? && cardProperties.featured && cardProperties.image?has_content>
        <#assign cardClass = "nhsd-m-card--image-position-adjacent nhsd-m-card--full-height"/>
    </#if>
    <#if cardProperties.authorsInfo?has_content && cardProperties.authorsInfo?size gt 0>
        <#assign cardClass += " nhsd-m-card--author"/>
    </#if>

    <#if cardProperties.contentType?has_content>
        <#assign trackingEvent = 'onclick="logGoogleAnalyticsEvent(\'Link click\', \'${cardProperties.contentType}\', \'${cardProperties.link}\')" onkeyup="return vjsu.onKeyUp(event)"' />
    </#if>

    <article class="nhsd-m-card ${cardClass}">
        <#if cardProperties.publishedDate?has_content>
   		    <@fmt.formatDate value=cardProperties.publishedDate.time type="Date" pattern="EEEE d MMMM yyyy" timeZone="${getTimeZone()}" var="publishedDate" />
            <@fmt.formatDate value=cardProperties.lastModified type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" var="lastModifiedDate" />
        </#if>

        <#if cardProperties.link?has_content>
            <a href="${cardProperties.link}" class="nhsd-a-box-link nhsd-a-box-link--focus-orange" ${cardProperties.title?has_content?then("aria-label='About NHS Digital'", "")?no_esc}>
        </#if>
            <div class="nhsd-a-box nhsd-a-box--bg-${bgColor} ${borderColor?has_content?then("nhsd-a-box--border-" + borderColor, "")}">
                <#if cardProperties.image?has_content>
                    <div class="nhsd-m-card__image_container">
                        <figure class="nhsd-a-image ${cardProperties.imageClass?has_content?then(cardProperties.imageClass, '')}">
                            <picture class="nhsd-a-image__picture">
                                <@hst.link hippobean=cardProperties.image.newsPostImageLarge fullyQualified=true var="leadImage" />
                                <img src="${leadImage}" alt="${cardProperties.altText}" />
                            </picture>
                        </figure>
                    </div>
                </#if>
                <div class="nhsd-m-card__content_container">
                    <div class="nhsd-m-card__content-box">
                        <#if cardProperties.tags?? && cardProperties.tags?size gt 0>
                            <div class="nhsd-m-card__tag-list">
                                <#list cardProperties.tags as tag>
                                	<#if tag.colour?? && tag.colour?has_content>
                                		<#assign tagColour = tag.colour/>
                                	<#else>
                                		<#assign tagColour = "nhsd-a-tag--bg-dark-grey"/>
                                	</#if>
                                    <#if tag.text?has_content>
                                        <span class="nhsd-a-tag ${tagColour}">${tag.text}</span>
                                    </#if>
                                </#list>
                            </div>
                        </#if>

                        <#if publishedDate?has_content>
                            <h2 class="nhsd-m-card__date">${publishedDate} (updated ${lastModifiedDate})</h2>
                        </#if>
                        <#if cardProperties.title?has_content>
                            <h2 class="nhsd-t-heading-s">${cardProperties.title}</h2>
                        </#if>
                        <#if cardProperties.shortsummary?has_content>
                            <p class="nhsd-t-body-s">${cardProperties.shortsummary}</p>
                        </#if>

                        <#if cardProperties.bullets?has_content>
                            <ul class="nhsd-t-list nhsd-t-list--bullet nhsd-!t-margin-bottom-0" style="margin-left: 15px;">
                        </#if>
                        <#if cardProperties.content?has_content>
                            <#if cardProperties.content?is_hash>
                                <#if cardProperties.bullets?has_content>
                                    <li>
                                </#if>
                                <@hst.html hippohtml=cardProperties.content contentRewriter=brContentRewriter />
                                <#if cardProperties.bullets?has_content>
                                    </li>
                                </#if>
                            <#else>
                                <#if cardProperties.bullets?has_content>
                                    <li>
                                </#if>
                                <p class="nhsd-t-body-s">${cardProperties.content}</p>
                                <#if cardProperties.bullets?has_content>
                                    </li>
                                </#if>
                            </#if>
                        </#if>
                        <#if cardProperties.bullets?has_content>
                            </ul>
                        </#if>

                        <#if cardProperties.threat?has_content>
                        	<div class="nhsd-t-body-s" style="margin-top: 1.1111111111rem;">
                        		<span class="nhsd-a-tag--bg-light-grey">
                        			${cardProperties.threat}
                        		</span>
                        	</div>
                        </#if>
                    </div>

                    <#if cardProperties.button?? && cardProperties.button?has_content>
                        <div class="nhsd-m-card__button-box">
                            <span class="nhsd-a-button">
                                <span class="nhsd-a-button__label">${cardProperties.button}</span>
                            </span>
                        </div>
                    <#elseif cardProperties.link?has_content>
                        <div class="nhsd-m-card__button-box">
                            <#if cardProperties.link?has_content>
                                <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-icon--size-s nhsd-a-icon--col-black">
                                  <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                    <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                                  </svg>
                                  </span>
                            </#if>
                        </div>
                    <#else>
                        <div class="nhsd-m-card__button-box"></div>
                    </#if>

                    <div class="nhsd-m-card__icon-container">
                        <#if cardProperties.icon?has_content>
                            <#if cardProperties.icon == "link">
                                <span class="nhsd-a-icon nhsd-a-icon--size-xxxl nhsd-m-card__icon">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16">
                                        <path d="M8,16l-6.9-4V4L8,0l6.9,4v8L8,16z M2,11.5L8,15l6-3.5v-7L8,1L2,4.5V11.5z"/>
                                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16" width="62%" height="62%" x="19%" y="19%">
                                            <path d="M11.4,9.1c0,2.5-2,3.9-3.6,5.5C7,15.5,5.8,16,4.6,16C2,16,0,14,0,11.4c0-2.1,1.2-3.1,2.6-4.5l1.6,1.6 c-0.8,0.8-2,1.6-2,2.9c0,1.3,1,2.3,2.3,2.3c0.6,0,1.2-0.2,1.6-0.7l2.3-2.3c0.4-0.4,0.7-1,0.7-1.6c0-0.9-0.5-1.7-1.3-2.1l1-2.1 C10.4,5.7,11.4,7.4,11.4,9.1z M16,4.6c0,2.1-1.2,3.1-2.6,4.5l-1.6-1.6c0.8-0.8,2-1.6,2-2.9c0-1.3-1-2.3-2.3-2.3 c-0.6,0-1.2,0.2-1.6,0.7L7.5,5.2c-0.4,0.4-0.7,1-0.7,1.6c0,0.9,0.5,1.7,1.3,2.1l-1,2.1c-1.6-0.7-2.7-2.4-2.7-4.2 c0-1.2,0.5-2.4,1.3-3.2C7.6,2,8.9,0,11.4,0C14,0,16,2,16,4.6z"></path>
                                        </svg>
                                    </svg>
                                </span>
                            </#if>
                        </#if>
                    </div>
                </div>
            </div>
        <#if cardProperties.link?has_content>
            </a>
        </#if>
        <#if cardProperties.authorsInfo?has_content && cardProperties.authorsInfo?size gt 0>
            <@authorSection cardProperties.authorsInfo />
        </#if>
    </article>
</#macro>

<#macro authorSection authors>
    <div class="nhsd-m-card__author">
        <#if authors?size == 1>
            <#assign author = authors[0] />
            <div class="nhsd-m-author">
                <#if author.image?has_content>
                    <@hst.link hippobean=author.image.authorPhoto2x fullyQualified=true var="authorImage" />
                    <div class="nhsd-a-avatar" title="${author.name}" aria-label="${author.name}">
                        <figure class="nhsd-a-image nhsd-a-image--cover" aria-hidden="true">
                            <picture class="nhsd-a-image__picture">
                                <img itemprop="image"
                                     class="bloghub__item__content__author__img"
                                     src="${authorImage}"
                                     alt="${author.name}"/>
                            </picture>
                        </figure>
                    </div>
                </#if>

                <div class="nhsd-m-author__details">
                    <#if author.link?has_content>
                        <a href="${author.link}" class="nhsd-a-link nhsd-t-body-s">${author.name}</a>
                    <#elseif author.name?has_content>
                        <span class="nhsd-t-heading-xs nhsd-!t-margin-0 nhsd-!t-col-black">${author.name}</span>
                    </#if>
                    <p class="nhsd-t-body-s nhsd-!t-margin-top-2 nhsd-!t-margin-bottom-0 nhsd-!t-col-black">
                        <#if author.role?has_content>${author.role}</#if><#if author.role?has_content>, ${author.org}</#if>
                    </p>
                </div>
            </div>
        <#elseif authors?size gt 1>
            <div class="nhsd-m-avatar-list nhsd-!t-margin-right-2">
                <#list authors as author>
                    <#if author.image?has_content>
                        <@hst.link hippobean=author.image.authorPhoto2x fullyQualified=true var="authorImage" />
                        <a href="${author.link}" class="nhsd-a-avatar" title="${author.name}" aria-label="${author.name}">
                            <figure class="nhsd-a-image nhsd-a-image--cover" aria-hidden="true">
                                <picture class="nhsd-a-image__picture">
                                    <img itemprop="image"
                                         class="bloghub__item__content__author__img"
                                         src="${authorImage}"
                                         alt="${author.name}"/>
                                </picture>
                            </figure>
                        </a>
                    <#else>
                        <a href="${author.link}" class="nhsd-a-avatar nhsd-a-avatar--initials" title="${author.name}" aria-label="${author.name}">${author.initials}</a>
                    </#if>
                </#list>
            </div>
        </#if>
    </div>
</#macro>
