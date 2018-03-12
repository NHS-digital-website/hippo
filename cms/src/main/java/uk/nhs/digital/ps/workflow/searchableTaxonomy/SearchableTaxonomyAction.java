package uk.nhs.digital.ps.workflow.searchableTaxonomy;

import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.model.ModelException;
import org.onehippo.repository.documentworkflow.action.AbstractDocumentTaskAction;

public class SearchableTaxonomyAction extends AbstractDocumentTaskAction<SearchableTaxonomyTask> {

    private static final String VARIANT_EXPR = "variantExpr";

    @Override
    protected SearchableTaxonomyTask createWorkflowTask() {
        return new SearchableTaxonomyTask();
    }

    public String getVariant() {
        return getParameter(VARIANT_EXPR);
    }

    public void setVariant(String variant) {
        setParameter(VARIANT_EXPR, variant);
    }

    @Override
    protected void initTask(SearchableTaxonomyTask task) throws ModelException, SCXMLExpressionException {
        super.initTask(task);
        task.setVariant(eval(getVariant()));
    }
}
