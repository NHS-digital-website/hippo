package uk.nhs.digital.freemarker.svg;

import static java.text.MessageFormat.format;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import uk.nhs.digital.svg.colour.SvgColourMagic;

import java.util.List;

public class SvgChangeColour implements TemplateMethodModelEx {

    @Override
    public Object exec(List list) throws TemplateModelException {
        if (list.size() != 2
            && !(list.get(0) instanceof SimpleScalar)
            && !(list.get(1) instanceof SimpleScalar)
            && !(SvgColourMagic.isColour(((SimpleScalar) list.get(1)).getAsString()))
        ) {
            throw new TemplateModelException("Wrong arguments. 1st argument should be a string. 2nd argument should be a string-hexadecimal.");
        } else {
            String subject = ((SimpleScalar) list.get(0)).getAsString();
            String colour = format("#{0}",((SimpleScalar) list.get(1)).getAsString());
            return SvgColourMagic.replaceColour(subject, colour);
        }
    }

}
