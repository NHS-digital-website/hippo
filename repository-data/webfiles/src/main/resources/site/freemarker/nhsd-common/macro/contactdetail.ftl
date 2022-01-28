<#ftl output_format="HTML">

<#include "../../include/imports.ftl">

<#macro contactdetail contactdetails='' idsuffix='' name='' email='' phone='' title="Contact details" schemaOrg="">
  <#if contactdetails='' || (contactdetails?has_content && (contactdetails.emailaddress?has_content || contactdetails.phonenumber?has_content)) >

    <div class="nhsd-m-contact-us nhsd-!t-margin-bottom-6" ${schemaOrg?has_content?then('itemscope itemtype="https://schema.org/'+schemaOrg+'"', '')?no_esc}>
      <div class="nhsd-a-box nhsd-a-box--bg-light-blue-10">
        <div class="nhsd-m-contact-us__content">
          <p class="nhsd-t-heading-m">${title}</p>

          <#assign rendername=name /><#if contactdetails != '' && contactdetails.name?has_content><#assign rendername=contactdetails.name /></#if>
          <#assign renderemail=email /><#if contactdetails != '' && contactdetails.emailaddress?has_content><#assign renderemail=contactdetails.emailaddress /></#if>
          <#assign renderphone=phone /><#if contactdetails != '' && contactdetails.phonenumber?has_content><#assign renderphone=contactdetails.phonenumber /></#if>

          <#if rendername != ''>
            <p class="nhsd-t-heading-xs nhsd-!t-margin-top-1 nhsd-!t-margin-bottom-0 nhsd-t-word-break">${rendername}</p>
          </#if>

          <#if contactdetails != '' && contactdetails.purpose?has_content>
            <p class="nhsd-t-body nhsd-!t-margin-top-1 nhsd-!t-margin-bottom-0 nhsd-t-word-break">Purpose: ${contactdetails.purpose}</p>
          </#if>

          <#if contactdetails != '' && contactdetails.description?has_content>
            <p class="nhsd-t-body nhsd-!t-margin-top-1 nhsd-!t-margin-bottom-0 nhsd-t-word-break">Description of contact point: ${contactdetails.description}</p>
          </#if>

          <#if renderemail != ''>
            <p class="nhsd-t-body nhsd-!t-margin-top-1 nhsd-!t-margin-bottom-0">Email:
              <#if schemaOrg?has_content>
              <a class="nhsd-a-link nhsd-t-word-break" href="mailto:${renderemail}" itemprop="email">${renderemail}
              <#else>
              <a class="nhsd-a-link nhsd-t-word-break" href="mailto:${renderemail}">${renderemail}
              </#if>
              <span class="nhsd-t-sr-only"></span>
              </a>
            </p>
          </#if>

          <#if renderphone != ''>
            <p class="nhsd-t-body nhsd-!t-margin-top-1 nhsd-!t-margin-bottom-0">Phone:
              <#if schemaOrg?has_content>
              <a href="tel:${renderphone?replace(" ","")?replace("-","")}" itemprop="telephone" class="nhsd-a-link nhsd-t-word-break">${renderphone}
              <#else>
              <a href="tel:${renderphone?replace(" ","")?replace("-","")}" class="nhsd-a-link nhsd-t-word-break">${renderphone}
              </#if>
              <span class="nhsd-t-sr-only"></span>
              </a>
            </p>
          </#if>

          <#if contactdetails != '' && contactdetails.twitterHandle?has_content>
            <#assign twitterhandle = contactdetails.twitterHandle />
            <#if twitterhandle?substring(0, 1) == "@">
              <#assign twitterhandle = contactdetails.twitterHandle?substring(1) />
            </#if>
            <#assign twitterlink = "https://twitter.com/" + twitterhandle />

            <p class="nhsd-t-body nhsd-!t-margin-top-1 nhsd-!t-margin-bottom-0">Twitter handle:
              <a class="nhsd-a-link nhsd-t-word-break" href="${twitterlink}" onClick="logGoogleAnalyticsEvent('Link click',document.class.name,'${twitterlink}');" onKeyUp="return vjsu.onKeyUp(event)"  title="${twitterhandle}">@${twitterhandle}
                <span class="nhsd-t-sr-only"></span>
              </a>
            </p>
          </#if>

          <#if contactdetails != '' && contactdetails.webchatDescription?has_content>
            <p class="nhsd-t-body nhsd-!t-margin-top-1 nhsd-!t-margin-bottom-0 nhsd-t-word-break">Webchat description: ${contactdetails.webchatDescription}</p>
          </#if>

          <#if contactdetails != '' && contactdetails.webchatLink?has_content>
            <p class="nhsd-t-body nhsd-!t-margin-top-1 nhsd-!t-margin-bottom-0">Webchat link:
              <a class="nhsd-a-link nhsd-t-word-break" href="${contactdetails.webchatLink}">${contactdetails.webchatLink}
                <span class="nhsd-t-sr-only"></span>
              </a>
            </p>
          </#if>

          <#if contactdetails != '' && contactdetails.webformDescription?has_content>
            <p class="nhsd-t-body nhsd-!t-margin-top-1 nhsd-!t-margin-bottom-0 nhsd-t-word-break">Webform description: ${contactdetails.webformDescription}</p>
          </#if>

          <#if contactdetails != '' && contactdetails.webformLink?has_content>
            <p class="nhsd-t-body nhsd-!t-margin-top-1 nhsd-!t-margin-bottom-0">Webform link:
              <a class="nhsd-a-link nhsd-t-word-break" href="${contactdetails.webformLink}">${contactdetails.webformLink}
                <span class="nhsd-t-sr-only"></span>
              </a>
            </p>
          </#if>

        </div>
      </div>
    </div>
  </#if>
</#macro>
