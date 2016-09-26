package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class TM {

	public static final int EXIT_FLAG = 0;
	static final int CONTINUE_FLAG = 1;

	List<OpItem> opList = new LinkedList<OpItem>();

	Op[] ops = { Op.Add, Op.Sub, Op.Mul, Op.Dev, Op.Cmp, Op.Mov, Op.Jl, Op.Je,
			Op.Jm, Op.Call };

	String[] strOps = { "Add", "Sub", "Mul", "Dev", "Cmp", "Mov", "Jl", "Je",
			"Jm", "Call" };

	Reg[] regs = { new Reg(), new Reg(), new Reg(), new Reg() };
	Reg reg_count = new Reg();
	Reg reg_pro_exit = new Reg();

	String[] strRegs = { "reg0", "reg1", "reg2", "reg3" };

	public TM() {
	}

	public static void main(String[] args) {
		TM tm = new TM();
		tm.start();
		File userDir = new File(System.getProperty("user.dir"));
		File codeFile = new File(userDir, "files/machine_code_simple.txt");
		System.out.println(codeFile.getPath());
		try {
			tm.runFromFile(codeFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Operand getOperand(String strOprand) {
		Operand operand = new Operand();
		int type = getOperandType(strOprand);
		operand.type = type;
		switch (type) {
		case Operand.IMMEDIATE_TYPE:
			operand.val = Integer.parseInt(strOprand);
			break;
		case Operand.REG_TYPE:
			int index = 0;
			while (!strOprand.equals(strRegs[index])) {
				index++;
			}
			operand.reg = regs[index];
			break;
		}
		return operand;
	}

	OpItem getOpItem(String strCommand) {
		OpItem opItem;

		String strOp = strCommand.substring(0, strCommand.indexOf(' '));
		int index = 0;
		while (!strOp.equals(strOps[index])) {
			index++;
		}
		opItem = new OpItem();
		opItem.op = ops[index];

		String[] operands = strCommand.substring(strCommand.indexOf(' ') + 1)
				.split(",");
		opItem.operands = new Operand[operands.length];
		for (index = 0; index < operands.length; index++) {
			opItem.operands[index] = getOperand(operands[index]);
		}
		opItem.tm = this;
		return opItem;
	}

	public void loadOps(File codeFile) throws IOException {
		InputStreamReader inr = new InputStreamReader(new FileInputStream(
				codeFile));
		BufferedReader br = new BufferedReader(inr);
		String strCommand = br.readLine();
		OpItem opItem;
		while (null != strCommand) {
			// empty line
			if (strCommand.length() == 0) {
				strCommand = br.readLine();
				continue;
			}
			opItem = getOpItem(strCommand);
			opList.add(opItem);
			strCommand = br.readLine();
		}
		br.close();
	}

	public void runFromFile(File codeFile) throws IOException {
		loadOps(codeFile);
		doOps();
	}

	void doOps() {
		reg_pro_exit.val = CONTINUE_FLAG;
		reg_count.val = 0;
		while (CONTINUE_FLAG == reg_pro_exit.val) {
			OpItem opItem = opList.get(reg_count.val);
			opItem.doOp();
		}
	}

	void printRegs() {
		for (int i = 0; i < regs.length; i++) {
			System.out.print(String.format("reg[%d]:%d - ", i, regs[i].val));
		}
		System.out.println(String.format("exit?:%s, count:%d",
				reg_pro_exit.val == EXIT_FLAG ? "exit" : "continue",
				reg_count.val));
	}

	public static int getOperandType(String strOprand) {
		if (strOprand.matches(Operand.REG_PATTERN)) {
			return Operand.REG_TYPE;
		} else if (strOprand.matches(Operand.IMMEDIATE_PATTERN)) {
			return Operand.IMMEDIATE_TYPE;
		}
		return -1;
	}

	SystemCall systemCall;

	public void start() {
		systemCall = new SystemCall();
		systemCall.tm = this;
	}
}