package commoble.morered;

import java.util.Arrays;

import net.minecraft.world.item.DyeColor;
import net.minecraft.Util;

public class ObjectNames
{
	// blocks and blockitems
	public static final String DIODE = "diode";
	public static final String NOT_GATE = "not_gate";
	public static final String NOR_GATE = "nor_gate";
	public static final String NAND_GATE = "nand_gate";
	public static final String OR_GATE = "or_gate";
	public static final String AND_GATE = "and_gate";
	public static final String XOR_GATE = "xor_gate";
	public static final String XNOR_GATE = "xnor_gate";
	public static final String MULTIPLEXER = "multiplexer";
	public static final String AND_2_GATE = "and_2_gate";
	public static final String NAND_2_GATE = "nand_2_gate";

	public static final String LATCH = "latch";
	public static final String PULSE_GATE = "pulse_gate";
	
	public static final String STONE_PLATE = "stone_plate";
	
	public static final String SOLDERING_TABLE = "soldering_table";
	
	public static final String REDWIRE_POST = "redwire_post";
	public static final String REDWIRE_POST_PLATE = "redwire_post_plate";
	public static final String REDWIRE_POST_RELAY_PLATE = "redwire_post_relay_plate";
	
	public static final String BUNDLED_CABLE_POST = "bundled_cable_post";
	public static final String BUNDLED_CABLE_RELAY_PLATE = "bundled_cable_relay_plate";

	public static final String MATRIX_INPUT = "matrix_input";
	public static final String MATRIX_INPUT_ENTITY = "matrix_input_entity";
	
	public static final String WIRE = "wire";
	public static final String RED_ALLOY_WIRE = "red_alloy_wire";
	public static final String COLORED_NETWORK_CABLE = "colored_network_cable";
	public static final String[] NETWORK_CABLES = Util.make(new String[16], array -> Arrays.setAll(array, i -> DyeColor.values()[i] + "_network_cable"));
	public static final String BUNDLED_NETWORK_CABLE = "bundled_network_cable";
	
	public static final String HEXIDECRUBROMETER = "hexidecrubrometer";
	
	public static final String BITWISE_LOGIC_PLATE = "bitwise_logic_plate";
	public static final String BITWISE_DIODE = "bitwise_diode";
	public static final String BITWISE_NOT_GATE = "bitwise_not_gate";
	public static final String BITWISE_OR_GATE = "bitwise_or_gate";
	public static final String BITWISE_AND_GATE = "bitwise_and_gate";
	public static final String BITWISE_XOR_GATE = "bitwise_xor_gate";
	public static final String BITWISE_XNOR_GATE = "bitwise_xnor_gate";
	
	public static final String ARITHMETIC_ADD_GATE = "arithmetic_add_gate";
	public static final String ARITHMETIC_SUB_GATE = "arithmetic_sub_gate";
	public static final String ARITHMETIC_MUL_GATE = "arithmetic_mul_gate";
	public static final String ARITHMETIC_DIV_GATE = "arithmetic_div_gate";
	public static final String ARITHMETIC_MOD_GATE = "arithmetic_mod_gate";
	public static final String ARITHMETIC_POW_GATE = "arithmetic_pow_gate";
	public static final String ARITHMETIC_SQRT_GATE = "arithmetic_sqrt_gate";
	public static final String ARITHMETIC_SHIFT_UP_GATE = "arithmetic_shift_up_gate";
	public static final String ARITHMETIC_SHIFT_DOWN_GATE = "arithmetic_shift_down_gate";

	public static final String BITWISE_ANALOG_LOGIC_PLATE = "bitwise_to_analog_logic_plate";
	
	public static final String ARITHMETIC_BITS_AND_GATE = "arithmetic_bits_and_gate";
	public static final String ARITHMETIC_BITS_OR_GATE = "arithmetic_bits_or_gate";
	public static final String ARITHMETIC_BITS_XOR_GATE = "arithmetic_bits_xor_gate";
	public static final String ARITHMETIC_EQL_GATE = "arithmetic_bits_eql_gate";
	public static final String ARITHMETIC_NEQ_GATE = "arithmetic_bits_neq_gate";
	public static final String ARITHMETIC_GRE_GATE = "arithmetic_bits_gre_gate";
	public static final String ARITHMETIC_LES_GATE = "arithmetic_bits_les_gate";
	

	// raw items
	public static final String RED_ALLOY_INGOT = "red_alloy_ingot";
	public static final String REDWIRE_SPOOL = "redwire_spool";
	public static final String BUNDLED_CABLE_SPOOL = "bundled_cable_spool";
	
	// tags
	public static final String REDWIRE_POSTS = "redwire_posts";
	public static final String BUNDLED_CABLE_POSTS = "bundled_cable_posts";
	public static final String RED_ALLOY_WIRES = "red_alloy_wires";
	public static final String COLORED_NETWORK_CABLES = "colored_network_cables";
	
	// capabilities
	public static final String POSTS_IN_CHUNK = "posts_in_chunk";
	
	// recipe types
	public static final String SOLDERING_RECIPE = "soldering";
	
	// loot functions
	public static final String WIRE_COUNT = "set_wire_count";
	
	// model loaders
	public static final String WIRE_PARTS = "wire_parts";
	public static final String ROTATE_TINTS = "rotate_tints";
}
