package jogoshannon.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ConfiguracoesView extends Composite {

    private static ConfiguracoesViewUiBinder uiBinder = GWT
            .create(ConfiguracoesViewUiBinder.class);

    interface ConfiguracoesViewUiBinder extends
            UiBinder<Widget, ConfiguracoesView> {
    }

    public ConfiguracoesView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
    }


}
