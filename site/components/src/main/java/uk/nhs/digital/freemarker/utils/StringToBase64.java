package uk.nhs.digital.freemarker.utils;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class StringToBase64 implements TemplateMethodModelEx {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringToBase64.class);

    @Override
    public Object exec(List list) throws TemplateModelException {
        if (list.size() != 1 && !(list.get(0) instanceof SimpleScalar)) {
            throw new TemplateModelException("Wrong argument. The argument should be a string.");
        } else {
            String string = ((SimpleScalar) list.get(0)).getAsString();
            if (StringUtils.isBlank(string)) {
                LOGGER.warn("String content is blank");
            }
            return encoded(string);
        }
    }

    String encoded(String s) {
        return Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }
}