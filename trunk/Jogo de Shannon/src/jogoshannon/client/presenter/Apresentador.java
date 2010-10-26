package jogoshannon.client.presenter;

import jogoshannon.client.PedidoEncerramento;

import com.google.gwt.user.client.ui.HasWidgets;

public abstract interface Apresentador {
    public void vai(HasWidgets pagina);
    
    public void encerrar(PedidoEncerramento notaDeFalecimento);
}
