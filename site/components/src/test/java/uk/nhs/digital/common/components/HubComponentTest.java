package uk.nhs.digital.common.components;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.core.container.ContainerConfiguration;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onehippo.cms7.essentials.components.ext.DoBeforeRenderExtension;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.website.beans.Hub;

import java.util.Collections;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HstServices.class, HubComponent.class})
@PowerMockIgnore({"javax.management.*", "com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*"})
public class HubComponentTest {

    @Mock
    private HstRequest request;

    @Mock
    private HstResponse response;

    @Mock
    private HstRequestContext context;

    @Mock
    private Hub hub;

    @Mock
    private ComponentManager componentManager;

    @Mock
    private DoBeforeRenderExtension doBeforeRenderExtension;

    @Mock
    private ContainerConfiguration containerConfiguration;

    private HubComponent component;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        PowerMockito.mockStatic(HstServices.class);
        PowerMockito.when(HstServices.getComponentManager()).thenReturn(componentManager);
        when(componentManager.getComponent(DoBeforeRenderExtension.class.getName()))
            .thenReturn(doBeforeRenderExtension);
        when(componentManager.getContainerConfiguration()).thenReturn(containerConfiguration);
        when(containerConfiguration.getBoolean("hst.preview", false)).thenReturn(false);

        component = PowerMockito.spy(new HubComponent());
        PowerMockito.doReturn(Collections.emptyList()).when(component)
            .getRelatedDocuments(org.mockito.ArgumentMatchers.any());
        PowerMockito.suppress(PowerMockito.method(DocumentChildComponent.class, "setEditMode",
            HstRequest.class));
        PowerMockito.suppress(PowerMockito.method(org.onehippo.cms7.essentials.components.CommonComponent.class,
            "setContentBeanWith404", HstRequest.class, HstResponse.class));
        PowerMockito.suppress(PowerMockito.method(org.onehippo.cms7.essentials.components.CommonComponent.class,
            "pageNotFound", HstResponse.class));
        when(request.getRequestContext()).thenReturn(context);
    }

    @Test
    public void populatesComponentsAttributeWhenHubPresent() {
        when(context.getContentBean(Hub.class)).thenReturn(hub);
        when(hub.getComponentlist()).thenReturn(Collections.emptyList());

        component.doBeforeRender(request, response);

        verify(request).setAttribute("components", Collections.emptyMap());
    }

    @Test
    public void doesNothingWhenHubMissing() {
        when(context.getContentBean(Hub.class)).thenReturn(null);

        component.doBeforeRender(request, response);

        verifyNoInteractions(hub);
        verifyNoInteractions(response);
    }
}
