<#ftl output_format="HTML">

<#include "../../include/imports.ftl">

  <#macro contactdetail contactdetails='' idsuffix='' name=''>
    <#if contactdetails?has_content && (contactdetails.emailaddress?has_content || contactdetails.phonenumber?has_content) >
      <div id="contactdetail-${slugify(idsuffix)}" class="contactdetail--div article-section">
        <h2>Contact details</h2> 

        <div class="contactdetail-box">
          <div class="contactdetail-item-bold"><span>${name}</span></div>
          <#if contactdetails.emailaddress?has_content>
            <div class="contactdetail-item">Telephone: <span itemprop="email" class="contactdetail-item-bold">${contactdetails.emailaddress}</span></div>
          </#if>
          <#if contactdetails.phonenumber?has_content>
            <div class="contactdetail-item">Email: <span itemprop="telephone" class=contactdetail-item-bold>${contactdetails.phonenumber}</span></div>
          </#if>
        </div>
      </div>
    </#if>
  </#macro>
