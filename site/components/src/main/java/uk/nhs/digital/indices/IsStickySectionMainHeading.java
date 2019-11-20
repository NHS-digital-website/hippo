package uk.nhs.digital.indices;

import freemarker.ext.util.WrapperTemplateModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.List;

public class IsStickySectionMainHeading implements TemplateMethodModelEx {

    /*
     * Makes a way for Freemaker to check the given StickySection is a main heading.
     * <p>
     * Method is based on https://stackoverflow.com/a/33246066/4122517
     */
    @Override
    public Object exec(List list) throws TemplateModelException {
        if (list.size() != 1) {
            throw new TemplateModelException("Wrong number of arguments for method 'isStickySectionMainHeading' expected 1 of type StickySection");
        } else {
            Object object = ((WrapperTemplateModel) list.get(0)).getWrappedObject();
            return object instanceof  StickySection && ((StickySection) object).isMainHeading() && !((StickySection) object).getHeading().isEmpty();
        }
    }
}
