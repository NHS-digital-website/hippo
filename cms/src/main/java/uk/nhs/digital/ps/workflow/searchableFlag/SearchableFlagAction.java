package uk.nhs.digital.ps.workflow.searchableFlag;

import static java.lang.Boolean.parseBoolean;

import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.model.ModelException;
import org.onehippo.repository.documentworkflow.action.AbstractDocumentTaskAction;

public class SearchableFlagAction extends AbstractDocumentTaskAction<SearchableFlagTask> {

    private static final String VARIANT_EXPR = "variantExpr";
    private static final String DEPUBLISHING = "depublishing";

    @Override
    protected SearchableFlagTask createWorkflowTask() {
        return new SearchableFlagTask();
    }

    public String getVariant() {
        return getParameter(VARIANT_EXPR);
    }

    public void setVariant(String variant) {
        setParameter(VARIANT_EXPR, variant);
    }

    public boolean getDepublishing() {
        return parseBoolean(getParameter(DEPUBLISHING));
    }

    public void setDepublishing(String publishing) {
        setParameter(DEPUBLISHING, publishing);
    }

    @Override
    protected void initTask(SearchableFlagTask task) throws ModelException, SCXMLExpressionException {
        super.initTask(task);
        task.setVariant(eval(getVariant()));
        task.setDepublishing(getDepublishing());
    }
}
