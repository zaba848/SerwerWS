package shared;

import java.io.Serializable;

import shared.CCommand.COMMANDS;

public class CPackage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3517817343141L;
	
	private COMMANDS command;
	private String data;
	private String destynation;
	private String source;
	
	public CPackage(COMMANDS caommand, String destynation, String source, String data)
	{
		super();
		this.setCommand(caommand);
		this.setData(data);
		this.setDestynation(destynation);
		this.setSource(source);
	}
	
	public CPackage()
	{
		this.setCommand(COMMANDS.BEGIN);
		this.setData(" ");
		this.setDestynation(" ");
		this.setSource(" ");
	}
	
	public void reset()
	{
		this.setCommand(COMMANDS.BEGIN);
		this.setData(" ");
		this.setDestynation(" ");

	}
	
	@Override
	public String toString() {
		return "command=" + getCommand() + ", data=" + getData() + ";";
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public COMMANDS getCommand() {
		return command;
	}

	public void setCommand(COMMANDS command) {
		this.command = command;
	}

	public String getDestynation() {
		return destynation;
	}

	public void setDestynation(String destynation) {
		this.destynation = destynation;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
