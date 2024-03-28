package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import uk.nhs.digital.common.enums.SearchArea;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchTabsComponent extends SearchComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        List<String> tabs = Stream.of(SearchArea.values())
            .map(Enum::toString)
            .collect(Collectors.toList());
        request.setAttribute("query", getQueryParameter(request));
        request.setAttribute("sort", getSortOption(request));
        request.setAttribute("facets", request.getRequestContext().getAttribute("facets"));
        request.setAttribute("tabs", tabs);
        request.setAttribute("area", getAreaOption(request).toString());
    }
}
