import cookies from '../utils/cookies';

// Do we need to open modal on page load?
if (window.openOrgTrackingModal && !isOrgSet()) nhsd('#track-download-modal').trigger('modal-open');

function isOrgSet() {
    const organisation = cookies.get('organisation');
    return organisation.length > 0;
}

function trackOrgDownload(downloadUrl) {
    if (!isOrgSet()) return false;

    const organisation = cookies.get('organisation');

    // Tracking code
    window.logGoogleAnalyticsEvent('Download attachment', organisation, downloadUrl);

    window.dataLayer.push({
        'event': 'download_attachment',
        'file': downloadUrl,
        'org': organisation
    });

    return true;
}

// For "DECLINE" & "NOT FOUND" values. Hold these for 1 day.
const trackOrgLinks = document.querySelectorAll('.js-track-org-button');
trackOrgLinks.forEach(link => link.addEventListener('click', (e) => {
    e.preventDefault();
    const orgCode = link.dataset.organisation;
    if (!orgCode) return;
    cookies.set('organisation', orgCode, 1);
    nhsd('#track-download-modal').trigger('modal-close');
}))

// If data-org-prompt is set on a link, launch the organisation prompt
// unless an organisation is already set
const orgPromptElements = document.querySelectorAll('[data-org-prompt]');
orgPromptElements.forEach(orgPromptElement => {
    orgPromptElement.addEventListener('click', function(e) {
        // Try to track if cookie exists
        if (trackOrgDownload(orgPromptElement.href)) return;

        // Tracking failed, open prompt
        e.preventDefault();
        nhsd('#track-download-modal').trigger('modal-open');
        // Wait for modal to close then replay click event
        nhsd('#track-download-modal').once('modal-close', () => orgPromptElement.click());
    });
});

// Setup organisation dropdown if found on page
const searchInput = document.querySelector('#org-search');
const searchDropdown = document.querySelector('#autocomplete-default');
if (searchInput && searchDropdown) {
    const apiUrl = searchInput.dataset.apiUrl;
    nhsd(searchInput).on(['keyup', 'click'], e => {
        let searchText = searchInput.value;

        if (searchText.length > 2) {
            fetch(apiUrl + '?orgName=' + searchText)
                .then(response => response.json())
                .then(orgs => {
                    if (orgs.length > 0) {
                        // Limit to 100 items
                        let orgList = orgs.slice(0, 1000);
                        nhsd(searchDropdown).trigger('dropdown-set-items', orgList.map(i => ({
                            text: `${i.orgName} (${i.code})`,
                            data: i,
                        })));
                        nhsd(searchDropdown).trigger('dropdown-open');
                    } else {
                        nhsd(searchDropdown).trigger('dropdown-close');
                    }
                })
                .catch(() => {
                    console.error(`Couldn't retrieve organisation data`);
                });
        } else {
            nhsd(searchDropdown).trigger('dropdown-close');
        }
    });

    // On org selection, create 'organisation' cookie
    nhsd(searchDropdown).on('dropdown-selection', (e, element) => {
        const orgCode = element.dataset.code;
        const orgName = element.dataset.orgName
        if (!orgCode || !orgName) return;
        cookies.set('organisation', `${orgName} (${orgCode})`, 7);
    });

    document.querySelector('#track-download-confirm-org').addEventListener('click', function() {
        // If tracking is successful, close modal
        if (isOrgSet()) return nhsd('#track-download-modal').trigger('modal-close');

        // Else, show an error
        document.querySelector('#org-not-selected').removeAttribute('hidden');
    })
}
