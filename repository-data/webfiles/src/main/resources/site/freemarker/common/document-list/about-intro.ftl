<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<@hst.setBundle basename="homepage.website.labels"/>
<@fmt.message key="about-us.title" var="aboutUsSectionTitle"/>
<@fmt.message key="about-us.text" var="aboutUsSectionText"/>

<div class="article-section__header">
    <h2>${aboutUsSectionTitle}</h2>
    <p>${aboutUsSectionText}</p>
</div>
