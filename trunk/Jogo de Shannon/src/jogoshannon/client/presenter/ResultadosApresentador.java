package jogoshannon.client.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jogoshannon.client.ModeloResposta;
import jogoshannon.client.PedidoEncerramento;
import jogoshannon.client.event.UsuarioCheckBoxEvent;
import jogoshannon.client.event.UsuarioCheckBoxHandler;
import jogoshannon.client.remote.JuizSoletrandoAsync;
import jogoshannon.client.util.ConjuntoUsuarios;
import jogoshannon.client.view.UsuarioWidget;
import jogoshannon.shared.CobaiaStub;
import jogoshannon.shared.ExperimentoStub;
import jogoshannon.shared.Tentativas;
import jogoshannon.shared.UsuarioNaoEncontradoException;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
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

        void adicionarId(UsuarioWidget widget);
        
        void adicionarExperimento(String nome);
        
        HasChangeHandlers getListaExperimentos ();
        
        int getExperimentoSelecionado ();

        void setExperimentosCarregando (boolean carregando);

        void atualizarLinha(int linha, int dados[]);

        void setTitulosTabela(String titulos[]);

        void atualizaEntropiaMaxima(int linha, double[] dados);

        void atualizaEntropiaMinima(int linha, double[] dados);

        void plotar();
        
        void setContagemIniciados(String contagem);
        
        void setContagemTerminados(String contagem);
        
        HasClickHandlers getMarcarTodos();
        
        HasClickHandlers getDesmarcarTodos();
        
    }

    private SimpleEventBus eventos;
    private Exibicao view;
    private JuizSoletrandoAsync servidor;
    private ModeloResposta entropia;
    private ExperimentoStub experimentos[];
    private ExperimentoStub experimentoAtual;
    private int contagemFinalizados;
    private ConjuntoUsuarios conjUsuarios;

    public ResultadosApresentador(SimpleEventBus eventos, Exibicao view,
            JuizSoletrandoAsync servidor) {
        this.eventos = eventos;
        this.view = view;
        this.servidor = servidor;
        conjUsuarios = new ConjuntoUsuarios(eventos);
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

        eventos.addHandler(UsuarioCheckBoxEvent.TIPO,
                new UsuarioCheckBoxHandler() {
                    @Override
                    public void onCheckboxMudou(UsuarioCheckBoxEvent evento) {
                        // TODO remover ao clicar em remover;
                        // removerId(evento.getOrigem().getId());
                        if (evento.getOrigem().getCheckboxMarcada() == false) {
                            desconsiderarId(evento.getOrigem().getId());
                        } else {
                            reconsiderarId(evento.getOrigem().getId());
                        }
                    }
                });
        
        view.getDesmarcarTodos().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                desconsiderarTodos();
            }
        });
        
        view.getMarcarTodos().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                reconsiderarTodos();
            }
        });
        
    }

    private void adicionarId(final List<Long> requisitar) {
        for (long id : requisitar) {
            
            UsuarioWidget novo = new UsuarioWidget(id);
            novo.ativaBotaoRemover(true);
            boolean foi = conjUsuarios.adicionaWidget(novo);
            if (foi) {
                view.adicionarId(novo);
                novo.setCarregando(true);
            }
        }

        servidor.getResultados(requisitar, new AsyncCallback<CobaiaStub[]>() {
            @Override
            public void onSuccess(CobaiaStub[] resultado) {
                for (CobaiaStub cada : resultado) {
                    
                    conjUsuarios.adicionaCobaia(cada);
                    UsuarioWidget visualizacaoUsuario = conjUsuarios.getWidget(cada.getId());
                    visualizacaoUsuario.setCarregando(false);
                    visualizacaoUsuario.setCheckboxMarcada(true);
                    
                    Tentativas adicionar[] = cada.getDesafios().
                            toArray(new Tentativas[0]); 
                    adicionaResultado(adicionar);
                    if (adicionar.length == 0) {
                        visualizacaoUsuario.setInfo("Não completou o experimento");
                        visualizacaoUsuario.setCheckboxVisivel(false);
                    } else {
                        ++contagemFinalizados;
                    }
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
                    desconsiderarId(e.getId());
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
        view.setContagemTerminados(""+contagemFinalizados);
        entropia.calculaEntropia();
        int max = entropia.getLinhaCount();
        for (int i = 0; i < max; ++i) {
            view.atualizarLinha(i, entropia.getLinha(i));
        }
        view.atualizaEntropiaMaxima(max, entropia.getEntropiaMaxima());
        view.atualizaEntropiaMinima(max, entropia.getEntropiaMinima());
        view.plotar();
    }

    private void desconsiderarId(long id) {
        // view.removerId(id);
        // view.limparIdAdicionar();
        CobaiaStub paraRemover = conjUsuarios.getCobaia(id);
        UsuarioWidget representacaoGrafica = conjUsuarios.getWidget(id);
        if (paraRemover != null && representacaoGrafica != null) {
            entropia.remove(paraRemover.getDesafios().toArray(new Tentativas[0]));
            representacaoGrafica.setCheckboxMarcada(false);
            atualizaTabelas();
        }
        
        //entropia.remove()
    }
    
    private void reconsiderarId (long id) {
        CobaiaStub paraRemover = conjUsuarios.getCobaia(id);
        UsuarioWidget representacaoGrafica = conjUsuarios.getWidget(id);
        if (paraRemover != null && representacaoGrafica != null) {
            entropia.adiciona(paraRemover.getDesafios().toArray(new Tentativas[0]));
            representacaoGrafica.setCheckboxMarcada(true);
            atualizaTabelas();
        }
    }
    
    private void desconsiderarTodos () {
        for (UsuarioWidget w : conjUsuarios.getTodosWidgets()) {
            w.setCheckboxMarcada(false);
        }
        entropia = new ModeloResposta(experimentoAtual);
        atualizaTabelas();
    }
    
    private void reconsiderarTodos () {
        for (UsuarioWidget w : conjUsuarios.getTodosWidgets()) {
            w.setCheckboxMarcada(true);
        }
        entropia = new ModeloResposta(experimentoAtual);
        for (CobaiaStub c : conjUsuarios.getTodasCobaias()) {
            entropia.adiciona(c.getDesafios().toArray(new Tentativas[0]));
        }
        atualizaTabelas();
    }
    
    private void doExperimentoMudou () {
        experimentoAtual = experimentos[view.getExperimentoSelecionado()];
        conjUsuarios.limparTudo();
        view.reset();
        entropia = new ModeloResposta(experimentoAtual);
        preparaTitulos();
        contagemFinalizados = 0;
        List<Long> requisitar = new ArrayList<Long>(experimentoAtual.getIdCobaias());
        Collections.sort(requisitar);
        adicionarId(requisitar);
        view.setContagemIniciados(""+requisitar.size());
        view.setContagemTerminados(" - indisponível - ");
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
