package decoder;

import java.io.DataInputStream;

public final class ArithDecoderStream {

	static final int CODE_VALUE_BITS = 31;
	static final long TOP_VALUE = ((long) 1 << CODE_VALUE_BITS) - 1;
	static final long FIRST_QUARTER = TOP_VALUE / 4 + 1;
	static final long HALF = 2 * FIRST_QUARTER;
	static final long THIRD_QUARTER = 3 * FIRST_QUARTER;

	long low = 0;
	long high = TOP_VALUE;

	BitInputStream input;
	long valor = 0;

	public ArithDecoderStream(DataInputStream in)throws Exception{
		input = new BitInputStream(in);
		for (int i = 1; i <= CODE_VALUE_BITS; i++) {
			bufferBit();
		}
	}
	
	public int getCurrentSymbolCount(int totalCount) {
		return (int) (((valor - low + 1) * totalCount - 1) / (high - low + 1));
	}

	public void removeSymbolFromStream(long lowCount, long highCount, long totalCount) throws Exception {
		long range = high - low + 1;
		high = low + (range * highCount) / totalCount - 1;
		low = low + (range * lowCount) / totalCount;
		while (true) {
			if (high < HALF) {
				// no effect
			} else if (low >= HALF) {
				valor -= HALF;
				low -= HALF;
				high -= HALF;
			} else if (low >= FIRST_QUARTER && high < THIRD_QUARTER) {
				valor -= FIRST_QUARTER;
				low -= FIRST_QUARTER;
				high -= FIRST_QUARTER;
			} else {
				return;
			}
			low <<= 1; // = 2 * _low; 	    // _low <<= 1;
			high = (high << 1) + 1; // 2 * _high + 1; //	    _high = (_high<<1) + 1;
			bufferBit();
		}
	}

	private void bufferBit() throws Exception {
		valor = (valor<<1);
		if(input.readBit())
			valor++;
	}

}
