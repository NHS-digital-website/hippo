package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import uk.nhs.digital.common.components.info.SearchComponentInfo;
import uk.nhs.digital.common.enums.SearchArea;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ParametersInfo(type = SearchComponentInfo.class)
public class SearchTabsComponent extends SearchComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        List<String> tabs = Stream.of(SearchArea.values())
            .map(Enum::toString)
            .collect(Collectors.toList());

        request.setAttribute("tabs", tabs);
    }
}
