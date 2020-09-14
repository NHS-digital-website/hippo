<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<@hst.setBundle basename="homepage.intranet.labels"/>


<@googleTags documentBean=document pageSubject=(document.relateddocument??)?then(document.relateddocument.title, "")/>
<#-- Add meta tags -->
<@metaTags></@metaTags>

<article class="article article--intranet-home">
    <h2><@fmt.message key="about-us.latestSocialPostsLabel"/></h2>
</article>
