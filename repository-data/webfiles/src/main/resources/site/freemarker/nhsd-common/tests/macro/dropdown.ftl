<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">
<#include "../../macro/toolkit/organisms/dropdown.ftl">

<div class="nhsd-t-grid nhsd-!t-margin-top-6 nhsd-!t-margin-bottom-6">
    <div class="nhsd-t-row">
        <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4 nhsd-!t-margin-top-6" data-variant="basic" style="height: 400px">
            <@nhsdDropdown>
                <@nhsdDropdownInput>
                    <button class="nhsd-a-button" type="button" data-dropdown-toggle aria-expanded="false" aria-owns="button-menu1-dropdown-list">
                        <span class="nhsd-a-button__label">Dropdown Menu</span>
                        <span class="nhsd-a-icon nhsd-a-icon--size-s"><svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16"><path d="M8,12L1,5.5L2.5,4L8,9.2L13.5,4L15,5.5L8,12z"/></svg></span>
                    </button>
                </@nhsdDropdownInput>
                <@nhsdDropdownContent>
                    <ul role="listbox">
                        <li role="none" data-dropdown-toggle><a href="#" role="option">Leeds General Infirmary (D6A2L)</a></li>
                        <li role="none" data-dropdown-toggle><a href="#" role="option">Leeds West CCG HQ (03CAR)</a></li>
                        <li role="none" data-dropdown-toggle><a href="#" role="option">Leeds Community and Mental Health Unit (BP4)</a></li>
                        <li role="none" data-dropdown-toggle><a href="#" role="option">Leeds Community Dental Services (V14366)</a></li>
                    </ul>
                </@nhsdDropdownContent>
            </@nhsdDropdown>
        </div>
    </div>
    <div class="nhsd-t-row">
        <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4 nhsd-!t-margin-top-6" data-variant="right aligned" style="height: 400px">
            <@nhsdDropdown>
                <@nhsdDropdownInput>
                    <div class="nhsd-!t-text-align-right">
                        <button class="nhsd-a-button" type="button" data-dropdown-toggle aria-expanded="false" aria-owns="button-menu1-dropdown-list">
                            <span class="nhsd-a-button__label">Dropdown Menu</span>
                            <span class="nhsd-a-icon nhsd-a-icon--size-s"><svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16"><path d="M8,12L1,5.5L2.5,4L8,9.2L13.5,4L15,5.5L8,12z"/></svg></span>
                        </button>
                    </div>
                </@nhsdDropdownInput>
                <@nhsdDropdownContent>
                    <ul role="listbox">
                        <li role="none" data-dropdown-toggle><a href="#" role="option" data-dropdown-toggle>Leeds General Infirmary (D6A2L)</a></li>
                        <li role="none" data-dropdown-toggle><a href="#" role="option" data-dropdown-toggle>Leeds West CCG HQ (03CAR)</a></li>
                        <li role="none" data-dropdown-toggle><a href="#" role="option" data-dropdown-toggle>Leeds Community and Mental Health Unit (BP4)</a></li>
                        <li role="none" data-dropdown-toggle><a href="#" role="option" data-dropdown-toggle>Leeds Community Dental Services (V14366)</a></li>
                    </ul>
                </@nhsdDropdownContent>
            </@nhsdDropdown>
        </div>
    </div>
    <div class="nhsd-t-row">
        <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4 nhsd-!t-margin-top-6" data-variant="search" style="height: 400px">
            <#assign dropdownId = 'search-dropdown' />
            <@nhsdDropdown { 'id': dropdownId }>
                <@nhsdDropdownInput>
                    <div class="nhsd-m-search-bar nhsd-m-search-bar__small nhsd-m-search-bar__full-width">
                        <label for="${dropdownId}-input">Search</label>
                        <div class="nhsd-t-form-control">
                            <input class="nhsd-t-form-input" type="text"
                                   id="${dropdownId}-input"
                                   name="query"
                                   autocomplete="off"
                                   placeholder="Search countries..."
                                   aria-label="Keywords"
                                   role="combobox"
                                   aria-expanded="false"
                                   aria-autocomplete="list"
                                   aria-owns="${dropdownId}-dropdown"
                            />
                            <span class="nhsd-t-form-control__button">
                            <button class="nhsd-a-button nhsd-a-button--circle-condensed nhsd-a-button--transparent" type="submit" aria-label="Perform search">
                                <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
                                        <path d="M7,10.9c-2.1,0-3.9-1.7-3.9-3.9c0-2.1,1.7-3.9,3.9-3.9c2.1,0,3.9,1.7,3.9,3.9C10.9,9.2,9.2,10.9,7,10.9zM13.4,14.8l1.4-1.4l-3-3c0.7-1,1.1-2.1,1.1-3.4c0-3.2-2.6-5.8-5.8-5.8C3.8,1.2,1.2,3.8,1.2,7c0,3.2,2.6,5.8,5.8,5.8c1.3,0,2.4-0.4,3.4-1.1L13.4,14.8z"/>
                                    </svg>
                                </span>
                            </button>
                        </span>
                        </div>
                    </div>
                </@nhsdDropdownInput>
                <@nhsdDropdownContent/>
            </@nhsdDropdown>
        </div>
    </div>
    <div class="nhsd-t-row">
        <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4 nhsd-!t-margin-top-6" data-variant="multiselect" style="height: 400px">
            <#assign dropdownId = 'multiselect-dropdown' />
            <@nhsdDropdown { 'id': dropdownId }>
                <@nhsdDropdownInput>
                    <div class="nhsd-m-search-bar nhsd-m-search-bar__small nhsd-m-search-bar__full-width">
                        <label for="${dropdownId}-input">Search</label>
                        <div class="nhsd-t-form-control">
                            <input class="nhsd-t-form-input" type="text"
                                   id="${dropdownId}-input"
                                   name="query"
                                   autocomplete="off"
                                   placeholder="What are you looking for today?"
                                   aria-label="Keywords"
                                   role="combobox"
                                   aria-expanded="false"
                                   aria-autocomplete="list"
                                   aria-owns="${dropdownId}-dropdown"
                            />
                            <span class="nhsd-t-form-control__button">
                            <button class="nhsd-a-button nhsd-a-button--circle-condensed nhsd-a-button--transparent" type="submit" aria-label="Perform search">
                                <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
                                        <path d="M7,10.9c-2.1,0-3.9-1.7-3.9-3.9c0-2.1,1.7-3.9,3.9-3.9c2.1,0,3.9,1.7,3.9,3.9C10.9,9.2,9.2,10.9,7,10.9zM13.4,14.8l1.4-1.4l-3-3c0.7-1,1.1-2.1,1.1-3.4c0-3.2-2.6-5.8-5.8-5.8C3.8,1.2,1.2,3.8,1.2,7c0,3.2,2.6,5.8,5.8,5.8c1.3,0,2.4-0.4,3.4-1.1L13.4,14.8z"/>
                                    </svg>
                                </span>
                            </button>
                        </span>
                        </div>
                    </div>
                </@nhsdDropdownInput>
                <@nhsdDropdownContent/>
            </@nhsdDropdown>
        </div>
    </div>
    <div class="nhsd-t-row">
        <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4 nhsd-!t-margin-top-6" data-variant="custom" style="height: 400px">
            <#assign dropdownId = 'custom-dropdown' />
            <@nhsdDropdown { 'id': dropdownId }>
                <@nhsdDropdownInput>
                    <div class="nhsd-m-search-bar nhsd-m-search-bar__small nhsd-m-search-bar__full-width">
                        <label for="${dropdownId}-input">Search</label>
                        <div class="nhsd-t-form-control">
                            <input class="nhsd-t-form-input" type="text"
                                   id="${dropdownId}-input"
                                   name="query"
                                   autocomplete="off"
                                   placeholder="What are you looking for today?"
                                   aria-label="Keywords"
                                   role="combobox"
                                   aria-expanded="false"
                                   aria-autocomplete="list"
                                   aria-owns="${dropdownId}-dropdown"
                            />
                            <span class="nhsd-t-form-control__button">
                            <button class="nhsd-a-button nhsd-a-button--circle-condensed nhsd-a-button--transparent" type="submit" aria-label="Perform search">
                                <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
                                        <path d="M7,10.9c-2.1,0-3.9-1.7-3.9-3.9c0-2.1,1.7-3.9,3.9-3.9c2.1,0,3.9,1.7,3.9,3.9C10.9,9.2,9.2,10.9,7,10.9zM13.4,14.8l1.4-1.4l-3-3c0.7-1,1.1-2.1,1.1-3.4c0-3.2-2.6-5.8-5.8-5.8C3.8,1.2,1.2,3.8,1.2,7c0,3.2,2.6,5.8,5.8,5.8c1.3,0,2.4-0.4,3.4-1.1L13.4,14.8z"/>
                                    </svg>
                                </span>
                            </button>
                        </span>
                        </div>
                    </div>
                </@nhsdDropdownInput>
                <@nhsdDropdownContent/>
            </@nhsdDropdown>
        </div>
    </div>
</div>
<#include "js/dropdown-js.ftl">
