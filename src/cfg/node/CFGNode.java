package cfg.node;

import java.io.PrintStream;
import java.util.ArrayList;

import cfg.build.index.VariableManager;

public abstract class CFGNode {	
	protected ArrayList<CFGNode> previous;
	protected CFGNode next;	
	private boolean vistited;
	private ArrayList<Integer> level = new ArrayList<>();
	private boolean flag;
	public CFGNode(){		
	}
	
	public CFGNode(CFGNode next){
		this.vistited = false;
		this.next = next;		
	}

	public CFGNode getNext() {
		return next;
	}

	public void setNext(CFGNode next) {
		this.next = next;
	}
	
	public ArrayList<CFGNode> getPrevious(){
		return this.previous;
	}
	
	public void setPrevious(ArrayList<CFGNode> previous){
		this.previous = previous;
	}
	
	public void addPrevious(CFGNode node){
		if (this.previous == null){
			this.previous = new ArrayList<>();
		}
		this.previous.add(node);
	}
	
	public ArrayList<CFGNode> adjacent() {
		ArrayList<CFGNode> adj = new ArrayList<>();
		adj.add(next);
		return adj;
	}
		
	public void printNode(){	
		if (this != null) System.out.println(this.getClass());
	}
	public String toString() {
		return "";
	}
	public void index(VariableManager vm) {
	}

	public boolean isVistited() {
		return vistited;
	}
	public String getFormula() {
		return null;	
	}
	public String getInfixFormula() {
		return null;
	}
	public void printFormular(PrintStream ps) {
		ps.println(getFormula());
	}
	public void printInfixFormular(PrintStream ps) {
		ps.println(getInfixFormula());
	}
	public void setVistited(boolean vistited) {
		this.vistited = vistited;
	}

	public ArrayList<Integer> getLevel() {
		return level;
	}

	public void setLevel(ArrayList<Integer> level) {
		this.level = level;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
