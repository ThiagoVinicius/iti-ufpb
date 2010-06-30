package jogoshannon.client.presenter;

import jogoshannon.client.JuizSoletrandoAsync;
import jogoshannon.client.ModeloJogoDeShannon;
import jogoshannon.client.event.FraseCompletaEvent;
import jogoshannon.client.event.FraseCompletaEventHandler;
import jogoshannon.client.event.JogoCompletoEvent;
import jogoshannon.client.event.JogoCompletoEventHandler;
import jogoshannon.client.event.TentativaEvent;
import jogoshannon.client.event.TentativaEventHandler;
import jogoshannon.client.view.PrincipalExibicao.EstadosServidor;
import jogoshannon.shared.Frase;
import jogoshannon.shared.SessaoInvalidaException;
import jogoshannon.shared.VerificadorDeCampo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class PrincipalApresentador implements Apresentador {
	
	public interface Exibicao {
		HasKeyPressHandlers getCampoResposta();
		void ignoraLetra();
		void setDesafio(String frase);
		void setTextoErro(String erro);
		char getResposta();
		void setResposta(char r);
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
	
	int enviosPendentes;
	int ponteiroFrasesEnviadas;
	
	int totalFrases;
	int ponteiroFrasesBaixadas;
	
	int idfail = 0;
	
	private void bind () {
		
		view.getCampoResposta().addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
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
				doTentativa(evento.getCorreta());
			}
		});
		
		eventos.addHandler(JogoCompletoEvent.TIPO, new JogoCompletoEventHandler() {
			@Override
			public void onJogoCompletoEvent(JogoCompletoEvent evento) {
				doFimDeJogo();
			}
		});
		
	}
	
	private boolean fimDeJogo () {
		return ponteiroFrasesEnviadas >= totalFrases; 
	} 
	
	private void enviarTentativas (final int id) {
		
		servidor.atualizaTentativas(id, jogoDeShannon.getTentativas(id), new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof SessaoInvalidaException) {
					mostrarSessaoExpirada();
				} else {
					enviarTentativas(id);
				}
			}
			@Override
			public void onSuccess(Void result) {
				--enviosPendentes;
				if (enviosPendentes == 0) {
					
					if (fimDeJogo()) {
						destruirSessao();
					} else {
						view.setEstadoServidor(EstadosServidor.TUDO_CERTO);
					}
					
				}
			}
		});
	}
	
	private void enviarTentativas () {
		++enviosPendentes;
		view.setEstadoServidor(EstadosServidor.AGUARDANDO_RESPOSTA);
		int memoria = ponteiroFrasesEnviadas;
		++ponteiroFrasesEnviadas;
		enviarTentativas(memoria);
	}
	
	private void destruirSessao () {
		servidor.destruirSessao(new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				Cookies.removeCookie("JSESSIONID");
				view.setEstadoServidor(EstadosServidor.TUDO_CERTO);
			}
			@Override
			public void onSuccess(Void result) {
				Cookies.removeCookie("JSESSIONID");
				view.setEstadoServidor(EstadosServidor.TUDO_CERTO);
			}
		});
		
	}
	
	private void enviar () {
		char tentativa = view.getResposta();
		jogoDeShannon.atualiza(tentativa, eventos);
		view.setDesafio(jogoDeShannon.getFraseParcial());
	}
	
	private void doFraseCompleta () {
		view.setTextoParabens("Muito bem, você acertou! Tente esta nova frase.");
		enviarTentativas();
	}
	
	private void doRespostaMudou (KeyPressEvent evento) {
		
		//NativeEvent evt2 = evento.;
		if (evento.isAnyModifierKeyDown() || evento.getCharCode() < 32) {
			return; //não devo me importar com controles
		}
		
		view.ignoraLetra();
		char tentativa = evento.getCharCode();
		
		if (!VerificadorDeCampo.letraValida(tentativa)) {
			view.setTextoErro("Digite apenas letras ou espaços.");
			return;
		}
		
		tentativa = VerificadorDeCampo.normalizaLetra(tentativa);
		view.setResposta(tentativa);
		
		enviar();

	}
	
	private void doTentativa (boolean correta) {
		view.setTextoParabens("");
		if (correta) {
			view.setTextoErro("");
		} else {
			view.setTextoErro("Resposta errada.");
		}
	}
	
	private void doFimDeJogo () {
		if (ponteiroFrasesBaixadas >= totalFrases) {
			view.exibeFimDeJogo("Fim de jogo",
					"Parabéns, você concluiu o jogo!\n" +
					"Obrigado pela sua participacao.\n" +
					"(para jogar novamente, recarregue a página)");
		} else {
			view.setCarregando(true);
		}
	}
	
	public PrincipalApresentador (HandlerManager eventos, Exibicao view, JuizSoletrandoAsync servidor) {
		this.view = view;
		this.eventos = eventos;
		this.servidor = servidor;
		this.jogoDeShannon = new ModeloJogoDeShannon(new Frase[] {
//				new Frase("FRASE DE TESTEFRASE DE TESTEFRASE DE TESTE" +
//						  "FRASE DE TESTEFRASE DE TESTEFRASE DE TESTE" +
//						  "FRASE DE TESTEFRASE DE TESTEFRASE DE TESTE" +
//						  "FRASE DE TESTEFRASE DE TESTEFRASE DE TESTE" +
//						  "FRASE DE TESTEFRASE DE TESTEFRASE DE TESTE"), 
//				new Frase("SEGUNDA_FRASE_DE_TESTE")
		});
		
	}
	
	@Override
	public void vai(HasWidgets pagina) {
		bind();
		pegarId();
		//baixarFrases();
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
				GWT.log("Falhou uma!");
				if (idfail > 5) {
					view.exibeFimDeJogo("Desculpe.", 
							"Estamos com dificuldades técnicas: não foi " +
							"possível comunicar-se com o servidor. Tente " +
							"recarregar a página. Se este erro persistir, " +
							"Tente novamente mais tarde.");
				} else {
					pegarId();
				}
			}
		});
	}
	

	private void baixarFrases() {
		servidor.getTotalFrases(new AsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer result) {
				totalFrases = result;
				baixarFrasesMesmo();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof SessaoInvalidaException) {
					mostrarSessaoExpirada();
				} else {
					baixarFrases();
				}
			}
		});
	}
	
	private void baixarFrasesMesmo () {
		
		if (ponteiroFrasesBaixadas >= totalFrases) {
			return;
		}
		
		servidor.getFrase(ponteiroFrasesBaixadas, new AsyncCallback<Frase>() {
			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof SessaoInvalidaException) {
					mostrarSessaoExpirada();
				} else {
					baixarFrasesMesmo();
				}
			}
			@Override
			public void onSuccess(Frase result) {
				view.setCarregando(false);
				jogoDeShannon.adicionaFrase(result);
				view.setDesafio(jogoDeShannon.getFraseParcial());
				++ponteiroFrasesBaixadas;
				baixarFrasesMesmo();
			}
			
		});
	}
	
	private void mostrarSessaoExpirada () {
		view.exibeFimDeJogo("Sessão expirou.", MENSAGEM_SESSAO_EXPIRADA);
	}

}
