package jogoshannon.client.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jogoshannon.client.ModeloResposta;
import jogoshannon.client.PedidoEncerramento;
import jogoshannon.client.event.UsuarioRemovidoEvent;
import jogoshannon.client.event.UsuarioRemovidoHandler;
import jogoshannon.client.remote.JuizSoletrandoAsync;
import jogoshannon.shared.CobaiaStub;
import jogoshannon.shared.ExperimentoStub;
import jogoshannon.shared.Tentativas;
import jogoshannon.shared.UsuarioNaoEncontradoException;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ResultadosApresentador implements Apresentador {

    public static interface Exibicao {
        Widget asWidget();
        
        void reset();

        void adicionarId(long id);
        
        void adicionarExperimento(String nome);
        
        HasChangeHandlers getListaExperimentos ();
        
        int getExperimentoSelecionado ();

        void setCarregandoId(long id, boolean carregando);
        
        void setExperimentosCarregando (boolean carregando);

        void removerId(long id);

        void atualizarLinha(int linha, int dados[]);

        void setTitulosTabela(String titulos[]);

        void atualizaEntropiaMaxima(int linha, double[] dados);

        void atualizaEntropiaMinima(int linha, double[] dados);

        void plotar();
    }

    private SimpleEventBus eventos;
    private Exibicao view;
    private JuizSoletrandoAsync servidor;
    private ModeloResposta entropia;
    private ExperimentoStub experimentos[];
    private ExperimentoStub experimentoAtual;

    public ResultadosApresentador(SimpleEventBus eventos, Exibicao view,
            JuizSoletrandoAsync servidor) {
        this.eventos = eventos;
        this.view = view;
        this.servidor = servidor;
        //this.entropia = new ModeloResposta();

        //preparaTitulos();
        amarrar();
        asyncInit();
    }
    
    private void asyncInit() {
        servidor.getExperimentos(new AsyncCallback<ExperimentoStub[]>() {
            @Override
            public void onSuccess(ExperimentoStub[] result) {
                experimentos = result;
                for (ExperimentoStub es : result) {
                    view.adicionarExperimento(es.getDescricao());
                }
                view.setExperimentosCarregando(false);
            }
            
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Ooops. Erro ao comunicar com o servidor");
            }

        });
    }

    private void preparaTitulos() {
        String titulos[] = new String[entropia.getEntropiaMaxima().length];
        for (int i = 0; i < titulos.length; ++i) {
            titulos[i] = Integer.toString(experimentoAtual.getMostrarLetras().get(i));
        }
        this.view.setTitulosTabela(titulos);
    }

    private void amarrar() {
        
        view.getListaExperimentos().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                doExperimentoMudou();
            }
        });

        eventos.addHandler(UsuarioRemovidoEvent.TIPO,
                new UsuarioRemovidoHandler() {
                    @Override
                    public void onUsuarioRemovido(UsuarioRemovidoEvent evento) {
                        // TODO remover ao clicar em remover;
                        // removerId(evento.getOrigem().getId());
                    }
                });
    }

    private void adicionarId(final List<Long> requisitar) {
        for (long id : requisitar) {
            view.adicionarId(id);
            view.setCarregandoId(id, true);
        }

        servidor.getResultados(requisitar, new AsyncCallback<CobaiaStub[]>() {
            @Override
            public void onSuccess(CobaiaStub[] resultado) {
                for (CobaiaStub cada : resultado) {
                    view.setCarregandoId(cada.getId(), false);
                    adicionaResultado(cada.getDesafios().
                            toArray(new Tentativas[0]));
                }
                atualizaTabelas();
                
                if (resultado.length != requisitar.size()) {
                    adicionarId(requisitar.subList(resultado.length-1, requisitar.size()));
                }
                
            }

            @Override
            public void onFailure(Throwable caught) {
                try {
                    throw caught; 
                } catch (UsuarioNaoEncontradoException e) {
                    requisitar.remove(e.getId());
                    removerId(e.getId());
                } catch (InvocationException e) {
                    Window.alert(e.getMessage());
                } catch (Throwable e) {
                }
            }
        });

    }

    private void adicionaResultado(Tentativas resultado[]) {
        entropia.adiciona(resultado);
    }

    private void atualizaTabelas() {
        entropia.calculaEntropia();
        int max = entropia.getLinhaCount();
        for (int i = 0; i < max; ++i) {
            view.atualizarLinha(i, entropia.getLinha(i));
        }
        view.atualizaEntropiaMaxima(max, entropia.getEntropiaMaxima());
        view.atualizaEntropiaMinima(max, entropia.getEntropiaMinima());
        view.plotar();
    }

    private void removerId(long id) {
        view.removerId(id);
        // view.limparIdAdicionar();
    }
    
    private void doExperimentoMudou () {
        experimentoAtual = experimentos[view.getExperimentoSelecionado()];
        view.reset();
        entropia = new ModeloResposta(experimentoAtual);
        preparaTitulos();
        List<Long> requisitar = new ArrayList<Long>(experimentoAtual.getIdCobaias());
        Collections.sort(requisitar);
        adicionarId(requisitar);
    }

    @Override
    public void vai(HasWidgets pagina) {
        pagina.clear();
        pagina.add(view.asWidget());
    }
    
    @Override
    public void encerrar(PedidoEncerramento notaDeFalecimento) {
    }

}
