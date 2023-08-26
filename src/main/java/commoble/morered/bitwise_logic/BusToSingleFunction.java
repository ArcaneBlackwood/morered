package commoble.morered.bitwise_logic;

@FunctionalInterface
public interface BusToSingleFunction {
	/**
	 * 
	 * @param a The first input for a gate, 90 degrees clockwise from the output
	 * @param b The second input for a gate, 270 degrees clockwise from the output
	 * @return Output value
	 */
	public int apply(char a, char b, char c);
}
