package uk.nhs.digital.intranet.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import uk.nhs.digital.common.components.BaseGaContentComponent;
import uk.nhs.digital.intranet.beans.Task;
import uk.nhs.digital.website.beans.ComponentList;
import uk.nhs.digital.website.beans.Internallink;

import java.util.List;
import java.util.stream.Collectors;

public class TaskHubComponent extends BaseGaContentComponent {

    @Override
    public void doBeforeRender(HstRequest request,
        HstResponse response) {
        super.doBeforeRender(request, response);
        final ComponentList bean = (ComponentList) request.getRequestContext()
            .getContentBean();
        List<?> priorityTaskList = bean.getBlocks().stream()
            .filter(block -> block instanceof Internallink)
            .map(internalLink -> ((Internallink) internalLink).getLink())
            .filter(linkedDocument -> linkedDocument instanceof Task)
            .filter(task -> ((Task) task).getPriorityAction())
            .collect(Collectors.toList());
        request.setAttribute("priorityTasks", priorityTaskList);
    }
}
