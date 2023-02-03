/* global nhsd */

import cookies from '../utils/cookies';
import debounce from '../utils/debounce';

function isOrgSet() {
    const organisation = cookies.get('organisation');
    return organisation.length > 0;
}

// Do we need to open modal on page load?
if (window.openOrgTrackingModal && !isOrgSet()) nhsd('#track-download-modal').trigger('modal-open');

function trackOrgDownload(downloadUrl) {
    if (!isOrgSet()) return false;

    const organisation = cookies.get('organisation');

    // Tracking code
    window.logGoogleAnalyticsEvent('Download attachment', organisation, downloadUrl);

    window.dataLayer.push({
        event: 'download_attachment',
        file: downloadUrl,
        org: organisation,
    });

    return true;
}

// For "DECLINE" & "NOT FOUND" values. Hold these for 1 day.
const trackOrgLinks = document.querySelectorAll('.js-track-org-button');
trackOrgLinks.forEach((link) => link.addEventListener('click', (e) => {
    e.preventDefault();
    const orgCode = link.dataset.organisation;
    if (!orgCode) return;
    cookies.set('organisation', orgCode, 1);
    nhsd('#track-download-modal').trigger('modal-close');
}));

// If data-org-prompt is set on a link, launch the organisation prompt
// unless an organisation is already set
const orgPromptElements = document.querySelectorAll('[data-org-prompt]');
orgPromptElements.forEach((orgPromptElement) => {
    orgPromptElement.addEventListener('click', (e) => {
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
const searchInput = document.querySelector('#org-search-input');
const searchDropdown = document.querySelector('#org-search');
if (searchInput && searchDropdown) {
    const { apiUrl } = searchInput.dataset;

    let orgList = [];
    const searchEvent = debounce(() => {
        const searchText = searchInput.value;

        orgList = [];
        if (searchText.length > 2) {
            fetch(`${apiUrl}?orgName=${searchText}`)
                .then((response) => response.json())
                .then((orgs) => {
                    if (orgs.length > 0) {
                        // Limit to 100 items
                        orgList = orgs.slice(0, 100);
                        nhsd(searchDropdown).trigger('dropdown-set-items', orgList.map((i) => ({
                            text: `${i.orgName} (${i.code})`,
                            data: i,
                        })));
                        nhsd(searchDropdown).trigger('dropdown-open');
                    } else {
                        nhsd(searchDropdown).trigger('dropdown-close');
                    }
                })
                .catch(() => console.error('Couldn\'t retrieve organisation data'));
        } else {
            nhsd(searchDropdown).trigger('dropdown-close');
        }
    }, 500);
    nhsd(searchInput).on('keyup', searchEvent);
    nhsd(searchInput).on('click', () => {
        if (orgList.length > 0) {
            nhsd(searchInput).trigger('dropdown-open');
        }
    });

    // On org selection, create 'organisation' cookie
    nhsd(searchDropdown).on('dropdown-selection', (e, element) => {
        const { code, orgName } = element.dataset;
        if (!code || !orgName) return;
        cookies.set('organisation', `${orgName} (${code})`, 7);
    });

    document.querySelector('#track-download-confirm-org').addEventListener('click', () => {
        // If tracking is successful, close modal
        if (isOrgSet()) return nhsd('#track-download-modal').trigger('modal-close');

        // Else, show an error
        document.querySelector('#org-not-selected').removeAttribute('hidden');

        return false;
    });
}
