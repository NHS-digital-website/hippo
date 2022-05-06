package uk.nhs.digital.customfield.datefield.resource;

import com.onehippo.cms7.eforms.cms.model.AbstractFieldModel;
import com.onehippo.cms7.eforms.cms.panels.AbstractFieldPanel;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.form.TextField;

public class CustomDateFieldPanel extends AbstractFieldPanel implements IHeaderContributor {

    public CustomDateFieldPanel(String id, AbstractFieldModel fieldModel) {
        super(id, fieldModel);

        TextField textField = new TextField("field");
        textField.setEnabled(false);
        add(textField);
    }


}
