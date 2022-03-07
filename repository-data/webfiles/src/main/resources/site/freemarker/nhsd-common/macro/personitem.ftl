<#ftl output_format="HTML">

<#macro personitem author>
    <#if author?? && author?has_content>
        <div class="nhsd-m-card nhsd-m-card--full-height nhsd-m-card--profile">
            <a href="<@hst.link hippobean=author/>" class="nhsd-a-box-link nhsd-a-box-link--focus-orange" aria-label="${author.title}">
                <div class="nhsd-a-box nhsd-a-box--border-grey">
                    <div class="nhsd-m-card__content_container">
                        <div class="nhsd-m-card__content-box">
                            <span class="nhsd-t-heading-s nhsd-!t-margin-0">${author.title}</span>
                            <#if author.roles?has_content && author.roles.primaryroles?has_content>
                                <span class="nhsd-!t-col-black nhsd-!t-margin-bottom-2">${author.roles.firstprimaryrole}</span>
                            </#if>

                            <ul class="nhsd-t-body-s">
                                <#if author.roles?has_content>
                                    <#if author.roles.primaryroleorg?has_content>
                                        <li>${author.roles.primaryroleorg}</li>
                                    </#if>

                                    <#if author.roles.contactdetails?has_content>
                                        <#if author.roles.contactdetails.phonenumber?has_content>
                                            <li>${author.roles.contactdetails.phonenumber}</li>
                                        </#if>
                                        <#if author.roles.contactdetails.emailaddress?has_content>
                                            <li>${author.roles.contactdetails.emailaddress}</li>
                                        </#if>
                                    </#if>
                                </#if>
                            </ul>
                        </div>
                        <div class="nhsd-m-card__button-box">
                            <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-icon--size-s nhsd-a-icon--col-black">
                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16"><path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/></svg>
                            </span>
                        </div>
                    </div>
                </div>
                <div class="nhsd-m-card__avatar">
                    <div class="nhsd-a-avatar nhsd-a-avatar--large" title="" aria-label="">
                        <figure class="nhsd-a-image nhsd-a-image--cover" aria-hidden="true">
                            <picture class="nhsd-a-image__picture " >
                                <#if author.personimages?? && author.personimages?has_content && author.personimages.picture?has_content>
                                <@hst.link hippobean=author.personimages.picture.authorPhotoLarge fullyQualified=true var="authorPicture" />
                                <@hst.link hippobean=author.personimages.picture.authorPhotoLarge2x fullyQualified=true var="authorPicture2x" />
                                <img srcset="${authorPicture} 200px, ${authorPicture2x} 400px"
                                     sizes="200px"
                                     src="${authorPicture}" alt="${author.title}"/>
                                <#else>
                                    <img src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="${author.title}"/>
                                </#if>
                            </picture>
                        </figure>
                    </div>
                </div>
            </a>
        </div>
    </#if>
</#macro>
