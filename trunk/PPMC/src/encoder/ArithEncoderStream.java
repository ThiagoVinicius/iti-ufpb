package encoder;

import java.io.DataOutputStream;

public final class ArithEncoderStream {

	static final int CODE_VALUE_BITS = 31;
	//static final long TOP_VALUE = ((long) 1 << CODE_VALUE_BITS) - 1;
        static final long TOP_VALUE = 1;
	static final long FIRST_QUARTER = TOP_VALUE / 4 + 1;
	static final long HALF = 2 * FIRST_QUARTER;
	static final long THIRD_QUARTER = 3 * FIRST_QUARTER;

	long low = 0;
	long high = TOP_VALUE;

	BitOutputStream output;

	int bitsSeguidos = 0;

	public ArithEncoderStream(DataOutputStream out)throws Exception{
		output = new BitOutputStream(out);
	}

	public int flush() throws Exception {
		++bitsSeguidos; // need a final bit (not sure why)
		if (low < FIRST_QUARTER)
			bitPlusFollowFalse();
		else
			bitPlusFollowTrue();

		return output.flush();//flush adiciona zero ate completar um byte
	}

	public void encode(int lowCount, int highCount, int totalCount) throws Exception {
		long range = high - low + 1;
		high = low + (range * highCount) / totalCount - 1;
		low  = low + (range * lowCount) / totalCount;
		while(true) {
			if (high < HALF) {
				bitPlusFollowFalse();
			} else if (low >= HALF) {
				bitPlusFollowTrue();
				low -= HALF;
				high -= HALF;
			} else if (low >= FIRST_QUARTER && high < THIRD_QUARTER) {
				bitsSeguidos++;
				low -= FIRST_QUARTER;
				high -= FIRST_QUARTER;
			} else {
				return;
			}
			low <<= 1;
			high = (high << 1) + 1;
		}
	}

	private void bitPlusFollowTrue() throws Exception {
		output.writeBitTrue();
		while(bitsSeguidos > 0){
			output.writeBitFalse();
			bitsSeguidos--;
		}
	}

	private void bitPlusFollowFalse() throws Exception {
		output.writeBitFalse();
		while(bitsSeguidos > 0){
			output.writeBitTrue();
			bitsSeguidos--;
		}
	}
}
