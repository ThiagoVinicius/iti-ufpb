package jogoshannon.client.presenter;

import jogoshannon.client.JuizSoletrandoAsync;
import jogoshannon.client.ModeloJogoDeShannon;
import jogoshannon.client.event.FraseCompletaEvent;
import jogoshannon.client.event.FraseCompletaHandler;
import jogoshannon.client.event.JogoCompletoEvent;
import jogoshannon.client.event.JogoCompletoHandler;
import jogoshannon.client.event.TentativaEvent;
import jogoshannon.client.event.TentativaHandler;
import jogoshannon.client.util.VerificadorDeCampo;
import jogoshannon.shared.DadosJogo;
import jogoshannon.shared.SessaoInvalidaException;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class PrincipalApresentador implements Apresentador {

    public interface Exibicao {
        // HasKeyUpHandlers getCampoResposta();
        HasValueChangeHandlers<Character> getTeclado();

        void setDesafio(String frase);

        void setTextoErro(String erro);

        void setCarregando(boolean estado);

        void setEstadoServidor(PrincipalApresentador.EstadosServidor estadoAtual);
        
        void setEstadoServidor(PrincipalApresentador.EstadosServidor estadoAtual, String info);

        void exibeFimDeJogo(String titulo, String texto);

        void setTextoParabens(String texto);

        void setId(String id);

        void desativaTecla(char tecla);

        void ativaTodasTeclas();

        Widget asWidget();
    }

    public static enum EstadosServidor {
        AGUARDANDO_RESPOSTA, TUDO_CERTO, SEM_ALTERACOES;
    }

    Exibicao view;
    HandlerManager eventos;
    ModeloJogoDeShannon jogoDeShannon;
    JuizSoletrandoAsync servidor;
    Long idExperimento;

    static final String MENSAGEM_SESSAO_EXPIRADA = "Muito tempo se passou desde sua última visita - não será possível "
            + "continuar. Você terá que recomeçar o jogo, recarregando a página."
            + "Desculpe o incomodo.";

    static final String MENSAGEM_ERRO_CONEXAO = "Estamos com dificuldades técnicas: não foi "
            + "possível comunicar-se com o servidor. Tente "
            + "recarregar a página. Se este erro persistir, "
            + "Tente novamente mais tarde.";
    
    static final int MAX_ENVIO_DELAY = 30000;

    int idfail = 0;
    int frasesfail = 0;
    int enviofail = 0;
    int envioDelay = 1;

    private void bind() {

        view.getTeclado().addValueChangeHandler(
                new ValueChangeHandler<Character>() {
                    @Override
                    public void onValueChange(ValueChangeEvent<Character> event) {
                        doRespostaMudou(event.getValue());
                    }
                });

        eventos.addHandler(FraseCompletaEvent.TIPO, new FraseCompletaHandler() {
            @Override
            public void onFraseCompletaEvent(FraseCompletaEvent event) {
                doFraseCompleta();
            }
        });

        eventos.addHandler(TentativaEvent.TIPO, new TentativaHandler() {
            @Override
            public void onTentativaEvent(TentativaEvent evento) {
                doTentativa(evento.getCorreta(), evento.getLetra());
            }
        });

        eventos.addHandler(JogoCompletoEvent.TIPO, new JogoCompletoHandler() {
            @Override
            public void onJogoCompletoEvent(JogoCompletoEvent evento) {
                doFimDeJogo();
            }
        });

    }

    Timer enviaTimer = new Timer() {
        @Override
        public void run() {
            enviarTentativasTimed();
        }
    };
    
    private void enviarTentativas () {
        enviaTimer.cancel();
        enviaTimer.schedule(envioDelay);
        view.setEstadoServidor(EstadosServidor.AGUARDANDO_RESPOSTA, 
                Integer.toString(envioDelay/1000));
        if (envioDelay == 1) {
            envioDelay = 1000;
        }else {
            envioDelay *= 2;
            envioDelay = Math.min(envioDelay, MAX_ENVIO_DELAY);
        }
    }
    
    private void enviarTentativasTimed() {

        servidor.atualizaTentativas(jogoDeShannon.getTodasTentativas(),
                new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        if (enviofail == 6) {
                            mostrarFimJogo("\nGrrr... O servidor está demorando " +
                            		"demais para responder.\nDados para coleta" +
                            		" manual:\n"+jogoDeShannon.strWriteTentativas());
                        }
                        
                        if (caught instanceof SessaoInvalidaException) {
                            mostrarSessaoExpirada();
                        } else {
                            ++enviofail;
                            enviarTentativas();
                        }
                    }

                    @Override
                    public void onSuccess(Void result) {
                        destruirSessao();
                        view.setEstadoServidor(PrincipalApresentador.EstadosServidor.TUDO_CERTO);
                    }
                });

    }

    private void destruirSessao() {
        Cookies.removeCookie("JSESSIONID");
    }

    private void enviar(char tentativa) {
        jogoDeShannon.atualiza(tentativa, eventos);
        view.setDesafio(jogoDeShannon.getFraseParcial());
    }

    private void doFraseCompleta() {
        view.setTextoParabens("Muito bem, você acertou! Tente esta nova frase.");
    }

    private void doRespostaMudou(char tentativa) {

        if (!VerificadorDeCampo.letraValida(tentativa)) {
            view.setTextoErro("Digite apenas letras ou espaços.");
            return;
        }

        view.desativaTecla(tentativa);
        tentativa = VerificadorDeCampo.normalizaLetra(tentativa);

        enviar(tentativa);

    }

    private void doTentativa(boolean correta, char letra) {
        if (correta) {
            view.setTextoErro("");
            view.ativaTodasTeclas();
        } else {
            view.setTextoErro(letra + " - Resposta errada.");
        }
    }

    private void doFimDeJogo() {
        view.setEstadoServidor(PrincipalApresentador.EstadosServidor.AGUARDANDO_RESPOSTA);
        mostrarFimJogo("");
        enviarTentativas();
    }

    public PrincipalApresentador(HandlerManager eventos, Exibicao view,
            JuizSoletrandoAsync servidor, Long expId) {
        this.view = view;
        this.eventos = eventos;
        this.servidor = servidor;
        this.idExperimento = expId;
    }

    @Override
    public void vai(HasWidgets pagina) {
        bind();
        pegarId();
        pagina.clear();
        pagina.add(view.asWidget());
    }

    private void pegarId() {
        servidor.getFrases(idExperimento, new AsyncCallback<DadosJogo>() {
            public void onSuccess(DadosJogo result) {
                History.newItem("jogar/"+result.idExperimento, false);
                view.setId(""+result.idUsuario);
                jogoDeShannon = new ModeloJogoDeShannon(result);
                view.setCarregando(false);
                view.setDesafio(jogoDeShannon.getFraseParcial());
                //baixarFrases();
            }

            public void onFailure(Throwable caught) {
                if (caught instanceof SessaoInvalidaException) {
                    mostrarSessaoExpirada();
                } else {
                    ++idfail;
                    if (idfail > 5) {
                        mostrarErroConexao();
                    } else {
                        pegarId();
                    }
                }
            }
        });
    }

    private void mostrarSessaoExpirada() {
        view.exibeFimDeJogo("Sessão expirou.", MENSAGEM_SESSAO_EXPIRADA);
    }

    private void mostrarErroConexao() {
        view.exibeFimDeJogo("Problemas com a conexão", MENSAGEM_ERRO_CONEXAO);
    }
    
    private void mostrarFimJogo (String extra) {
        
        String msg = "Parabéns, você concluiu o jogo!\n" 
        + "Obrigado pela sua participacao.\n"
        + "(para jogar novamente, recarregue a página)";
        
        msg += extra;
        
        view.exibeFimDeJogo("Fim de jogo", msg);
    }

}
