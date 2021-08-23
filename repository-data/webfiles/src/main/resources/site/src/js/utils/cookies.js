export default {
    get(cname) {
        const name = `${cname}=`;
        const decodedCookie = decodeURIComponent(document.cookie);
        const ca = decodedCookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) === ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) === 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    },
    set(cname, cvalue, exdays) {
        const d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        let expires = "expires=" + d.toUTCString();
        document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
    },
    // Helper function to check for Cookiebot cookie content
    onCookieConsent(consentType) {
        const checkConsent = consentType => {
            if (consentType == null) return true;
            return Cookiebot.consent[consentType];
        }

        return new Promise((res, rej) => {
            if (Cookiebot.hasResponse) {
                if (checkConsent(consentType)) res();
                else rej();
            } else {
                window.addEventListener('CookiebotOnConsentReady', () => {
                    if (checkConsent(consentType)) res();
                    else rej();
                })
            }
        });
    }
}
