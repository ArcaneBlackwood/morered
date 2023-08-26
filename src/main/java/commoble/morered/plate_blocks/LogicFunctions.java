package commoble.morered.plate_blocks;

import commoble.morered.bitwise_logic.BusLogicFunction;
import commoble.morered.bitwise_logic.BusToSingleFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class LogicFunctions
{
	public static final Int2ObjectMap<LogicFunction> TINTINDEXES = new Int2ObjectOpenHashMap<>();
	public static final Int2ObjectMap<BusLogicFunction> TINTINDEXES_BUS = new Int2ObjectOpenHashMap<>();
	public static final Int2ObjectMap<BusToSingleFunction> TINTINDEXES_BUS_SINGLE = new Int2ObjectOpenHashMap<>();
	
	// we explicitly enumerate these because the model jsons' tintindexes need literal ints and I want to see which ints are for what
	// particles have tintindex 0 for some reason, start counting at 1 so we don't tint particles
	public static final LogicFunction FALSE = registerTintIndexLog(1, (a,b,c) -> false);
	public static final LogicFunction TRUE = registerTintIndexLog(2, (a,b,c) -> true);
	public static final LogicFunction INPUT_A = registerTintIndexLog(3, (a,b,c) -> a);
	public static final LogicFunction INPUT_B = registerTintIndexLog(4, (a,b,c) -> b);
	public static final LogicFunction INPUT_C = registerTintIndexLog(5, (a,b,c) -> c);
	public static final LogicFunction NOT_A = registerTintIndexLog(6, (a,b,c) -> !a);
	public static final LogicFunction NOT_B = registerTintIndexLog(7, (a,b,c) -> !b);
	public static final LogicFunction NOT_C = registerTintIndexLog(8, (a,b,c) -> !c);
	public static final LogicFunction AND = registerTintIndexLog(9, (a,b,c) -> a && b && c);
	public static final LogicFunction NAND = registerTintIndexLog(10, (a,b,c) -> !(a && b && c));
	public static final LogicFunction OR = registerTintIndexLog(11, (a,b,c) -> a || b || c);
	public static final LogicFunction NOR = registerTintIndexLog(12, (a,b,c) -> !(a || b || c));
	// we omit B in the xor functions because the xor/xnor logic plates don't have a B input 
	public static final LogicFunction XOR_AC = registerTintIndexLog(13, (a,b,c) -> a ^ c);
	public static final LogicFunction XNOR_AC = registerTintIndexLog(14, (a,b,c) -> !(a^c));
	public static final LogicFunction MULTIPLEX = registerTintIndexLog(15, (a,b,c) -> b ? c : a);
	// these two are used for intermediary component tinting in the XOR/XNOR gate models
	public static final LogicFunction A_NOR_A_NOR_C = registerTintIndexLog(16, (a,b,c) -> !(a || !(a || c)));
	public static final LogicFunction C_NOR_A_NOR_C = registerTintIndexLog(17, (a,b,c) -> !(c || !(a || c)));
	public static final int SET_LATCH = 18;	// state-memory blocks don't use logic functions
	public static final int UNSET_LATCH = 19;
	// two input AND gate (does not use B input)
	public static final LogicFunction AND_2 = registerTintIndexLog(20, (a,b,c) -> a && c);
	public static final LogicFunction NAND_2 = registerTintIndexLog(21, (a,b,c) -> !(a && c));
	
	//Swapped inputs (a <> b) makes more sense in game, since B is on the left.
	public static final BusLogicFunction ADD = registerTintIndexBus(1, (b,a) -> (char)(a+b));
	public static final BusLogicFunction SUB = registerTintIndexBus(2, (b,a) -> (char)(a-b));
	public static final BusLogicFunction MUL = registerTintIndexBus(3, (b,a) -> (char)(a*b));
	public static final BusLogicFunction DIV = registerTintIndexBus(4, (b,a) -> (char)(b == 0 ? 0 : a/b));
	public static final BusLogicFunction MOD = registerTintIndexBus(5, (b,a) -> (char)(b == 0 ? 0 : a%b));
	public static final BusLogicFunction SHIFT_UP = registerTintIndexBus(7, (b,a) -> (char)(a>>b));
	public static final BusLogicFunction SHIFT_DOWN = registerTintIndexBus(8, (b,a) -> (char)(a<<b));
	public static final BusLogicFunction POW = registerTintIndexBus(6, (b,a) -> {
		char result = 1;
		for (int i = 1; i <= b; i++)
		   result *= a;
		return result;
	});
	
	public static final BusToSingleFunction EQL = registerTintIndexSing(1, (a, b, c) -> (a==c) ? 16 : 0);
	public static final BusToSingleFunction NEQ = registerTintIndexSing(2, (a, b, c) -> (a!=c) ? 16 : 0);
	public static final BusToSingleFunction GRE = registerTintIndexSing(3, (a, b, c) -> (c>a) ? 16 : 0);
	public static final BusToSingleFunction LES = registerTintIndexSing(4, (a, b, c) -> (c<a) ? 16 : 0);
	public static final BusToSingleFunction BAND = registerTintIndexSing(6, (a, b, c) -> (b!=65535) ? 16 : 0); //All bit one, == 2^16-1
	public static final BusToSingleFunction BOR = registerTintIndexSing(5, (a, b, c) -> (b!=0) ? 16 : 0); //Any bit one
	public static final BusToSingleFunction BXOR = registerTintIndexSing(7, (a, b, c) -> {
		boolean state = false;
		for (int i=0; i<16; i++)
			state = state ^ (((b >> i) & 1) == 1);
		return state ? 16 : 0;
	});

	public static LogicFunction registerTintIndexLog(int index, LogicFunction function)
	{
		TINTINDEXES.put(index, function);
		return function;
	}
	public static BusLogicFunction registerTintIndexBus(int index, BusLogicFunction function)
	{
		TINTINDEXES_BUS.put(index, function);
		return function;
	}
	public static BusToSingleFunction registerTintIndexSing(int index, BusToSingleFunction function)
	{
		TINTINDEXES_BUS_SINGLE.put(index, function);
		return function;
	}
}
