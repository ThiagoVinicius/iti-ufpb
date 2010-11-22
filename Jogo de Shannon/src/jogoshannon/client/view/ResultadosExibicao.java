package jogoshannon.client.view;

import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import jogoshannon.client.ModeloResposta;
import jogoshannon.client.presenter.ResultadosApresentador;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.LegendPosition;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.LineChart;

public class ResultadosExibicao extends Composite 
implements ResultadosApresentador.Exibicao {

    private static ResultadosExibicaoUiBinder uiBinder = GWT
            .create(ResultadosExibicaoUiBinder.class);

    interface ResultadosExibicaoUiBinder extends
            UiBinder<Widget, ResultadosExibicao> {
    }
    
    interface TabelaCss extends CssResource {
        String tituloTabela();
        String corpoTabela();
        String primeiraColuna();
        String zeroZero();
        String rotulo_info();
        String celula();
    }

    private static final int LINHA_TITULO = 1;
    private static final int LINHA_BRANCO = 2;
    private static final int OFFSET_LINHA = 3;

    private static final int LEGENDA_COLUNA = 0;
    private static final int OFFSET_COLUNA = 1;

    private static final int ENTROPIA_MIN_END_OFFSET = 2;
    private static final int ENTROPIA_MAX_END_OFFSET = 1;
    
    @UiField 
    TabelaCss style; 
    
    @UiField
    protected FlowPanel conteinerListaUsuarios;
    
    @UiField
    protected FlowPanel listaUsuarios;
    
    @UiField
    protected ListBox listaExperimentos;
    
    @UiField
    protected Anchor mostrarOcultarUsuarios;
    
    @UiField 
    protected Anchor mostrarOcultarTabela;
    
    @UiField
    protected FlexTable tabelaTentativas;
    
    @UiField
    protected Image carregando;
    
    @UiField
    protected SimplePanel painelGrafico;
    
    @UiField
    protected Label contagemIniciados;
    
    @UiField
    protected Label contagemFinalizados;
    
    @UiField
    protected Anchor marcarTodos;
    
    @UiField
    protected Anchor desmarcarTodos;
    
    @UiField
    protected CheckBox distribuirContagens;
    
    private int maxLinha;
    
    private SortedMap<Integer, Double> entropiaMax;
    private SortedMap<Integer, Double> entropiaMin;
    
    public ResultadosExibicao (SimpleEventBus eventos) {
        initWidget(uiBinder.createAndBindUi(this));
        listaExperimentos.addItem("Selecione o experimento");
        atualizarLabelsVisibilidade();
        reset();
    }
    
    @UiHandler({"mostrarOcultarUsuarios", "mostrarOcultarTabela"})
    public void onClick (ClickEvent evt) {
        if (evt.getSource() == mostrarOcultarUsuarios) {
            trocarVisibilidade(conteinerListaUsuarios);
        } else if (evt.getSource() == mostrarOcultarTabela) {
            trocarVisibilidade(tabelaTentativas);
        }
        atualizarLabelsVisibilidade();
    }
    
    private void trocarVisibilidade (Widget onde) {
        boolean orig = onde.isVisible();
        boolean novo = !orig;
        onde.setVisible(novo);
        
    }
    
    private void atualizarLabelsVisibilidade() {
        mostrarOcultarUsuarios.setText(textoVisibilidade(conteinerListaUsuarios.isVisible()));
        mostrarOcultarTabela.setText(textoVisibilidade(tabelaTentativas.isVisible()));
    }
    
    private String textoVisibilidade (boolean visivel) {
        if (visivel == false) {
            return "-mostrar-";
        } else {
            return "-ocultar-";
        }
    }
    
    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void adicionarId(UsuarioWidget widget) {
        listaUsuarios.add(widget);
    }

    private String doubleToString(double numero) {
        return NumberFormat.getFormat("0.000").format(numero);
    }
    
    private void legendar (int linha, String texto) {
        Label legenda = new Label(texto);
        legenda.setStylePrimaryName(style.primeiraColuna());
        legenda.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        tabelaTentativas.setWidget(OFFSET_LINHA + linha, LEGENDA_COLUNA,
                legenda);
    }

    @Override
    public void atualizaEntropiaMinima(int linha, Map<Integer, Double> dados) {
        entropiaMin = new TreeMap<Integer, Double>(dados);
        linha += ENTROPIA_MIN_END_OFFSET;
        legendar(linha, "Entropia Mínima");
        int i = 0;
        for (Double v : entropiaMin.values()) {
            Label texto = new Label(doubleToString(v));
            texto.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            tabelaTentativas.setWidget(OFFSET_LINHA + linha, OFFSET_COLUNA + i,
                    texto);
            ++i;
        }
    }

    @Override
    public void atualizaEntropiaMaxima(int linha, Map<Integer, Double> dados) {
        entropiaMax = new TreeMap<Integer, Double>(dados);
        linha += ENTROPIA_MAX_END_OFFSET;
        legendar(linha, "Entropia Máxima");
        int i = 0;
        for (Double v : entropiaMax.values()) {
            Label texto = new Label(doubleToString(v));
            texto.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            tabelaTentativas.setWidget(OFFSET_LINHA + linha, OFFSET_COLUNA + i,
                    texto);
            ++i;
        }
    }

    @Override
    public void atualizarLinha(int linha, double[] dados) {
        maxLinha = Math.max(maxLinha, linha);
        legendar(linha, ""+(linha+1));
        
        for (int i = 0; i < dados.length; ++i) {
            
            String texto;
            double valor = dados[i];
            boolean exato = Math.rint(valor) == valor;
            if (exato) {
                int valorInt = (int) valor;
                texto = valorInt == 0 ? "" : ""+valorInt;
            } else {
                texto = valor == 0.0d ? "" : doubleToString(valor);
            }
            
            Label label = new Label(texto);
            label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            label.addStyleName(style.celula());
            tabelaTentativas.setWidget(OFFSET_LINHA + linha, OFFSET_COLUNA + i,
                    label);
        }
    }

    @Override
    public void setTitulosTabela(String[] titulos) {
        for (int i = 0; i < titulos.length; ++i) {
            tabelaTentativas.setText(LINHA_TITULO, OFFSET_COLUNA + i,
                    titulos[i]);
        }
        tabelaTentativas.getCellFormatter().setStyleName(LINHA_TITULO, 
                LEGENDA_COLUNA, style.zeroZero());
        
        tabelaTentativas.setText(LINHA_BRANCO, LEGENDA_COLUNA, 
                "Acertos na tentativa");
        tabelaTentativas.getCellFormatter().setStyleName(LINHA_BRANCO, 
                LEGENDA_COLUNA, style.primeiraColuna());
        tabelaTentativas.getCellFormatter().addStyleName(LINHA_BRANCO, 
                LEGENDA_COLUNA, style.celula());
    }
    
    @Override
    public void adicionarExperimento(String nome) {
        if (nome.length() > 53) {
            nome = nome.substring(0, 50).concat("...");
        }
        listaExperimentos.addItem(nome);
    }

    @Override
    public HasChangeHandlers getListaExperimentos() {
        return listaExperimentos;
    }
    
    @Override
    public void reset() {
        listaUsuarios.clear();
        tabelaTentativas.removeAllRows();
        tabelaTentativas.getRowFormatter().addStyleName(LINHA_TITULO, style.tituloTabela());
        tabelaTentativas.addStyleName(style.corpoTabela());
        tabelaTentativas.getCellFormatter().setStyleName(0, 0, style.zeroZero());
        
        //tabelaTentativas.addCell(0); //TODO arumar depois
        tabelaTentativas.setText(0, OFFSET_COLUNA, 
                "Contexto (Número de letras conhecidas ao chutar)");
        tabelaTentativas.getFlexCellFormatter().setColSpan(0, OFFSET_COLUNA, 1000);
        tabelaTentativas.getCellFormatter().setHorizontalAlignment(0, 
                OFFSET_COLUNA, HorizontalPanel.ALIGN_CENTER);
        
        tabelaTentativas.setTitle("Densidade de acertos por tentativa por contexto");
        
        painelGrafico.setVisible(false);
    }

    @Override
    public int getExperimentoSelecionado() {
        return listaExperimentos.getSelectedIndex() - 1;
    }
    
    @Override
    public void setExperimentosCarregando(boolean estaCarregando) {
        carregando.setVisible(estaCarregando);
    }
    
    @Override
    public void plotar () {
        VisualizationUtils.loadVisualizationApi(new Runnable() {
            @Override
            public void run() {
                plotarImpl();
            }
        } , LineChart.PACKAGE);
    }
    
    private void plotarImpl () {
        LineChart.Options opt = LineChart.Options.create();
        opt.setWidth(800);
        opt.setHeight(450);
        opt.setSmoothLine(true);
        opt.setLegend(LegendPosition.RIGHT);
        opt.setEnableTooltip(true);
        opt.setTitleX("Contexto");
        opt.setTitleY("Entropia calculada");
        opt.setMin(0.0);
        opt.setMax(5.0);
        
        DataTable dados = DataTable.create();
        dados.addColumn(ColumnType.STRING, "Letras exibidas");
        dados.addColumn(ColumnType.NUMBER, "Entropia Máxima (jogadores)");
        dados.addColumn(ColumnType.NUMBER, "Entropia Mínima (jogadores)");
        dados.addColumn(ColumnType.NUMBER, "Entropia Máxima (computador)");
        dados.addColumn(ColumnType.NUMBER, "Entropia Mínima (computador)");
        
        Map<Integer, Double> entropiaMaxComp = ModeloResposta.getEntropiaMaximaComputador();
        Map<Integer, Double> entropiaMinComp = ModeloResposta.getEntropiaMinimaComputador();
        
        SortedSet<Integer> chaves = new TreeSet<Integer>(entropiaMaxComp.keySet());
        chaves.addAll(entropiaMin.keySet()); //uniao dos conjuntos
        
        dados.addRows(chaves.size());
        
        int i = 0;
        for (Integer chaveAtual : chaves) {
            dados.setValue(i, 0, ""+chaveAtual);
            
            if (entropiaMax.containsKey(chaveAtual)) {
                dados.setValue(i, 1, entropiaMax.get(chaveAtual));
            }
            if (entropiaMin.containsKey(chaveAtual)) {
                dados.setValue(i, 2, entropiaMin.get(chaveAtual));
            }
            if (entropiaMaxComp.containsKey(chaveAtual)) {
                dados.setValue(i, 3, entropiaMaxComp.get(chaveAtual));
            }
            if (entropiaMinComp.containsKey(chaveAtual)) {
                dados.setValue(i, 4, entropiaMinComp.get(chaveAtual));
            }
            
            ++i;
        }
        
        LineChart grafico = new LineChart(dados, opt);
        painelGrafico.setWidget(grafico);
        painelGrafico.setVisible(true);
        
    }

    @Override
    public void setContagemIniciados(String contagem) {
        contagemIniciados.setText(contagem);
    }

    @Override
    public void setContagemTerminados(String contagem) {
        contagemFinalizados.setText(contagem);
    }

    @Override
    public HasClickHandlers getDesmarcarTodos() {
        return desmarcarTodos;
    }

    @Override
    public HasClickHandlers getMarcarTodos() {
        return marcarTodos;
    }

    @Override
    public boolean getDistribuir() {
        return distribuirContagens.getValue();
    }
    
    public HasClickHandlers getDistribuirWidget() {
        return distribuirContagens;
    }

}
