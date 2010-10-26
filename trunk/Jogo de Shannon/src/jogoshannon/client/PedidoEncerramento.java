package jogoshannon.client;

public interface PedidoEncerramento {
    
    public void impossivelEncerrarAgora();
    
    /**
     * @return <code>true</code>, se o apresentador sera encerrado, independente
     *         da chamada {@link PedidoEncerramento#impossivelEncerrarAgora()}
     *         ser feita ou não. 
     */
    public boolean getEncerramentoObrigatorio();

}
