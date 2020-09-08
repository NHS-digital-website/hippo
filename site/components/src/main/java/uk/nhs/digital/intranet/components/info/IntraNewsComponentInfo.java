package uk.nhs.digital.intranet.components.info;

import org.hippoecm.hst.core.parameters.*;
import uk.nhs.digital.common.components.info.LatestNewsComponentInfo;

@FieldGroupList({
    @FieldGroup(
        value = {"newsNumberOfRows", "featuredArticle"},
        titleKey = "list.group"),
    @FieldGroup(
        value = {
            "tasksTitle",
            "tasksPageUrl",
            "taskDocument1",
            "taskDocument2",
            "taskDocument3",
            "taskDocument4",
            "taskDocument5",
            "taskDocument6",
            "taskDocument7",
            "taskDocument8"
        },
        titleKey = "Tasks")
    })
public interface IntraNewsComponentInfo extends LatestNewsComponentInfo {

    @Parameter(name = "tasksTitle", displayName = "Tasks Title")
    String getTasksTitle();

    @Parameter(name = "tasksPageUrl", displayName = "Tasks Page URL")
    String getTasksPageUrl();

    @Parameter(name = "taskDocument1", displayName = "Task Document 1")
    @JcrPath(
        isRelative = true,
        pickerSelectableNodeTypes = {"intranet:task"})
    String getTaskOne();

    @Parameter(name = "taskDocument2", displayName = "Task Document 2")
    @JcrPath(
        isRelative = true,
        pickerSelectableNodeTypes = {"intranet:task"})
    String getTaskTwo();

    @Parameter(name = "taskDocument3", displayName = "Task Document 3")
    @JcrPath(
        isRelative = true,
        pickerSelectableNodeTypes = {"intranet:task"})
    String getTaskThree();

    @Parameter(name = "taskDocument4", displayName = "Task Document 4")
    @JcrPath(
        isRelative = true,
        pickerSelectableNodeTypes = {"intranet:task"})
    String getTaskFour();

    @Parameter(name = "taskDocument5", displayName = "Task Document 5")
    @JcrPath(
        isRelative = true,
        pickerSelectableNodeTypes = {"intranet:task"})
    String getTaskFive();

    @Parameter(name = "taskDocument6", displayName = "Task Document 6")
    @JcrPath(
        isRelative = true,
        pickerSelectableNodeTypes = {"intranet:task"})
    String getTaskSix();

    @Parameter(name = "taskDocument7", displayName = "Task Document 7")
    @JcrPath(
        isRelative = true,
        pickerSelectableNodeTypes = {"intranet:task"})
    String getTaskSeven();

    @Parameter(name = "taskDocument8", displayName = "Task Document 8")
    @JcrPath(
        isRelative = true,
        pickerSelectableNodeTypes = {"intranet:task"})
    String getTaskEight();

    @Parameter(name = "newsNumberOfRows", displayName = "News rows", defaultValue = "1")
    @DropDownList(value = {"1", "2"})
    String getNewsNumberOfRows();

    @Parameter(
        name = "featuredArticle",
        displayName = "Featured News Article",
        required = true)
    @JcrPath(
        isRelative = true,
        pickerConfiguration = "cms-pickers/documents",
        pickerSelectableNodeTypes = {"intranet:newsinternal"},
        pickerInitialPath = "content/documents/intranet"
    )
    String getFeaturedArticle();

    @Parameter(
        name = "path",
        required = false)
    @JcrPath(
        pickerConfiguration = "cms-pickers/documents",
        pickerSelectableNodeTypes = {"hippostd:folder"},
        pickerInitialPath = "/content/documents/intranet/news")
    @Override
    String getPath();

    @Parameter(
        name = "documentTypes",
        required = true,
        defaultValue = "intranet:newsinternal")
    @Override
    String getDocumentTypes();

    @Parameter(
        name = "documentDateField",
        required = false,
        defaultValue = "intranet:publicationdate")
    String getDocumentDateField();

    @Parameter(
        name = "sortField",
        required = false,
        defaultValue = "intranet:publicationdate")
    @Override
    String getSortField();
}
