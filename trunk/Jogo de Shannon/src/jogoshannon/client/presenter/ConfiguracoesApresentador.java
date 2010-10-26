package jogoshannon.client.presenter;

import jogoshannon.client.PedidoEncerramento;
import jogoshannon.client.ProducaoProgramaAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;

public class ConfiguracoesApresentador implements Apresentador {

    public static interface Exibicao {
        
    }
    
    private Exibicao view;
    private HandlerManager eventos;
    private ProducaoProgramaAsync servidor;
    
    public ConfiguracoesApresentador(HandlerManager eventos, Exibicao view,
            ProducaoProgramaAsync servidor) {
        this.view = view;
        this.eventos = eventos;
        this.servidor = servidor;
    }
    
    @Override
    public void vai(HasWidgets pagina) {
        
    }
    
    @Override
    public void encerrar(PedidoEncerramento notaDeFalecimento) {
    }
    
}
