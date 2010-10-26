package jogoshannon.client;

import jogoshannon.client.presenter.Apresentador;
import jogoshannon.client.presenter.EnviarObraApresentador;
import jogoshannon.client.presenter.PrincipalApresentador;
import jogoshannon.client.presenter.ResultadosApresentador;
import jogoshannon.client.view.EnviarObraView;
import jogoshannon.client.view.PrincipalExibicao;
import jogoshannon.client.view.ResultadosExibicao;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

public class ControladorAplicacao implements Apresentador,
        ValueChangeHandler<String> {

    private HasWidgets pagina;
    private Apresentador atual;
    private String tokenAtual;
    
    private class PedidoEncerramentoImpl implements PedidoEncerramento {
        public boolean obrigatorio;
        public boolean encerrar = true;
        public PedidoEncerramentoImpl (boolean obrigatorio) {
            this.obrigatorio = obrigatorio;
        }
        @Override
        public boolean getEncerramentoObrigatorio() {
            return obrigatorio;
        }
        @Override
        public void impossivelEncerrarAgora() {
            encerrar = false;
        }
    }

    public ControladorAplicacao() {
        amarra();
    }

    private void amarra() {
        History.addValueChangeHandler(this);
    }
    
    public static void mudarToken (String novoToken) {
        Jogo_de_Shannon.getApresentadorChefe().mudarTokenImpl(novoToken);
    }
    
    private void mudarTokenImpl (String novoToken) {
        History.newItem(novoToken, false);
        tokenAtual = novoToken;
    }

    @Override
    public void vai(HasWidgets pagina) {
        this.pagina = pagina;

        if ("".equals(History.getToken())) {
            History.newItem("jogar");
        } else {
            History.fireCurrentHistoryState();
        }
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        String token = event.getValue();
        
        if (token != null) {
            Apresentador oEscolhido = null;
            HandlerManager eventos = new HandlerManager(null);

            if (token.equals("jogar")) {
                oEscolhido = new PrincipalApresentador(
                        eventos,
                        new PrincipalExibicao(), 
                        Jogo_de_Shannon.getJuizSoletrando(),
                        0L);
            } else if (token.startsWith("jogar/")) {
                Long exp = new Long(token.substring(6).trim());
                oEscolhido = new PrincipalApresentador(
                        eventos,
                        new PrincipalExibicao(), 
                        Jogo_de_Shannon.getJuizSoletrando(),
                        exp);
            }

            else if (token.equals("resultados")) {
                oEscolhido = new ResultadosApresentador(
                        eventos,
                        new ResultadosExibicao(eventos), 
                        Jogo_de_Shannon.getJuizSoletrando());
            }
            
            else if (token.equals("enviar")) {
                oEscolhido = new EnviarObraApresentador(
                        new EnviarObraView(),
                        Jogo_de_Shannon.getProducaoPrograma());
            }

            if (oEscolhido != null) {
                despachar(oEscolhido, token);
            }

        }

    }
    
    private void despachar(Apresentador oEscolhido, String novoToken) {
        
        if (atual != null) {
            PedidoEncerramentoImpl vaiCooperar = new PedidoEncerramentoImpl(false);
            atual.encerrar(vaiCooperar);
            if (vaiCooperar.encerrar == false) {
                History.newItem(tokenAtual, false);
                return;
            }
        }
        
        atual = oEscolhido;
        tokenAtual = novoToken;
        oEscolhido.vai(pagina);
    }

    @Override
    public void encerrar(PedidoEncerramento notaDeFalecimento) {
        //nada tenho a fazer.
    }

}
