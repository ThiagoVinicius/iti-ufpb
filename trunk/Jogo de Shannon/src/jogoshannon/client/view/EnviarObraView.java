package jogoshannon.client.view;

import jogoshannon.client.presenter.EnviarObraApresentador;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class EnviarObraView extends Composite 
implements EnviarObraApresentador.Exibicao {

    private static EnviarObraViewUiBinder uiBinder = GWT
            .create(EnviarObraViewUiBinder.class);

    interface EnviarObraViewUiBinder extends UiBinder<Widget, EnviarObraView> {
    }

    @UiField
    protected FormPanel form;
    
    @UiField
    protected Button formSubmit;
    
    @UiField
    protected Image carregando;
    
    @UiField
    protected Image erro;
    
    @UiField
    protected TextBox titulo;
    
    @UiField
    protected TextBox autor;
    
    @UiField
    protected TextArea descricao;
    
    @UiField
    protected Label status;
    
    @UiField
    protected FileUpload arquivo;
    
    public EnviarObraView() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    @Override
    public void enviarAtivado(boolean estado) {
        formSubmit.setEnabled(estado);
        titulo.setEnabled(estado);
        autor.setEnabled(estado);
        descricao.setEnabled(estado);
        //arquivo.setEnabled(estado);
    }

    @Override
    public FormPanel getForm() {
        return form;
    }

    @Override
    public void setErro(boolean estado) {
        erro.setVisible(estado);
    }

    @Override
    public void setTrabalhando(boolean estado) {
        carregando.setVisible(estado);
    }
    
    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public String getAutor() {
        return autor.getValue();
    }

    @Override
    public String getDescricao() {
        return descricao.getValue();
    }

    @Override
    public String getTitulo() {
        return titulo.getText();
    }

    @Override
    public HasClickHandlers getSubmit() {
        return formSubmit;
    }
    
    Timer timerLimparStatus = new Timer() {
        @Override
        public void run() {
            setStatus("");
        }
    };
    @Override
    public void setStatus(String status) {
        
        if (!status.isEmpty()) {
            timerLimparStatus.cancel();
            timerLimparStatus.schedule(15000);
        }
        
        this.status.setText(status);
    }
    
    @Override
    public void reset() {
        autor.setText("");
        titulo.setText("");
        descricao.setText("");
    }

}
