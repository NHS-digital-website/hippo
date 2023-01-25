<#ftl output_format="HTML">

<script>
// Search dropdown
var searchDropdownId = 'search-dropdown';
var countries = ["Afghanistan","Albania","Algeria","Andorra","Angola","Antigua & Deps","Argentina","Armenia","Australia","Austria","Azerbaijan","Bahamas","Bahrain","Bangladesh","Barbados","Belarus","Belgium","Belize","Benin","Bhutan","Bolivia","Bosnia Herzegovina","Botswana","Brazil","Brunei","Bulgaria","Burkina","Burundi","Cambodia","Cameroon","Canada","Cape Verde","Central African Rep","Chad","Chile","China","Colombia","Comoros","Congo","Congo {Democratic Rep}","Costa Rica","Croatia","Cuba","Cyprus","Czech Republic","Denmark","Djibouti","Dominica","Dominican Republic","East Timor","Ecuador","Egypt","El Salvador","Equatorial Guinea","Eritrea","Estonia","Ethiopia","Fiji","Finland","France","Gabon","Gambia","Georgia","Germany","Ghana","Greece","Grenada","Guatemala","Guinea","Guinea-Bissau","Guyana","Haiti","Honduras","Hungary","Iceland","India","Indonesia","Iran","Iraq","Ireland {Republic}","Israel","Italy","Ivory Coast","Jamaica","Japan","Jordan","Kazakhstan","Kenya","Kiribati","Korea North","Korea South","Kosovo","Kuwait","Kyrgyzstan","Laos","Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macedonia","Madagascar","Malawi","Malaysia","Maldives","Mali","Malta","Marshall Islands","Mauritania","Mauritius","Mexico","Micronesia","Moldova","Monaco","Mongolia","Montenegro","Morocco","Mozambique","Myanmar","{Burma}","Namibia","Nauru","Nepal","Netherlands","New Zealand","Nicaragua","Niger","Nigeria","Norway","Oman","Pakistan","Palau","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Poland","Portugal","Qatar","Romania","Russian Federation","Rwanda","St Kitts & Nevis","St Lucia","Saint Vincent & the Grenadines","Samoa","San Marino","Sao Tome & Principe","Saudi Arabia","Senegal","Serbia","Seychelles","Sierra Leone","Singapore","Slovakia","Slovenia","Solomon Islands","Somalia","South Africa","South Sudan","Spain","Sri Lanka","Sudan","Suriname","Swaziland","Sweden","Switzerland","Syria","Taiwan","Tajikistan","Tanzania","Thailand","Togo","Tonga","Trinidad & Tobago","Tunisia","Turkey","Turkmenistan","Tuvalu","Uganda","Ukraine","United Arab Emirates","United Kingdom","United States","Uruguay","Uzbekistan","Vanuatu","Vatican City","Venezuela","Vietnam","Yemen","Zambia","Zimbabwe"]

const searchDropdown = document.querySelector('#' + searchDropdownId);
const dropdownSearch = () => {
    var searchText = document.querySelector('.nhsd-t-form-input').value;
    var filteredCountries = countries.filter(i => i.toLowerCase().indexOf(searchText.toLowerCase()) >= 0);

    if (searchText.length > 0 && filteredCountries.length > 0) {
        nhsd(searchDropdown).trigger('dropdown-set-items', filteredCountries.map(i => ({
            text: i,
        })));
        nhsd(searchDropdown).trigger('dropdown-open');
    } else {
        nhsd(searchDropdown).trigger('dropdown-close');
    }
};
searchDropdown.querySelector('.nhsd-t-form-input').addEventListener('input', dropdownSearch);
searchDropdown.querySelector('.nhsd-t-form-input').addEventListener('click', dropdownSearch);


// Multiselct dropdown
var multiselectDropdownId = 'multiselect-dropdown';
var multiSelectDropdown = document.querySelector('#' + multiselectDropdownId);
var filters = ["Data publications", "Data sets", "Published work chapters", "News", "Information requests", "Cyber alerts", "2021", "2020", "2019", "2018", "2017", "2016", "Data and information", "Health and Social Care", "National Health Service", "Primary care", "Dental practice", "Dental treatment"];

function multiSelectDropdownSearch() {
    var searchText = multiSelectDropdown.querySelector('.nhsd-t-form-input').value;
    var filteredFilters = filters.filter(i => i.toLowerCase().indexOf(searchText.toLowerCase()) >= 0);

    if (searchText.length > 0 && filteredFilters.length > 0) {
        nhsd(multiSelectDropdown).trigger('dropdown-set-items', filteredFilters.map(i => {
            const name = i.toLowerCase().replace(/ /g, '-');
            return {
                text: i,
                name: name,
                checked: checked[name],
                checkbox: true,
            };
        }));

        nhsd(multiSelectDropdown).trigger('dropdown-open');
    } else {
        nhsd(multiSelectDropdown).trigger('dropdown-close');
    }
}
multiSelectDropdown.querySelector('.nhsd-t-form-input').addEventListener('input', multiSelectDropdownSearch);
multiSelectDropdown.querySelector('.nhsd-t-form-input').addEventListener('click', multiSelectDropdownSearch);

const checked = [];
nhsd(multiSelectDropdown).on('dropdown-selection', (e, selected) => {
    checked[selected.name] = selected.checked;
});


