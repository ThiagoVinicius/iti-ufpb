package jogoshannon.client.presenter;

import jogoshannon.client.ModeloResposta;
import jogoshannon.client.PedidoEncerramento;
import jogoshannon.client.event.UsuarioRemovidoEvent;
import jogoshannon.client.event.UsuarioRemovidoHandler;
import jogoshannon.client.remote.JuizSoletrandoAsync;
import jogoshannon.shared.ExperimentoStub;
import jogoshannon.shared.Tentativas;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
    }

    private HandlerManager eventos;
    private Exibicao view;
    private JuizSoletrandoAsync servidor;
    private ModeloResposta entropia;
    private ExperimentoStub experimentos[];
    private ExperimentoStub experimentoAtual;

    public ResultadosApresentador(HandlerManager eventos, Exibicao view,
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
    
    private void postInit() {
        preparaTitulos();
        amarrar();
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

    private void adicionarId(final long id) {
        view.adicionarId(id);
        view.setCarregandoId(id, true);

        servidor.getResultados(id, new AsyncCallback<Tentativas[]>() {
            @Override
            public void onSuccess(Tentativas[] resultado) {
                view.setCarregandoId(id, false);
                adicionaResultado(resultado);
            }

            @Override
            public void onFailure(Throwable caught) {
                removerId(id);
            }
        });

    }

    private void adicionaResultado(Tentativas resultado[]) {
        entropia.adiciona(resultado);
        atualizaTabelas();
    }

    private void atualizaTabelas() {
        entropia.calculaEntropia();
        int max = entropia.getLinhaCount();
        for (int i = 0; i < max; ++i) {
            view.atualizarLinha(i, entropia.getLinha(i));
        }
        view.atualizaEntropiaMaxima(max, entropia.getEntropiaMaxima());
        view.atualizaEntropiaMinima(max, entropia.getEntropiaMinima());
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
        for (Long id : experimentoAtual.getIdCobaias()) {
            adicionarId(id);
        }
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
