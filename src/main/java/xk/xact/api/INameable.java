package xk.xact.api;

/**
 * Used to identify XACT Crafters with names
 */
public interface INameable {
	
	public String getName();
	
	public void setName(String name);
	
	public boolean hasName();
	
	public int getXPos();
	
	public int getYPos();
	
	public int getZPos();
	
	public String getUUID();
}
