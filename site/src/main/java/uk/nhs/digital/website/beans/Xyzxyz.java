package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.ContentNodeBinder;
import org.hippoecm.hst.content.beans.ContentNodeBindingException;
import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


import javax.jcr.RepositoryException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "xyzxyz")
@XmlAccessorType(XmlAccessType.NONE)
@HippoEssentialsGenerated(internalName = "website:xyzxyz")
@Node(jcrType = "website:xyzxyz")
public class Xyzxyz extends BaseDocument implements ContentNodeBinder {

    private String name;
    private String description;
    private String apiMethod;

    @XmlElement
    @HippoEssentialsGenerated(internalName = "website:apimethod")
    public String getApiMethod() {
        if (this.apiMethod == null) {
            this.apiMethod = getProperty("website:apimethod");
        }
        return this.apiMethod;
    }

    @XmlElement
    @HippoEssentialsGenerated(internalName = "website:name")
    public String getName() {
        if (this.name == null) {
            this.name = getProperty("website:name");
        }
        return this.name;
    }

    @XmlElement
    @HippoEssentialsGenerated(internalName = "website:description")
    public String getDescription() {
        if (this.description == null) {
            this.description = getProperty("website:description");
        }
        return this.description;
    }

    @HippoEssentialsGenerated(internalName = "website:name")
    public void setName(String name) {
        this.name = name;
    }

    @HippoEssentialsGenerated(internalName = "website:description")
    public void setDescription(String description) {
        this.description = description;
    }

    @HippoEssentialsGenerated(internalName = "website:apimethod")
    public void setApiMethod(String apimethod) {
        this.apiMethod = apimethod;
    }


    public boolean bind(Object content, javax.jcr.Node node) throws ContentNodeBindingException {
        try {
            Xyzxyz xyzxyz = (Xyzxyz) content;
            node.setProperty("website:name", xyzxyz.getName());
            node.setProperty("website:description", xyzxyz.getDescription());
            node.setProperty("website:apimethod", xyzxyz.getApiMethod());
            return true;
        } catch (RepositoryException e) {
            throw new ContentNodeBindingException(e);
        }
    }

}
