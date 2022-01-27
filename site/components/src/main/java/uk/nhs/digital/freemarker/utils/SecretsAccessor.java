package uk.nhs.digital.freemarker.utils;

import static org.hippoecm.hst.site.HstServices.getComponentManager;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;
import uk.nhs.digital.toolbox.secrets.ApplicationSecrets;

import java.util.List;

public class SecretsAccessor implements TemplateMethodModelEx {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecretsAccessor.class);

    @Override
    public Object exec(List list) throws TemplateModelException {
        if (list.size() != 1 && !(list.get(0) instanceof SimpleScalar)) {
            throw new TemplateModelException("Wrong argument. The argument should be a string.");
        } else {
            String key = ((SimpleScalar) list.get(0)).getAsString();
            String value = ((ApplicationSecrets) getComponentManager().getComponent("applicationSecrets")).getValue(key);
            if (StringUtils.isBlank(value)) {
                LOGGER.warn("ApplicationSecrets did not return a value for " + key);
            }
            return value;
        }
    }
}
