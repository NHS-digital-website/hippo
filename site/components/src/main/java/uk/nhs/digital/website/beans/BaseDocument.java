package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.onehippo.cms7.essentials.dashboard.annotations.*;

import java.util.Calendar;
import java.util.Date;

@HippoEssentialsGenerated(internalName = "website:basedocument")
@Node(jcrType = "website:basedocument")
public class BaseDocument extends HippoDocument {

    public Date getLastModified() {
        return ((Calendar) getSingleProperty("hippostdpubwf:lastModificationDate")).getTime();
    }

    public Date getLastPublicationDate() {
        return ((Calendar) getSingleProperty("hippostdpubwf:publicationDate")).getTime();
    }

}

