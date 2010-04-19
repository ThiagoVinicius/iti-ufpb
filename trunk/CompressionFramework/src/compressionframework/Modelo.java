/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package compressionframework;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Janduy
 */
public abstract class Modelo {

    public abstract void codifica(String letra) throws IOException;

    public abstract void decodifica(String letra) throws IOException;

    public abstract void setInput(DataInputStream input);

    public abstract void setOutput(DataOutputStream output);

    public void inicioDaDecodificacao(String letra) throws IOException{}

    public void fimDaCodificacao() throws IOException{}
}
