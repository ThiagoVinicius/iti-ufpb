package jogoshannon.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ExperimentoWidget extends Composite {

    private static ExperimentoWidgetUiBinder uiBinder = GWT
            .create(ExperimentoWidgetUiBinder.class);

    interface ExperimentoWidgetUiBinder extends
            UiBinder<Widget, ExperimentoWidget> {
    }

    public ExperimentoWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
