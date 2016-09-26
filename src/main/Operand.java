package main;
public class Operand {
		public static final int IMMEDIATE_TYPE = 0;
		public static final int REG_TYPE = 1;
		int type;
		int val;
		Reg reg;
		static final String REG_PATTERN = "reg[0-9]+";
		static final String IMMEDIATE_PATTERN = "[0-9]+";

		public void setValue(int val) {
			if (type == IMMEDIATE_TYPE) {
				this.val = val;
			} else if (type == REG_TYPE) {
				this.reg.val = val;
			}
		}

		public int getValue() {
			if (type == IMMEDIATE_TYPE) {
				return val;
			} else if (type == REG_TYPE) {
				return reg.val;
			}
			return -1;
		}
	}