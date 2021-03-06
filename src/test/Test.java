package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import cfg.build.ASTFactory;
import cfg.build.VtseCFG;

/**
 * @author va
 *
 */
public class Test {

	static String FLOAT_CDFPL = "./benchmark/floats-cdfpl-func";
	static String KRATOS = "./benchmark/kratos/loop_10";
	static String ECA_RERS = "./benchmark/eca-rers2012";
	static String EXAMPLE = "./benchmark/example";
	
	public static void  main(String[] args) throws FileNotFoundException {
		//ASTFactory ast = new ASTFactory("./kratos/transmitter.12.c");
//		ASTFactory ast = new ASTFactory(FLOAT_CDFPL + "./newton_1_1_true_unreach_call.c");
		ASTFactory ast = new ASTFactory(EXAMPLE + "/example_4.c");
		VtseCFG cfg = new VtseCFG(ast.getFunction(0), ast);
//		ast.print();
		//cfg.printBoundary();
		cfg.unfold();
//		cfg.index();
		cfg.printGraph();
		//cfg.printMeta();
//		cfg.getVm().printList();
//		System.out.println("number of variable: " + cfg.getVm().getSize()+ " in cfg, "+ 
//						 cfg.getVm().getTotalEmplement() + " in formula");
		
//		cfg.printSMTFormula(System.out);

		
		
	}
}
