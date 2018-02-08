function setEnvironmentImage() {
    var url = document.URL;

	if (url.includes("tst.nhsd.io")) {
	    document.body.className = 'tst';
	} else if (url.includes("uat.nhsd.io")) {
	    document.body.className = 'uat';
	} else if (url.includes("localhost")) {
	    document.body.className = 'local';
	} else {
	    document.body.className = 'live';
	}
}

setEnvironmentImage();
