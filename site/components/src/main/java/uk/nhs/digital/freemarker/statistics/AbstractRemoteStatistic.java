package uk.nhs.digital.freemarker.statistics;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

abstract class AbstractRemoteStatistic implements TemplateMethodModelEx {

    @Override
    public Object exec(List list) throws TemplateModelException {
        if (list.size() != 1 && !(list.get(0) instanceof SimpleScalar)) {
            throw new TemplateModelException("Wrong argument. Take 1 argument of type string");
        }
        try {
            return getDataFromApi(new URL(((SimpleScalar) list.get(0)).getAsString()));
        } catch (MalformedURLException e) {
            throw new TemplateModelException("1st argument should be a valid URL");
        }
    }

    protected abstract Statistic getDataFromApi(URL url);

}
