package jogoshannon.server.persistent;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

public class Rodada implements Externalizable {

    private static final long serialVersionUID = 1L;
    
    private int tentativas[];
    
    public Rodada() {
    }
    
    public Rodada(int numeroDesafios) {
        tentativas = new int[numeroDesafios];
    }

    public Rodada(int tentativas[]) {
        this.tentativas = tentativas;
    }

    public int[] getTentativas() {
        return tentativas;
    }

    public void setTentativas(int tentativas[]) {
        this.tentativas = tentativas;
    }

    public void somaTentativas(int somarCom[]) {
        for (int i = 0; i < somarCom.length; ++i) {
            tentativas[i] += somarCom[i];
        }
        // tocando na variável, dando a dica de que ela precisa ser salva.
        setTentativas(tentativas);
    }

    @Override
    public String toString() {
        return Arrays.toString(getTentativas());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {

        long ver = in.readLong();
        if (ver == 1L) {
            tentativas = (int[]) in.readObject();
        } else {
            throw new IOException("Numero de versao("+ver+") desconhecido!");
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(serialVersionUID);
        out.writeObject(tentativas);
    }
    
}
