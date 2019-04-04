package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import cfg.build.ASTFactory;
import cfg.build.VtseCFG;
import cfg.build.opt.ShortcutCFG;

/**
 * @author va
 *
 */
public class Test {

	static String FLOAT_CDFPL = "./benchmark/float-cdfpl-func";
	static String KRATOS = "./benchmark/kratos";
	static String ECA_RERS = "./benchmark/eca-rers2012";
	static String EXAMPLE = "./benchmark/example";
	
	public static void  main(String[] args) throws FileNotFoundException {
//		ASTFactory ast = new ASTFactory(KRATOS + "/token_ring.10.c");
//		ASTFactory ast = new ASTFactory(FLOAT_CDFPL + "./newton_1_1_true_unreach_call.c");
		ASTFactory ast = new ASTFactory(EXAMPLE + "/example_4.c");
		
		VtseCFG cfg = new VtseCFG(ast.getFunction(0), ast);
		ShortcutCFG cfgopt = new ShortcutCFG(cfg);
//		cfg.unfold();
		cfgopt.fillPreviousNode(cfg.getStart(), 0);
		cfgopt.scanCFG(cfg.getExit());
		cfgopt.checkFillPrevious(cfg.getStart(), 0);
		
		
//		ast.print();
		//cfg.printBoundary();
			
//		cfg.index();
//		cfg.printGraph();
//		cfg.printMeta();
//		cfg.getVm().printList();
//		System.out.println("number of variable: " + cfg.getVm().getSize()+ " in cfg, "+ 
//						 cfg.getVm().getTotalEmplement() + " in formula");
		
//		cfg.printSMTFormula(System.out);

		
		
	}
}
