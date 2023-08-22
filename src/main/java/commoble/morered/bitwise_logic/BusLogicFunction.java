package commoble.morered.bitwise_logic;

import commoble.morered.plate_blocks.LogicFunction;

/**
 * Bus logic 
 */
@FunctionalInterface
public interface BusLogicFunction {
	/**
	 * 
	 * @param a The first input for a gate, 90 degrees clockwise from the output
	 * @param b The second input for a gate, 270 degrees clockwise from the output
	 * @return Output value
	 */
	public char apply(char a, char b);
	
	
	static BusLogicFunction wrapSingle(LogicFunction source) {
		return (a, b) -> {
			char out = 0;
			for (int i=0; i<16; i++) {
				boolean outputBit = source.apply(false, ((a >> i) & 1) == 1, false);
				if (outputBit)
					out = (char)(out | (1 << i)); 
			}
			return out;
		};
	}
	static BusLogicFunction wrapDouble(LogicFunction source) {
		return (a, b) -> {
			char out = 0;
			for (int i=0; i<16; i++) {
				boolean outputBit = source.apply(((a >> i) & 1) == 1, false, ((b >> i) & 1) == 1);
				if (outputBit)
					out = (char)(out | (1 << i)); 
			}
			return out;
		};
	}
}
