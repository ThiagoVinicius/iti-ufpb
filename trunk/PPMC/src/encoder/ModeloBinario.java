package encoder;

public final class ModeloBinario {

	private static final char LIMITE = 2000;

	private final int[] tabela = new int[3];//usada como retorno pra nao criar nova tabela toda vez

	private int kmax;

	private int leitura = 0;
	public int leituraLength = 0;

	private int[] BASES;
	private char[] contadores;//todos comecam com 1
	//char sao 2bytes sem sinal

	public int simbolo;//0 ou 1

	public ModeloBinario(int k){
		kmax = k;
		BASES = new int[kmax+1];
		for (int i = 0; i <= kmax; i++)
			BASES[i] = (1 << (i+1)) - 2; //2^(k+1) - 2

		contadores = new char[ (1 << (kmax+2)) - 2 ]; //tamanho total  2^(kmax+2) - 2

		//preencher tudo com 1
		for (int i = 0; i < contadores.length; i++)
			contadores[i] = 1;
	}

	public void addSimbToLeitura() {
		leitura = (leitura << 1) | simbolo;
		leituraLength++;
		if (leituraLength > kmax) {
			leituraLength = kmax;
		}
	}

	public void atualizaModelo(){

		int k = kmax;

		if(k < 0)return;

		if(k > leituraLength)
			k = leituraLength;

		int and = (1 << k) - 1;

		int nivel = leitura & and;

		//enderecamento base + deslocamento do nivel
		//multiplica por 2 pq cada posicao tem 2 contadores
		int end0 = BASES[k] + (nivel << 1);		//endereco do contador do 0
		int end1 = end0+1;						//endereco do contador do 1
		int endS = end0+simbolo;				//endereco do contador do simbolo

		contadores[endS]++;
	}

	public int[] getTabela(int k) {
		if (k > leituraLength)
			return null;

		int and = (1 << k) - 1;

		int nivel = leitura & and;

		// enderecamento base + deslocamento do nivel
		// multiplica por 2 pq cada posi�ao tem 2 contadores
		int end0 = BASES[k] + (nivel << 1); // endereco do contador do 0
		int end1 = end0 + 1; // endereco do contador do 1

		int cont0 = contadores[end0];//ignorar bit de sinal
		int cont1 = contadores[end1];//ignorar bit de sinal

		//tabela[0] = 0;//nao precisa pq eh sempre zero
		tabela[1] = cont0;
		tabela[2] = cont0 + cont1;
		return tabela;
	}
}
