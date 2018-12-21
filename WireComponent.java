/**************************************************************
each one of these objects will represent some piece of wire
for now it will only be able to represent some sort of voltage 
source and some resistance.  So resistive sources can be modeled
as well as ideal motive sources and ideal resistors.  The first
new stuff to come would be current souces then other linear 
components like inductors and capacitors!

I'm also considering adding a special characteristics for 
material type and length to accound for natural wire resitance
***************************************************************/
import java.lang.*;

public class WireComponent
{
	private double resistance = 0;											//self explanatory
	private double voltage = 0;												//yep
	private int ID;															//keep track of the id on the component

	//ought to be zero upon construction no matter what
	public double calculatedVoltage = 0;									//allows for the voltage to be a running tally for superposition


	//these are slighttly different than a normal approach
	//for the math to work out when you try to access this 
	//wire component from w (negative) terminal it will give
	//negative values for volatage and current dependant 
	//voltage
	public static int v;												//from positive 
	public static int w;												//to negative


	//init as a resistance and voltage component!
	public WireComponent(int vee, int doubleu, double r, double vol, int id)
	{
		v = vee;
		w = doubleu;
		resistance = r;
		voltage = vol;
	}

	//init as just a resistive component
	public WireComponent(int vee, int doubleu, double r, String type, int id)
	{
		if(type.equals("resistor"))
		{
			v = vee;
			w = doubleu;
			resistance = r;
			ID = id;
		}
		else if (type.equals("voltage"))
		{
			v = vee;
			w = doubleu;
			voltage = r;
			ID = id;
		}
		else
		{
			throw new IllegalStateException("initialize as either voltage or resistor");
		}
	}

	//init as just a wire
	public WireComponent(int vee, int doubleu, int id)
	{
		v = vee;
		w = doubleu;
		ID = id;
	}

	//this method will return a double array of length 2
	//containing at index 0 the voltage of the component
	//at index 1 the resistive coefficent.  It will be negative or
	//positive of what is stored dependanct on which terminal this 
	//is being accessed from
	public double[] currentFlowInfo(int accessTerminal) 
	{
		if (accessTerminal != v || accessTerminal != w) 
		{
			throw new IllegalArgumentException("This component is not connected to node: " + accessTerminal);
		}

		//accessing from positive terminal
		if (accessTerminal == v)
		{
			double[] ret = {voltage, resistance};
			return ret;
		}
		else
		{
			double[] ret = {-voltage, -resistance};
			return ret;
		}
	}

	//some getters, pretty self explanatory
	public double resistance(){return resistance;}
	public double voltage(){return voltage;}
	public boolean isPlainWire(){return (voltage == 0 && resistance == 0);}
	public int from(){return v;}
	public int to(){return w;}
	public int positive(){return this.from();}
	public int negative(){return this.to();}
	public boolean isSource(){return voltage != 0;}
	public double calculatedVoltage(){return calculatedVoltage;}
	public int ID(){return ID;}

	//turn this into a short
	public void toShort()
	{
		resistance = 0;
		voltage = 0;
	}

	public String toString()
	{
		return "pos terminal " + v + ", neg terminal " + w + ".  With voltage: " +  voltage + " and resistance: " + resistance;
	}

}