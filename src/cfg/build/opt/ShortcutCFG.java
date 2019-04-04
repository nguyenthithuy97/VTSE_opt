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
	private ArrayList<Integer> levelManager;
	public ShortcutCFG(){		
	}
	
	public ShortcutCFG(VtseCFG cfg){
		this.cfgOld = cfg;
		this.setConjunction(new ArrayList<CFGNode>());
		this.levelManager = new ArrayList<Integer>();
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
	public void fillPreviousNode(CFGNode start, int index){
		CFGNode iter = start;
		if (iter == null) {			
			return;
		} else if (iter instanceof DecisionNode) {
			iter.setLevel(increaseLevel(iter, index));
			CFGNode thenNode = ((DecisionNode) iter).getThenNode();
			CFGNode elseNode = ((DecisionNode) iter).getElseNode();			
			if (thenNode != null) {
				thenNode.addPrevious(iter);
				fillPreviousNode(thenNode, index);
			}			
			if (elseNode != null){
				elseNode.addPrevious(iter);
				fillPreviousNode(elseNode, index);
			}				
		}  else if (iter instanceof GotoNode) {	
			iter.setLevel(increaseLevel(iter, index));
			if (iter.getNext() != null) iter.getNext().addPrevious(iter);
			fillPreviousNode(((GotoNode) iter).getNext(), index);
		} else if (iter instanceof IterationNode) {
			iter.setLevel(increaseLevel(iter, index));
			if (iter.getNext() != null) {
				iter.getNext().addPrevious(iter);
				fillPreviousNode(iter.getNext(), index);
			}
			else return;
		} else if (iter instanceof EmptyNode) {	
			iter.setLevel(increaseLevel(iter, index));
			if (iter.getNext() != null)iter.getNext().addPrevious(iter);
			fillPreviousNode(iter.getNext(), index);
		} else if (iter instanceof EndConditionNode) {
			return;
		} else if (iter instanceof BeginNode)  {
			iter.setLevel(increaseLevel(iter, index));
			if (iter.getNext() != null) iter.getNext().addPrevious(iter);
			fillPreviousNode(iter.getNext(), index + 1);
			
			((BeginNode) iter).getEndNode().addPrevious(iter);
			CFGNode endNode = ((BeginNode) iter).getEndNode();
			endNode.setLevel(increaseLevel(endNode, index));
			if (endNode.getNext() != null) endNode.getNext().addPrevious(endNode);
			fillPreviousNode(((BeginNode) iter).getEndNode().getNext(), index);
		} else if (iter instanceof EndNode) {
			iter.setLevel(increaseLevel(iter, index));
			if (iter.getNext() != null) iter.getNext().addPrevious(iter);
			fillPreviousNode(iter.getNext(), index);
		}
		else {	
			iter.setLevel(increaseLevel(iter, index));
			if (iter.getNext() != null) {
				iter.getNext().addPrevious(iter);
			}
			fillPreviousNode(iter.getNext(), index);
		}	
	}	
	
	public void checkFillPrevious(CFGNode start, int level){
		CFGNode iter = start;
		printSpace(level);
		if (iter == null) {
//			System.out.println(iter.get);
			return;
		} else if (iter instanceof DecisionNode) {
			System.out.print(" --- " + iter.getLevel().toString() + " --- ");
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
			System.out.print(" --- " + iter.getLevel().toString() + " --- ");
			iter.printNode();
			//printSpace(level);
			//((GotoNode) iter).getNext().printNode();
			checkFillPrevious( ((GotoNode) iter).getNext(), level);
		} else if (iter instanceof IterationNode) {
			System.out.print(" --- " + iter.getLevel().toString() + " --- ");
			iter.printNode();
			if (iter.getNext() != null) checkFillPrevious(iter.getNext(), level);
			else return;
		} else if (iter instanceof EmptyNode) {
			System.out.print(" --- " + iter.getLevel().toString() + " --- ");
			iter.printNode();
			checkFillPrevious(iter.getNext(), level);
		} else if (iter instanceof EndConditionNode) {	
			level -= 7;
		} else if (iter instanceof BeginNode)  {
			System.out.print(" --- " + iter.getLevel().toString() + " --- ");
			iter.printNode();
			checkFillPrevious(iter.getNext(), level);
			System.out.print(" --- " + ((BeginNode) iter).getEndNode().getLevel().toString() + " --- ");
			((BeginNode) iter).getEndNode().printNode();
			checkFillPrevious(((BeginNode) iter).getEndNode().getNext(),  level);
		} else if (iter instanceof EndNode) {
			System.out.print(" --- " + iter.getLevel().toString() + " --- ");
			iter.printNode();
			checkFillPrevious(iter.getNext(), level);
		}
		else {
			System.out.print(" --- " + iter.getLevel().toString() + " --- ");
			iter.printNode();			
			checkFillPrevious(iter.getNext(), level);
		}		
	}
	
	public boolean checkPriority(CFGNode origin, CFGNode destination) {
//		TODO xac dinh quan he giua node
		return false;
	}
	
	public ArrayList<Integer> increaseLevel(CFGNode node, int index) {
		ArrayList<Integer> level = new ArrayList<>();;		
		if (node.getPrevious() == null) {			
			level.add(1);
			this.levelManager.add(1);
			return level;
		}		
		CFGNode previous = node.getPrevious().get(0);
		if (node instanceof EndConditionNode) {
			for (CFGNode iter : node.getPrevious()) {
				if (iter instanceof BeginNode) {
					level = iter.getLevel();	
				}				
			}			
			this.levelManager.remove(index + 1);
		} else if (previous instanceof BeginNode) {
			level = new ArrayList<>(previous.getLevel());
			level.add(1);
			this.levelManager.add(1);
		} else {			
			level = new ArrayList<>(previous.getLevel());			
			level.set(index, this.levelManager.get(index) + 1);
			this.levelManager.set(index, level.get(index));
 		} 	
//		node.printNode();
//		System.err.println(this.levelManager.toString() + level.toString() + index);
		return level;
	}
	
//	public void indexLevelCFGNode(CFGNode start, int index) {
//		CFGNode iter = start;
//		if (iter == null) {			
//			return;
//		} else if (iter instanceof DecisionNode) {
//			iter.setLevel(increaseLevel(iter, index));
//			CFGNode thenNode = ((DecisionNode) iter).getThenNode();
//			CFGNode elseNode = ((DecisionNode) iter).getElseNode();			
//			if (thenNode != null) {
//				indexLevelCFGNode(thenNode, index);
//			}			
//			if (elseNode != null) {
//				indexLevelCFGNode(elseNode, index);
//			}
//		} else if (iter instanceof IterationNode) {
//			iter.setLevel(increaseLevel(iter, index));
//			if (iter.getNext() != null) {
//				indexLevelCFGNode(iter.getNext(), index);
//			}
//			else return;
//		} else if (iter instanceof EndConditionNode) {
//			return;
//		} else if (iter instanceof BeginNode)  {
//			iter.setLevel(increaseLevel(iter, index));			
//			indexLevelCFGNode(iter.getNext(), index + 1);
//			
//			CFGNode endNode = ((BeginNode) iter).getEndNode();
//			endNode.setLevel(increaseLevel(endNode, index));
//			indexLevelCFGNode(endNode.getNext(), index);
//		}
//		else {			
//			iter.setLevel(increaseLevel(iter, index));
//			indexLevelCFGNode(iter.getNext(), index);
//		}		
//	}
//	
//	public void checkIndexLevel() {
//		if (this.levelManager.isEmpty()) {
//			this.levelManager.add(0, 0);			
//		}
//		indexLevelCFGNode(this.cfgOld.getStart(), 0);
//	}
	
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

	public ArrayList<CFGNode> getConjunction() {
		return conjunction;
	}

	public void setConjunction(ArrayList<CFGNode> conjunction) {
		this.conjunction = conjunction;
	}

	public ArrayList<Integer> getLevelManager() {
		return levelManager;
	}

	public void setLevelManager(ArrayList<Integer> levelManager) {
		this.levelManager = levelManager;
	}
	
}
