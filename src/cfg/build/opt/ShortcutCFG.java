package cfg.build.opt;

import java.util.ArrayList;

import cfg.build.VtseCFG;
import cfg.node.BeginNode;
import cfg.node.CFGNode;
import cfg.node.DecisionNode;
import cfg.node.EmptyNode;
import cfg.node.EndConditionNode;
import cfg.node.EndNode;
import cfg.node.GotoNode;
import cfg.node.IterationNode;

/**
 * @author thuynt
 *
 */

public class ShortcutCFG {
	private VtseCFG cfgOld;
	private VtseCFG cfgNew;
	private ArrayList<CFGNode> conjunction;
	private ArrayList<int[]> levelManager;
	public ShortcutCFG(){		
	}
	
	public ShortcutCFG(VtseCFG cfg){
		this.cfgOld = cfg;
		this.conjunction = new ArrayList<CFGNode>();
		this.levelManager = new ArrayList<int[]>();
		this.cfgNew = shortenGFG();
	}
	
	public VtseCFG getCfgOld() {
		return cfgOld;
	}
	
	public void setCfgOld(VtseCFG cfgOld) {
		this.cfgOld = cfgOld;
	}
	
	public VtseCFG getCfgNew() {
		return cfgNew;
	}
	
	public void setCfgNew(VtseCFG cfgNew) {
		this.cfgNew = cfgNew;
	}
	
	private static void printSpace(int level) {
		for (int i = 0; i < level; i++) {
			System.out.print(" ");
		}
	}
	
	/**
	 * thu thuat cho phep duyet nguoc do thi luong dieu khien 
	 */
	public void fillPreviousNode(CFGNode start){
		CFGNode iter = start;
		if (iter == null) {			
			return;
		} else if (iter instanceof DecisionNode) {
			CFGNode thenNode = ((DecisionNode) iter).getThenNode();
			CFGNode elseNode = ((DecisionNode) iter).getElseNode();			
			if (thenNode != null) {
				thenNode.addPrevious(iter);
				fillPreviousNode(thenNode);
			}			
			if (elseNode != null){
				elseNode.addPrevious(iter);
				fillPreviousNode(elseNode);
			}				
		}  else if (iter instanceof GotoNode) {	
			if (iter.getNext() != null) iter.getNext().addPrevious(iter);
			fillPreviousNode(((GotoNode) iter).getNext());
		} else if (iter instanceof IterationNode) {
			if (iter.getNext() != null) {
				iter.getNext().addPrevious(iter);
				fillPreviousNode(iter.getNext());
			}
			else return;
		} else if (iter instanceof EmptyNode) {			
			if (iter.getNext() != null)iter.getNext().addPrevious(iter);
			fillPreviousNode(iter.getNext());
		} else if (iter instanceof EndConditionNode) {
			return;
		} else if (iter instanceof BeginNode)  {
			if (iter.getNext() != null) iter.getNext().addPrevious(iter);
			fillPreviousNode(iter.getNext());
			
			((BeginNode) iter).getEndNode().addPrevious(iter);
			CFGNode endNode = ((BeginNode) iter).getEndNode();
			if (endNode.getNext() != null) endNode.getNext().addPrevious(endNode);
			fillPreviousNode(((BeginNode) iter).getEndNode().getNext());
		} else if (iter instanceof EndNode) {
			if (iter.getNext() != null) iter.getNext().addPrevious(iter);
			fillPreviousNode(iter.getNext());
		}
		else {			
			if (iter.getNext() != null) {
				iter.getNext().addPrevious(iter);
			}
			fillPreviousNode(iter.getNext());
		}	
	}	
	
	public void checkFillPrevious(CFGNode start, int level){
		CFGNode iter = start;
		printSpace(level);
		if (iter == null) {
//			System.out.println(iter.get);
			return;
		} else if (iter instanceof DecisionNode) {
			System.out.print(" --- " + iter.getPrevious().size() + " --- ");
			iter.printNode();			
//			System.out.println(iter.getFormula());
			printSpace(level);
			System.out.println("Then Clause: ");
			if (((DecisionNode) iter).getThenNode() != null) {
				checkFillPrevious(((DecisionNode) iter).getThenNode(), level + 7);
			}
			//printSpace(level);
			System.out.println("Else Clause: ");
			if (((DecisionNode) iter).getElseNode() != null)
				checkFillPrevious(((DecisionNode) iter).getElseNode(),  level + 7);		
		} else if (iter instanceof GotoNode) {
			System.out.print(" --- " + iter.getPrevious().size() + " --- ");
			iter.printNode();
			//printSpace(level);
			//((GotoNode) iter).getNext().printNode();
			checkFillPrevious( ((GotoNode) iter).getNext(), level);
		} else if (iter instanceof IterationNode) {
			System.out.print(" --- " + iter.getPrevious().size() + " --- ");
			iter.printNode();
			if (iter.getNext() != null) checkFillPrevious(iter.getNext(), level);
			else return;
		} else if (iter instanceof EmptyNode) {
			System.out.print(" --- " + iter.getPrevious().size() + " --- ");
			iter.printNode();
			checkFillPrevious(iter.getNext(), level);
		} else if (iter instanceof EndConditionNode) {	
			level -= 7;
		} else if (iter instanceof BeginNode)  {
			System.out.print(" --- " + iter.getPrevious().size() + " --- ");
			iter.printNode();
			checkFillPrevious(iter.getNext(), level);
			System.out.print(" --- " + ((BeginNode) iter).getEndNode().getPrevious().size() + " --- ");
			((BeginNode) iter).getEndNode().printNode();
			checkFillPrevious(((BeginNode) iter).getEndNode().getNext(),  level);
		} else if (iter instanceof EndNode) {
			System.out.print(" --- " + iter.getPrevious().size() + " --- ");
			iter.printNode();
			checkFillPrevious(iter.getNext(), level);
		}
		else {
			if (iter.getPrevious() == null) {
				System.out.print("--- 0 ---");
			} else System.out.print(" --- " + iter.getPrevious().size() + " --- ");
			iter.printNode();			
			checkFillPrevious(iter.getNext(), level);
		}	
	}
	
	public boolean checkPriority(CFGNode origin, CFGNode destination) {
		
		return false;
	}
	
	public void scanCFG(CFGNode start) {
		CFGNode iter = start;
		iter.printNode();
		if (iter == null || iter.getPrevious() == null) return;	
		for (CFGNode i : iter.getPrevious()) {			
			if ((iter instanceof EndConditionNode) && (i instanceof BeginNode)) {
				continue;
			}
			if (iter instanceof EndConditionNode) System.out.println("*");
			scanCFG(i);
		}	
	}
	
	/**
	 * @return CFGnew da duoc rut gon
	 * Ap dung ky thuat toi uu hoa sinh rang buoc
	 * 
	 */
	public VtseCFG shortenGFG(){
		
		
		return this.cfgOld;
	}
	
}
