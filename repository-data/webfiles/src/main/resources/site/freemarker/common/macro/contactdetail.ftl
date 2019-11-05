<#ftl output_format="HTML">

<#include "../../include/imports.ftl">

  <#macro contactdetail contactdetails='' idsuffix='' name='' email='' phone='' title="Contact details" isSchemaOrg=true>
    <#if contactdetails='' || (contactdetails?has_content && (contactdetails.emailaddress?has_content || contactdetails.phonenumber?has_content)) >
      <div id="contactdetail-${slugify(idsuffix)}" class="contactdetail--div article-section">
        <h2>${title}</h2> 

        <#assign rendername=name /><#if contactdetails != '' && contactdetails.name?has_content><#assign rendername=contactdetails.name /></#if>
        <#assign renderemail=email /><#if contactdetails != '' && contactdetails.emailaddress?has_content><#assign renderemail=contactdetails.emailaddress /></#if>
        <#assign renderphone=phone /><#if contactdetails != '' && contactdetails.phonenumber?has_content><#assign renderphone=contactdetails.phonenumber /></#if>

        <div class="contactdetail-box">
          <#if rendername != ''>
            <div class="contactdetail-item-bold"><span>${rendername}</span></div>
          </#if>
          <#if renderemail != ''>
            <div class="contactdetail-item">Email: 
              <#if isSchemaOrg>
                <a href="mailto:${renderemail}" itemprop="email" class="contactdetail-item">${renderemail}</a>
              <#else>
                <a href="mailto:${renderemail}" class="contactdetail-item">${renderemail}</a>
              </#if>
            </div>
          </#if>
          <#if renderphone != ''>
            <div class="contactdetail-item">Phone: 
              <#if isSchemaOrg>
                <a href="tel:${renderphone}" itemprop="telephone" class=contactdetail-item>${renderphone}</a>
              <#else>
                <a href="tel:${renderphone}" class=contactdetail-item>${renderphone}</a>
              </#if>
            </div>
          </#if>
        </div>
      </div>
    </#if>
  </#macro>
