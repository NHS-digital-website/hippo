<#ftl output_format="HTML">
<#macro googleTags documentBean pageSubject>
    <script>
        window.dataLayer.push({'data-doc-type': '${documentBean.docType}'});
        window.dataLayer.push({'data-creation-date': '${documentBean.creationDate?datetime?iso_utc}'});
        window.dataLayer.push({'data-last-modified-date': '${documentBean.lastModified?datetime?iso_utc}'});
        window.dataLayer.push({'data-author': '${documentBean.createdBy}'});
        window.dataLayer.push({'data-topics': '${(documentBean.keys??)?then(documentBean.keys?join(","), "")}'});
        window.dataLayer.push({'data-subject': '${pageSubject}'});
    </script>
</#macro>