// Custom dropdown
var customDropdownId = 'custom-dropdown';
var people = [{
    name: "Firstname Lastname",
    role: "Job title",
}, {
    name: "Person 1",
    role: "Role",
}, {
    name: "Person 2",
    role: "Person role",
}, {
    name: "Example Person",
    role: "Job title",
}];
var documents = [{
    name: "General Document",
    year: 2020,
}, {
    name: "Publication 1",
    year: 2021,
}, {
    name: "Publication 2",
    year: 2019,
}, {
    name: "News",
    year: 2020,
}];

var customDropdown = document.querySelector('#' + customDropdownId);

function customDropdownSearch() {
    let searchText = customDropdown.querySelector('.nhsd-t-form-input').value;

    let filteredPeople = people.filter(i => i.name.toLowerCase().indexOf(searchText.toLowerCase()) >= 0);
    let filteredDocuments = documents.filter(i => i.name.toLowerCase().indexOf(searchText.toLowerCase()) >= 0);

    if (searchText.length > 0 && (filteredPeople.length > 0 || filteredDocuments.length > 0)) {
        let dropdownContent = '';

        if (filteredPeople.length > 0) {
            var filteredPeopleSearch = filteredPeople.map(person =>
                `<li>
                  <button class="nhsd-!t-flex" data-dropdown-search-value="` + person.name + `">
                    <div class="nhsd-!t-margin-right-xs-1 nhsd-!t-margin-right-s-1 nhsd-!t-margin-right-1">
                        <span class="nhsd-a-icon nhsd-a-icon--size-xs nhsd-!t-margin-right-2">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16"><g><path d="M 16 11.199219 L 15.332031 16 L 0.667969 16 L 0 11.199219 L 3.867188 9.667969 C 2.867188 8.601562 2.265625 7.199219 2.265625 5.734375 C 2.265625 2.601562 4.867188 0 8 0 C 11.132812 0 13.734375 2.601562 13.734375 5.734375 C 13.734375 7.199219 13.132812 8.601562 12.132812 9.667969 Z M 13.601562 12.667969 L 9.464844 11.066406 L 9.464844 8.867188 C 10.667969 8.066406 11.464844 7.199219 11.464844 5.667969 C 11.464844 3.800781 9.933594 2.265625 8.066406 2.265625 C 6.199219 2.265625 4.667969 3.800781 4.667969 5.667969 C 4.667969 7.199219 5.464844 8.066406 6.667969 8.867188 L 6.667969 11.066406 L 2.398438 12.667969 L 2.601562 13.734375 L 13.398438 13.734375 Z M 13.601562 12.667969 "/></g></svg>
                        </span>
                    </div>
                    <div>
                      <div class="nhsd-t-heading-xs nhsd-!t-margin-0">` + person.name + `</div>
                      <div>` + person.role + `</div>
                    </div>
                  </button>
                </li>`
            ).reduce((v, acc) => acc + v, '');

            dropdownContent += `<div class="nhsd-t-heading-xs nhsd-!t-padding-2 nhsd-!t-padding-left-4 nhsd-!t-margin-2 nhsd-!t-margin-top-0 nhsd-!t-bg-pale-grey nhsd-t-round">People</div>
                <ul>
                  ` + filteredPeopleSearch + `
                </ul>
              </div>`;
        }

        if (filteredDocuments.length > 0 && filteredPeople.length > 0) {
            dropdownContent += '<hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s nhsd-!t-margin-top-0" />';
        }

        if (filteredDocuments.length > 0) {
            var documentSearchResults = filteredDocuments.map(document =>
                `<li>
                    <button class="nhsd-!t-flex" data-dropdown-search-value="` + document.name + `">
                        <div class="nhsd-!t-margin-right-xs-1 nhsd-!t-margin-right-s-1 nhsd-!t-margin-right-1">
                            <span class="nhsd-a-icon nhsd-a-icon--size-xs nhsd-!t-margin-right-2">
                                <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px" viewBox="0 0 512 512" xml:space="preserve"><g><path d="M360.948,0H51.613v512h408.774V100.123L360.948,0z M392.258,443.871H119.742V68.129h202.323v70.194h70.194V443.871z"/></g><g><rect x="187.871" y="171.355" width="136.258" height="68.129"/></g><g><rect x="187.871" y="307.613" width="136.258" height="68.129"/></g></svg>
                            </span>
                        </div>
                        <div>
                            <div class="nhsd-t-heading-xs nhsd-!t-margin-0">` + document.name + `</div>
                            <div>` + document.year + `</div>
                        </div>
                    </button>
                </li>`
            ).reduce((v, acc) => acc + v, '');

            dropdownContent +=
                `<div class="nhsd-t-heading-xs nhsd-!t-padding-2 nhsd-!t-padding-left-4 nhsd-!t-margin-2 nhsd-!t-margin-top-0 nhsd-!t-bg-pale-grey nhsd-t-round">Documents</div>
                    <ul>
                        ` + documentSearchResults + `
                    </ul>
                </div>`;
        }

        nhsd(customDropdown).trigger('dropdown-set-content', dropdownContent);
        nhsd(customDropdown).trigger('dropdown-open');
    } else {
        nhsd(customDropdown).trigger('dropdown-close');
    }
}
customDropdown.querySelector('.nhsd-t-form-input').addEventListener('input', customDropdownSearch);
customDropdown.querySelector('.nhsd-t-form-input').addEventListener('click', customDropdownSearch);

nhsd(customDropdown).on('dropdown-selection', (e, selected) => {
    customDropdown.querySelector('.nhsd-t-form-input').value = selected.dataset.dropdownSearchValue;
    nhsd(customDropdown).trigger('dropdown-close');
    return false;
});

// Listen to all dropdown selection events for testing
nhsd.on('dropdown-selection', (e, selected) => window.dropdownValue = selected.innerText);
</script>