package jogoshannon.server.persistent;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

public class RegistroRodadas implements Externalizable {
    
    /**
     * Numero de versao, utilizado no processo de leitura e escrita de
     * objetos.
     * <p/>
     * Somente deve ser alterado quando os metodos de entrada e saida forem
     * atualizados
     */
    private static final long serialVersionUID = 1;
    
    private List<Rodada> rodadas;

    public List<Rodada> getRodadas() {
        if (rodadas == null) {
            rodadas = new ArrayList<Rodada>();
        }
        return rodadas;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        
        long ver = in.readLong();
        if (ver == 1L) {
            rodadas = (List<Rodada>) in.readObject();
        } else {
            throw new IOException("Numero de versao("+ver+") desconhecido!");
        }
        
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(serialVersionUID);
        out.writeObject(rodadas);
    }
    
    public RegistroRodadas copiaRasa() {
        RegistroRodadas result = new RegistroRodadas();
        result.rodadas = this.rodadas;
        return result;
    }
    
}
