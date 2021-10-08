<#ftl output_format="HTML">

<#include "../../include/imports.ftl">
<#include "./responsiveImage.ftl">

<#macro personitem person>
    <#if person?has_content>
        <#if person.class.name?starts_with('uk.nhs.digital.website.beans.Person')>
            <#assign personOptions = personBeanOptions(person) />
        <#elseif person.class.name?starts_with('uk.nhs.digital.intranet.model.Person')>
            <#assign personOptions = personModelOptions(person) />
        </#if>

        <div class="nhsd-m-card nhsd-m-card--full-height nhsd-m-card--profile">
            <a href="${personOptions.link}" class="nhsd-a-box-link nhsd-a-box-link--focus-orange" aria-label="${personOptions.name}">
                <div class="nhsd-a-box nhsd-a-box--border-grey">
                    <div class="nhsd-m-card__content_container">
                        <div class="nhsd-m-card__content-box">
                            <span class="nhsd-t-heading-s nhsd-!t-margin-0">${personOptions.name}</span>
                            <#if personOptions.role?has_content>
                                <span class="nhsd-!t-col-black nhsd-!t-margin-bottom-2">${personOptions.role}</span>
                            </#if>

                            <ul class="nhsd-t-body-s">
                                <#if personOptions.org?has_content>
                                    <li>${personOptions.org}</li>
                                </#if>

                                <#if personOptions.phone?has_content>
                                    <li>${personOptions.phone}</li>
                                </#if>
                                <#if personOptions.email?has_content>
                                    <li>${personOptions.email}</li>
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
                            <picture class="nhsd-a-image__picture">
                                <#if personOptions.image?? && personOptions.image?has_content>
                                    <img src="${personOptions.image}" alt="${personOptions.name}"/>
                                <#else>
                                    <img src="data:image/svg+xml;base64,PHN2ZyB2ZXJzaW9uPSIxLjIiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgdmlld0JveD0iMCAwIDI1NiAyNTYiIHdpZHRoPSIyNTYiIGhlaWdodD0iMjU2Ij4KPHJlY3Qgd2lkdGg9IjEwMCUiIGhlaWdodD0iMTAwJSIgZmlsbD0iIzAwNWJiYiIgLz4KPHBhdGggZmlsbD0iIzk5YmRlNCIgZD0ibTM0LjYgMjU2YzAtNjUuMyA0MS44LTExOC4zIDkzLjQtMTE4LjNjNTEuNiAwIDkzLjQgNTMgOTMuNCAxMTguM3oiIC8+CjxwYXRoIGZpbGw9IiM5OWJkZTQiIGQ9Im0xMjggMTI5LjljLTI4LjUgMC01MS41LTIxLjktNTEuNS00OWMwLTI3LjEgMjMtNDkgNTEuNS00OWMyOC41IDAgNTEuNSAyMS45IDUxLjUgNDljMCAyNy4xLTIzIDQ5LTUxLjUgNDl6IiAvPgo8L3N2Zz4=" alt="${personOptions.name}"/>
                                </#if>
                            </picture>
                        </figure>
                    </div>
                </div>
            </a>
        </div>
    </#if>
</#macro>

<#function personBeanOptions personBean>
    <#assign role = "" />
    <#assign org = "" />
    <#assign phone = "" />
    <#assign email = "" />

    <#if personBean.roles?has_content && personBean.roles.primaryroles?has_content>
        <#assign role = personBean.roles.firstprimaryrole />
    </#if>

    <#if personBean.roles?has_content && personBean.roles.primaryroleorg?has_content>
        <#assign org = personBean.roles.firstprimaryrole />
    </#if>

    <#if personBean.roles?has_content && personBean.roles.contactdetails?has_content>
        <#if personBean.roles.contactdetails.phonenumber?has_content>
            <#assign phone = personBean.roles.contactdetails.phonenumber />
        </#if>
        <#if personBean.roles.contactdetails.emailaddress?has_content>
            <#assign email = personBean.roles.contactdetails.emailaddress />
        </#if>
    </#if>

    <@hst.link hippobean=personBean var="authorLink" />

    <#assign personOptions = {
        'name': personBean.title,
        'link': authorLink,
        'role': role,
        'org': org,
        'phone': phone,
        'email': email
    }/>

    <#if personBean.personimages?has_content && personBean.personimages.picture?has_content>
        <@hst.link hippobean=personBean.personimages.picture.authorPhoto2x fullyQualified=true var="authorPicture" />
        <#assign personOptions += { 'image': authorPicture } />
    </#if>

    <#return personOptions />
</#function>

<#function personModelOptions person>
    <@hst.link siteMapItemRefId="person" var="personLink" />

    <#assign personOptions = {
        'name': person.displayName,
        'link': personLink + '/' + person.id,
        'role': person.jobRole?has_content?then(person.jobRole, ''),
        'org': person.department?has_content?then(person.department, ''),
        'phone': person.phoneNumber?has_content?then(person.phoneNumber, ''),
        'email': person.email?has_content?then(person.email, '')
    }/>

    <#if person.photo?has_content>
        <#assign personOptions += { 'image': 'data:image/jpeg;base64,' + person.photo } />
    </#if>

    <#return personOptions />
</#function>
