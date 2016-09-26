package main;
public class SystemCall {
		static final int print = 0;
		TM tm;
		public void runCall(int functionCode) {
			System.out.println("functionCode:" + functionCode);
			switch (functionCode) {
			case print:
				System.out.println(tm.regs[0].val);
				break;
			}
		}
	}