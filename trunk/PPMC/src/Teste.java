
import java.util.HashSet;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrador
 */
public class Teste {
    public static void main(String[] args) {
        HashSet<Character> conjunto = new HashSet<Character>();
        conjunto.add('a');
        System.out.println(conjunto.toString());
        conjunto.add('b');
        System.out.println(conjunto.toString());
        conjunto.add('c');
        System.out.println(conjunto.toString());
        conjunto.add('a');
        System.out.println(conjunto.toString());
        conjunto.add('d');
        System.out.println(conjunto.toString());
        conjunto.add('b');
        System.out.println(conjunto.toString());
    }
}
