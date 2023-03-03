<#ftl output_format="plainText">

<#assign html>
<button class="nhsd-a-button" type="button">
    <span class="nhsd-a-button__label">Take primary action</span>
</button>
</#assign>

<#assign nunjucks>
{% from "nhsd/njk/macros/atoms.njk" import nhsd_a_button %}

{{ nhsd_a_button({
    label: 'Take primary action',
}) }}
</#assign>
