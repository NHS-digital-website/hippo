<#ftl output_format="HTML">

<#--
 nhsdDropdown contains code for the dropdown list section a dropdown component.
 The input which which triggers and controls a dropdown is nested into the component, along with dropdown content.
 This allows buttons, text inputs and other components to be used as dropdown triggers.
 Dropdown
 -->

<#assign nhsdDropdownCount = nhsdDropdownCount?has_content?then(nhsdDropdownCount, 0) />

<#macro nhsdDropdown options={}>
    <#-- Ensure there's a unique ID for this dropdown -->
    <#assign nhsdDropdownCount++ />
    <#assign dropdownId = options.id?has_content?then(options.id, 'dropdown-'+ nhsdDropdownCount) />

    <div class="nhsd-o-dropdown nhsd-o-dropdown--full-width" id="${dropdownId}">
        <#nested />
    </div>
</#macro>

<#macro nhsdDropdownInput>
    <div class="nhsd-o-dropdown__input">
        <#nested />
    </div>
</#macro>

<#macro nhsdDropdownContent>
    <div class="nhsd-o-dropdown__dropdown">
        <#nested />
    </div>
</#macro>