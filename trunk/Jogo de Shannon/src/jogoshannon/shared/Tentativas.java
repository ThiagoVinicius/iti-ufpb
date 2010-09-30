package jogoshannon.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Tentativas implements Serializable {

    public int contagens[];

    @SuppressWarnings("unused")
    private Tentativas() {
        this(0);
    }

    public Tentativas(int dados[]) {
        this(dados.length);

        // equivalente a:
        // contagens = dados.clone();
        for (int i = 0; i < contagens.length; ++i) {
            contagens[i] = dados[i];
        }
    }

    public Tentativas(int contagemDesafios) {
        contagens = new int[contagemDesafios];
    }

}
