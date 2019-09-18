package uk.nhs.digital.website.beans;

import org.hippoecm.hst.jaxrs.model.content.HippoDocumentRepresentation;

import javax.jcr.RepositoryException;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "xyzrep")
public class XyzxyzRepresentation extends HippoDocumentRepresentation {

    private String documentName;
    private String name;
    private String description;
    private String apiMethod;

    public XyzxyzRepresentation represent(Xyzxyz bean) throws RepositoryException {
        super.represent(bean);
        this.name = bean.getName();
        this.description = bean.getDescription();
        this.apiMethod = bean.getApiMethod();
        return this;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setDocumentName(String doc) {
        this.documentName = doc;
    }

    public void setApiMethod(String apiMethodName) {
        this.apiMethod = apiMethodName;
    }

}
