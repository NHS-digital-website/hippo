<#ftl output_format="HTML">

<#macro downloadBlock doc itemprop="">

    <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, doc.title) />
    <a href="<@hst.link hippobean=doc />"
       class="block-link"
       onClick="${onClickMethodCall}"
       onKeyUp="return vjsu.onKeyUp(event)" ${itemprop}>
        <div class="block-link__header">
            <span class="icon icon--html icon--download" aria-hidden="true"></span>
        </div>
        <div class="block-link__body">
            <span class="block-link__title">${doc.title}</span>
            <#if doc.shortsummary?has_content>
              <p class="cta__text">${doc.shortsummary}</p>
            </#if>
        </div>
    </a>
</#macro>
