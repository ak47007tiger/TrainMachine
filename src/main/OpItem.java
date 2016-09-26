package main;
public class OpItem {
		Op op;
		Operand[] operands;
		TM tm;
		public void doOp() {
			switch (op) {
			case Add:
				operands[2].setValue(operands[0].getValue()
						+ operands[1].getValue());
				break;
			case Sub:

				break;
			case Mul:

				break;
			case Dev:

				break;
			case Cmp:
				if (operands[0].getValue() < operands[1].getValue())
					operands[2].setValue(-1);
				else if (operands[0].getValue() == operands[1].getValue()) {
					operands[2].setValue(0);
				} else {
					operands[2].setValue(1);
				}
				break;
			case Mov:
				operands[0].setValue(operands[1].getValue());
				break;
			case Jl:
				if (-1 == operands[0].getValue()) {
					tm.reg_count.val = operands[1].getValue();
					return;
				}
				break;
			case Je:

				break;
			case Jm:

				break;
			case Call:
				tm.systemCall.runCall(operands[0].getValue());
				break;
			}
			tm.reg_count.val++;
			if (tm.reg_count.val == tm.opList.size()) {
				tm.reg_pro_exit.val = TM.EXIT_FLAG;
			}
		}
	}