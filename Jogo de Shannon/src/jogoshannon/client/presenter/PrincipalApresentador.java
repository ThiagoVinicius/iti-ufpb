package jogoshannon.client.presenter;

import jogoshannon.client.JuizSoletrandoAsync;
import jogoshannon.client.ModeloJogoDeShannon;
import jogoshannon.client.event.FraseCompletaEvent;
import jogoshannon.client.event.FraseCompletaEventHandler;
import jogoshannon.client.event.JogoCompletoEvent;
import jogoshannon.client.event.JogoCompletoEventHandler;
import jogoshannon.client.event.TentativaEvent;
import jogoshannon.client.event.TentativaEventHandler;
import jogoshannon.client.util.VerificadorDeCampo;
import jogoshannon.client.view.PrincipalExibicao.EstadosServidor;
import jogoshannon.shared.Frase;
import jogoshannon.shared.SessaoInvalidaException;

import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class PrincipalApresentador implements Apresentador {
	
	public interface Exibicao {
		HasKeyUpHandlers getCampoResposta();
		void ignoraLetra();
		void setDesafio(String frase);
		void setTextoErro(String erro);
		char getResposta();
		void limparResposta();
		void setCarregando (boolean estado);
		void setEstadoServidor (EstadosServidor estadoAtual);
		void exibeFimDeJogo(String titulo, String texto);
		void setTextoParabens (String texto);
		void setId (String id);
		Widget asWidget();
	}
	
	Exibicao view;
	HandlerManager eventos;
	ModeloJogoDeShannon jogoDeShannon;
	JuizSoletrandoAsync servidor;
	
	static final String MENSAGEM_SESSAO_EXPIRADA = 
		"Muito tempo se passou desde sua última visita - não será possível " +
		"continuar. Você terá que recomeçar o jogo, recarregando a página." +
		"Desculpe o incomodo.";
	
	static final String MENSAGEM_ERRO_CONEXAO = 
		"Estamos com dificuldades técnicas: não foi " +
		"possível comunicar-se com o servidor. Tente " +
		"recarregar a página. Se este erro persistir, " +
		"Tente novamente mais tarde.";
	
	int idfail = 0;
	int frasesfail = 0;
	
	private void bind () {
		
		view.getCampoResposta().addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				doRespostaMudou(event);
			}
		});
		
		eventos.addHandler(FraseCompletaEvent.TIPO, new FraseCompletaEventHandler() {
			@Override
			public void onFraseCompletaEvent(FraseCompletaEvent event) {
				doFraseCompleta();
			}
		});
		
		eventos.addHandler(TentativaEvent.TIPO, new TentativaEventHandler() {
			@Override
			public void onTentativaEvent(TentativaEvent evento) {
				doTentativa(evento.getCorreta(), evento.getLetra());
			}
		});
		
		eventos.addHandler(JogoCompletoEvent.TIPO, new JogoCompletoEventHandler() {
			@Override
			public void onJogoCompletoEvent(JogoCompletoEvent evento) {
				doFimDeJogo();
			}
		});
		
	}
	
	private void enviarTentativas () {
		
		servidor.atualizaTentativas(jogoDeShannon.getTodasTentativas(), new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof SessaoInvalidaException) {
					mostrarSessaoExpirada();
				} else {
					enviarTentativas();
				}
			}
			@Override
			public void onSuccess(Void result) {
				destruirSessao();
				view.setEstadoServidor(EstadosServidor.TUDO_CERTO);
			}
		});

	}
	
	private void destruirSessao () {
		Cookies.removeCookie("JSESSIONID");
	}
	
	private void enviar (char tentativa) {
		jogoDeShannon.atualiza(tentativa, eventos);
		view.setDesafio(jogoDeShannon.getFraseParcial());
	}
	
	private void doFraseCompleta () {
		view.setTextoParabens("Muito bem, você acertou! Tente esta nova frase.");
	}
	
	private void doRespostaMudou (KeyUpEvent evento) {
		
		//view.ignoraLetra();
		char tentativa = view.getResposta();
		view.limparResposta();
		
		if (!VerificadorDeCampo.letraValida(tentativa)) {
			view.setTextoErro("Digite apenas letras ou espaços.");
			return;
		}
		
		tentativa = VerificadorDeCampo.normalizaLetra(tentativa);
		
		enviar(tentativa);

	}
	
	private void doTentativa (boolean correta, char letra) {
		if (correta) {
			view.setTextoErro("");
		} else {
			view.setTextoErro( letra + " - Resposta errada.");
		}
	}
	
	private void doFimDeJogo () {
		enviarTentativas();
		view.exibeFimDeJogo("Fim de jogo",
				"Parabéns, você concluiu o jogo!\n" +
				"Obrigado pela sua participacao.\n" +
				"(para jogar novamente, recarregue a página)");
	}
	
	public PrincipalApresentador (HandlerManager eventos, Exibicao view, JuizSoletrandoAsync servidor) {
		this.view = view;
		this.eventos = eventos;
		this.servidor = servidor;
		//this.jogoDeShannon = new ModeloJogoDeShannon(new Frase[] {});
		
	}
	
	@Override
	public void vai(HasWidgets pagina) {
		bind();
		pegarId();
		pagina.clear();
		pagina.add(view.asWidget());
	}
	
	private void pegarId () {
		servidor.getId(new AsyncCallback<Long>() {
			public void onSuccess(Long result) {
				view.setId(result.toString());
				baixarFrases();
			}
			public void onFailure(Throwable caught) {
				++idfail;
				if (idfail > 5) {
					mostrarErroConexao();
				} else {
					pegarId();
				}
			}
		});
	}

	private void baixarFrases() {
		servidor.getFrases(new AsyncCallback<Frase[]>() {
			@Override
			public void onSuccess(Frase[] result) {
				jogoDeShannon = new ModeloJogoDeShannon(result);
				view.setCarregando(false);
				view.setDesafio(jogoDeShannon.getFraseParcial());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof SessaoInvalidaException) {
					mostrarSessaoExpirada();
				} else {
					++idfail;
					if (idfail > 5) {
						mostrarErroConexao();
					} else {
						baixarFrases();
					}
				}
			}
		});
	}
	
	private void mostrarSessaoExpirada () {
		view.exibeFimDeJogo("Sessão expirou.", MENSAGEM_SESSAO_EXPIRADA);
	}
	
	private void mostrarErroConexao () {
		view.exibeFimDeJogo("Problemas com a conexão", MENSAGEM_ERRO_CONEXAO);
	}

}
