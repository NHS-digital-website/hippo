package uk.nhs.digital.intranet.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import uk.nhs.digital.common.components.ContentRewriterComponent;
import uk.nhs.digital.intranet.beans.Task;
import uk.nhs.digital.website.beans.ComponentList;
import uk.nhs.digital.website.beans.Internallink;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskHubComponent extends ContentRewriterComponent {

    @Override
    public void doBeforeRender(HstRequest request,
        HstResponse response) {
        super.doBeforeRender(request, response);
        final ComponentList bean = (ComponentList) request.getRequestContext()
            .getContentBean();

        List<?> priorityTaskList = getTasksFromLinkBlocks(bean.getBlocks())
            .filter(task -> ((Task) task).getPriorityAction())
            .collect(Collectors.toList());
        request.setAttribute("priorityTasks", priorityTaskList);

        List<?> alternativeTaskList = getTasksFromLinkBlocks(bean.getBlocks())
            .flatMap(task -> addAlternativeNameEntries((Task)task))
            .collect(Collectors.toList());
        request.setAttribute("alternativeTasks", alternativeTaskList);
    }

    private Stream<?> addAlternativeNameEntries(Task task) {
        return Arrays.stream(task.getAlternativeNames()).map(
            alternativeName -> new TaskWithAlternativeName(alternativeName, task));
    }

    private Stream<?> getTasksFromLinkBlocks(List<?> input) {
        return input.stream()
            .filter(block -> block instanceof Internallink)
            .map(internalLink -> ((Internallink) internalLink).getLink())
            .filter(linkedDocument -> linkedDocument instanceof Task);
    }

    public static class TaskWithAlternativeName {

        private final String title;
        private final Task task;

        TaskWithAlternativeName(String title, Task task) {
            this.title = title;
            this.task = task;
        }

        public String getType() {
            return "alternativeTask";
        }

        public Task getTask() {
            return task;
        }

        public String getTitle() {
            return title;
        }

        public String getShortsummary() {
            return task.getShortsummary();
        }
    }
}
