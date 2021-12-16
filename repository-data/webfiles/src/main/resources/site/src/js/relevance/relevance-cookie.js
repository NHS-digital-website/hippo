import cookies from '../utils/cookies';

function showTrackingInfo() {
    $.ajax({
        url: '/gdpr/visitorinfo',
        type: 'GET',
        success(result) {
            $('.container-tracking-info').html(`<pre>${JSON.stringify(result, undefined, 4)}</pre>`);
        }
    });
}

function clearTrackingInfo() {
    $.ajax({
        url: '/gdpr/visitorinfo',
        type: 'DELETE',
        success() {
            showTrackingInfo();
        }
    });
}

function getVisitorTrackingStatus() {
    $('#tracking-status').html(cookies.get("visitortracking"));
}

function setVisitorTracking(status) {
    // function may be called directly

    const cookieConsent = cookies.get("CookieConsent");
    if (cookieConsent.indexOf("statistics:false") >= 0) {
        if (status === 'allowed') {
            alert('Only necessary cookies have been allowed. To enable tracking, all cookies must be allowed in Cookie consent');
        }
        return;
    }
    const CookieDate = new Date;
    CookieDate.setFullYear(CookieDate.getFullYear() + 2);
    document.cookie = `visitortracking=${status}; expires=${CookieDate.toUTCString()}; path=/`;


    if (status === 'disallowed') {
        clearTrackingInfo();
    }

    getVisitorTrackingStatus();
}

export function initCookieConsent() {

    const cookieConsent = cookies.get("CookieConsent");
    if (cookieConsent !== "") {
        if (cookieConsent.indexOf("statistics:true") >= 0 && cookies.get("visitortracking") === '') {
            setVisitorTracking('allowed');
        }

        if (cookieConsent.indexOf("statistics:false") >= 0) {
            clearTrackingInfo();
        }
    }

    $(function () {
        let status = cookies.get("visitortracking");
        if (status === '') {
            status = 'disallowed (cookies disabled)'
        }
        $('#tracking-status').html(status);
    });
}
