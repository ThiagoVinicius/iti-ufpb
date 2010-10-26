package jogoshannon.client.presenter;

import jogoshannon.client.PedidoEncerramento;
import jogoshannon.client.ProducaoProgramaAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

public class EnviarObraApresentador implements Apresentador {
    
    public static interface Exibicao {
        FormPanel getForm();
        void setTrabalhando(boolean estado);
        void setErro(boolean estado);
        void enviarAtivado(boolean estado);
        String getTitulo();
        String getAutor();
        String getDescricao();
        HasClickHandlers getSubmit();
        void setStatus(String status);
        void reset();
        Widget asWidget();
    }
    
    private Exibicao view;
    private ProducaoProgramaAsync servidor;
    
    public EnviarObraApresentador (Exibicao view, ProducaoProgramaAsync servidor) {
        this.view = view;
        this.servidor = servidor;
    }
    
    private void amarrar () {
        view.getForm().addSubmitHandler(new SubmitHandler() {
            @Override
            public void onSubmit(SubmitEvent event) {
                doSubmit();
            }
        });
        view.getForm().addSubmitCompleteHandler(new SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(SubmitCompleteEvent event) {
                doSubmitComplete(event);
            }
        });
        view.getSubmit().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                prepararFormulario();
            }
        });
    }
    
    protected void doSubmitComplete(SubmitCompleteEvent event) {
        if (event.getResults() == null) {
            view.enviarAtivado(true);
            view.setTrabalhando(false);
            view.setErro(true);
            view.setStatus("A última operacao falhou.");
        } else {
            view.setStatus("Enviado com sucesso!");
            resetaFormulario();
        }
        
    }

    protected void doSubmit() {
        view.enviarAtivado(false);
        view.setTrabalhando(true);
        view.setErro(false);
    }
    
    private void prepararFormulario() {
        view.enviarAtivado(false);
        view.setTrabalhando(true);
        view.setErro(false);
        
        servidor.getUploadUrl(view.getTitulo(), view.getAutor(), 
                view.getDescricao(), new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                view.getForm().setAction(result);
                view.getForm().submit();
            }
            @Override
            public void onFailure(Throwable caught) {
                History.fireCurrentHistoryState();
            }
        });
    }
    
    private void resetaFormulario () {
        view.reset();
        view.enviarAtivado(true);
        view.setTrabalhando(false);
        view.setErro(false);
    }

    @Override
    public void vai(HasWidgets pagina) {
        amarrar();
        resetaFormulario();
        pagina.clear();
        pagina.add(view.asWidget());
    }
    
    @Override
    public void encerrar(PedidoEncerramento notaDeFalecimento) {
        // TODO Auto-generated method stub
    }
    
}
