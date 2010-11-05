package jogoshannon.client.view;

import jogoshannon.client.Jogo_de_Shannon;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public class UsuarioWidget extends Composite {

    private final long id;
    private Label idLabel;
    private CheckBox valendo;
    private Image carregando;
    private Label infoLabel;

    public UsuarioWidget(long id) {
        super();

        this.id = id;

        SimplePanel root = new SimplePanel();
        initWidget(root);

        idLabel = new Label("ID = " + id);
        idLabel.setWidth("7em");

        valendo = new CheckBox("Considerar resultados");
        valendo.setEnabled(false);

        carregando = new Image(Jogo_de_Shannon.IMAGENS.ampulheta());
        
        infoLabel = new Label();

        HorizontalPanel painelPrincipal = new HorizontalPanel();
        painelPrincipal
                .setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        painelPrincipal.add(idLabel);
        painelPrincipal.add(valendo);
        painelPrincipal.add(carregando);
        painelPrincipal.add(infoLabel);

        root.setWidget(painelPrincipal);

    }

    public HasClickHandlers getBotaoRemover() {
        return valendo;
    }

    public void ativaBotaoRemover(boolean ativar) {
        valendo.setEnabled(ativar);
    }

    public long getId() {
        return id;
    }

    public void setCarregando(boolean estado) {
        carregando.setVisible(estado);
    }
    
    public void setInfo (String info) {
        SafeHtmlBuilder safe = new SafeHtmlBuilder();
        safe.appendEscaped("(");
        safe.appendEscaped(info);
        safe.appendEscaped(")");
        infoLabel.setText(safe.toSafeHtml().asString());
    }

    @Override
    public int hashCode() {
        return new Long(id).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UsuarioWidget other = (UsuarioWidget) obj;
        if (id != other.id)
            return false;
        return true;
    }

    public void setCheckboxMarcada(boolean marcado) {
        valendo.setValue(marcado, false);
    }
    
    public boolean getCheckboxMarcada() {
        return valendo.getValue();
    }

    public void setCheckboxVisivel(boolean b) {
        valendo.setVisible(false);
    }
    
}
