package uk.nhs.digital.website.security;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.opensaml.saml2.core.NameID;
import org.opensaml.xml.Namespace;
import org.opensaml.xml.NamespaceManager;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSBooleanValue;
import org.opensaml.xml.util.IDIndex;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;
import org.springframework.security.saml.SAMLCredential;
import org.w3c.dom.Element;
import uk.nhs.digital.intranet.model.IntranetUser;

import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

@RunWith(MockitoJUnitRunner.class)
public class SamlUserDetailsServiceTest {

    private static final String DUMMY_PASSWORD = "dummy_password";
    private static final String ADFS_ATTR_GIVEN_NAME = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/givenname";
    private static final String ADFS_ATTR_SURNAME = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/surname";
    private static final String USER_NAME = "user1";
    private static final String GIVEN_NAME = "John";
    private static final String SURNAME = "Doe";

    @Mock private SAMLCredential samlCredential;

    private final SamlUserDetailsService userDetailsService = new SamlUserDetailsService();

    @Test
    public void createsIntranetUserWithFullNameAndNameId() {
        when(samlCredential.getNameID()).thenReturn(new TestNameId(USER_NAME));
        when(samlCredential.getAttributeAsString(ADFS_ATTR_GIVEN_NAME)).thenReturn(GIVEN_NAME);
        when(samlCredential.getAttributeAsString(ADFS_ATTR_SURNAME)).thenReturn(SURNAME);

        final IntranetUser user = (IntranetUser) userDetailsService.loadUserBySAML(samlCredential);

        assertEquals(USER_NAME, user.getUsername());
        assertEquals(DUMMY_PASSWORD, user.getPassword());
        assertEquals(GIVEN_NAME, user.getFirstName());
        assertEquals(SURNAME, user.getLastName());
        assertTrue(user.getAuthorities().isEmpty());
    }

    private static class TestNameId implements NameID {

        private String value;

        public TestNameId(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public void setValue(String s) {
            this.value = s;
        }

        @Override
        public String getNameQualifier() {
            return null;
        }

        @Override
        public void setNameQualifier(String s) {

        }

        @Override
        public String getSPNameQualifier() {
            return null;
        }

        @Override
        public void setSPNameQualifier(String s) {

        }

        @Override
        public String getFormat() {
            return null;
        }

        @Override
        public void setFormat(String s) {

        }

        @Override
        public String getSPProvidedID() {
            return null;
        }

        @Override
        public void setSPProvidedID(String s) {

        }

        @Override
        public List<Validator> getValidators() {
            return null;
        }

        @Override
        public void registerValidator(Validator validator) {

        }

        @Override
        public void deregisterValidator(Validator validator) {

        }

        @Override
        public void validate(boolean b) throws ValidationException {

        }

        @Override
        public void addNamespace(Namespace namespace) {

        }

        @Override
        public void detach() {

        }

        @Override
        public Element getDOM() {
            return null;
        }

        @Override
        public QName getElementQName() {
            return null;
        }

        @Override
        public IDIndex getIDIndex() {
            return null;
        }

        @Override
        public NamespaceManager getNamespaceManager() {
            return null;
        }

        @Override
        public Set<Namespace> getNamespaces() {
            return null;
        }

        @Override
        public String getNoNamespaceSchemaLocation() {
            return null;
        }

        @Override
        public List<XMLObject> getOrderedChildren() {
            return null;
        }

        @Override
        public XMLObject getParent() {
            return null;
        }

        @Override
        public String getSchemaLocation() {
            return null;
        }

        @Override
        public QName getSchemaType() {
            return null;
        }

        @Override
        public boolean hasChildren() {
            return false;
        }

        @Override
        public boolean hasParent() {
            return false;
        }

        @Override
        public void releaseChildrenDOM(boolean b) {

        }

        @Override
        public void releaseDOM() {

        }

        @Override
        public void releaseParentDOM(boolean b) {

        }

        @Override
        public void removeNamespace(Namespace namespace) {

        }

        @Override
        public XMLObject resolveID(String s) {
            return null;
        }

        @Override
        public XMLObject resolveIDFromRoot(String s) {
            return null;
        }

        @Override
        public void setDOM(Element element) {

        }

        @Override
        public void setNoNamespaceSchemaLocation(String s) {

        }

        @Override
        public void setParent(XMLObject xmlObject) {

        }

        @Override
        public void setSchemaLocation(String s) {

        }

        @Override
        public Boolean isNil() {
            return null;
        }

        @Override
        public XSBooleanValue isNilXSBoolean() {
            return null;
        }

        @Override
        public void setNil(Boolean aBoolean) {

        }

        @Override
        public void setNil(XSBooleanValue xsBooleanValue) {

        }
    }
}
