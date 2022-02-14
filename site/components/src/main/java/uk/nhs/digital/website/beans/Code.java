package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.highlighter.Highlighter;
import uk.nhs.digital.highlighter.Language;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@HippoEssentialsGenerated(internalName = "website:code")
@Node(jcrType = "website:code")
@XmlAccessorType(XmlAccessType.NONE)
public class Code extends HippoCompound {
    private Language language = null;

    @XmlElement(name = "sectionType")
    public String getSectionType() {
        return "code";
    }

    @HippoEssentialsGenerated(internalName = "website:heading")
    @XmlElement(name = "heading")
    public String getHeading() {
        return getSingleProperty("website:heading");
    }

    @HippoEssentialsGenerated(internalName = "website:headinglevel")
    public String getHeadingLevel() {
        return getSingleProperty("website:headinglevel");
    }

    @HippoEssentialsGenerated(internalName = "website:linenumbers")
    public Boolean getLineNumbers() {
        return getSingleProperty("website:linenumbers");
    }

    @HippoEssentialsGenerated(internalName = "website:firstlinenumber")
    public String getFirstLineNumber() {
        return getSingleProperty("website:firstlinenumber");
    }

    @HippoEssentialsGenerated(internalName = "website:wraplines")
    public Boolean getWrapLines() {
        return getSingleProperty("website:wraplines");
    }

    @HippoEssentialsGenerated(internalName = "website:codelanguage")
    public String getCodeLanguage() {
        return getSingleProperty("website:codelanguage");
    }

    @HippoEssentialsGenerated(internalName = "website:codetext")
    @XmlElement(name = "code")
    public String getCodetext() {
        return getSingleProperty("website:codetext");
    }

    public String getCodeTextHighlighted() {
        return Highlighter.INSTANCE.paint(getCodetext(), getLanguage());
    }

    @XmlElement(name = "language")
    public Language getLanguage() {
        if (language == null) {
            language = Language.getByKey(getCodeLanguage());
        }
        return language;
    }
}
