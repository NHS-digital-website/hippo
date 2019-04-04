package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:code")
@Node(jcrType = "website:code")
public class Code extends HippoCompound {

    public String getSectionType() {
        return "code";
    }

    @HippoEssentialsGenerated(internalName = "website:heading")
    public String getHeading() {
        return getProperty("website:heading");
    }

    @HippoEssentialsGenerated(internalName = "website:headinglevel")
    public String getHeadingLevel() {
        return getProperty("website:headinglevel");
    }

    @HippoEssentialsGenerated(internalName = "website:linenumbers")
    public Boolean getLineNumbers() {
        return getProperty("website:linenumbers");
    }

    @HippoEssentialsGenerated(internalName = "website:firstlinenumber")
    public String getFirstLineNumber() {
        return getProperty("website:firstlinenumber");
    }

    @HippoEssentialsGenerated(internalName = "website:wraplines")
    public Boolean getWrapLines() {
        return getProperty("website:wraplines");
    }

    @HippoEssentialsGenerated(internalName = "website:codelanguage")
    public String getCodeLanguage() {
        return getProperty("website:codelanguage");
    }

    @HippoEssentialsGenerated(internalName = "website:codetext")
    public String getCodeText() {
        return getProperty("website:codetext");
    }

    @HippoEssentialsGenerated(internalName = "website:codetype")
    public String getCodeType() {
        return getProperty("website:codetype");
    }

}
