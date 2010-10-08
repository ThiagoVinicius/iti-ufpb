package jogoshannon.client.presenter;

import jogoshannon.client.JuizSoletrandoAsync;
import jogoshannon.client.ModeloResposta;
import jogoshannon.client.event.UsuarioRemovidoEvent;
import jogoshannon.client.event.UsuarioRemovidoHandler;
import jogoshannon.shared.ExperimentoStub;
import jogoshannon.shared.Tentativas;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ResultadosApresentador implements Apresentador {

    public static interface Exibicao {
        Widget asWidget();

        HasClickHandlers botaoAdicionar();

        long getIdAdicionar();

        void adicionarId(long id);

        void setCarregandoId(long id, boolean carregando);

        void limparIdAdicionar();

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

    public ResultadosApresentador(HandlerManager eventos, Exibicao view,
            JuizSoletrandoAsync servidor) {
        this.eventos = eventos;
        this.view = view;
        this.servidor = servidor;
        //this.entropia = new ModeloResposta();

        //preparaTitulos();
        //amarrar();
    }
    
    private void asyncInit() {
        servidor.getExperimentoStub(0L, new AsyncCallback<ExperimentoStub>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Falha ao comunicar com o servidor");
            }

            @Override
            public void onSuccess(ExperimentoStub result) {
                entropia = new ModeloResposta(result);
                postInit();
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
            titulos[i] = Integer.toString(entropia.getEntropiaMaxima().length);
        }
        this.view.setTitulosTabela(titulos);
    }

    private void amarrar() {
        view.botaoAdicionar().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                adicionarIdPeloCampo();
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

    private void adicionarIdPeloCampo() {
        final long id = view.getIdAdicionar();
        adicionarId(id);
        view.limparIdAdicionar();
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

    @Override
    public void vai(HasWidgets pagina) {
        pagina.clear();
        pagina.add(view.asWidget());
    }

}
