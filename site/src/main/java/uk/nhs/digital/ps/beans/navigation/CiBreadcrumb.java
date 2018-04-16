package uk.nhs.digital.ps.beans.navigation;

import org.onehippo.forge.breadcrumb.om.Breadcrumb;
import org.onehippo.forge.breadcrumb.om.BreadcrumbItem;

import java.util.List;

public class CiBreadcrumb extends Breadcrumb {

    private boolean isClinicalIndicator;

    public CiBreadcrumb(List<BreadcrumbItem> items, String separator, boolean isClinicalIndicator) {
        super(items, separator);
        this.isClinicalIndicator = isClinicalIndicator;
    }

    public boolean isClinicalIndicator() {
        return this.isClinicalIndicator;
    }
}
