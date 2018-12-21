/********************************************************************************
This class is meant to represent a reistor network as a weighted graph
it will be undirected in order to easily program the node volatage matehamtics
since you can go through the postive or negative termimal but the edge class 
will allow for detection of travel direction

I'm storying the this as a weighted graph represented as an adjacency list.  The 
edges will be WireComponent objects.
********************************************************************************/
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class ResistorNetwork
{
	/****************************************
				Instance Data
	****************************************/
	//keeps track of number of vertices (nodes) and egdes (wires/sources/components)
	private final int V;
	private int E;

	private ArrayList<WireComponent>[] adjTo;						//represents the wire components connected to any node

	public ArrayList<WireComponent> sources;						//tracks the sources on the circuit

	//this bad boy might not be needed we'll see
	public ArrayList<WireComponent> others;							//tracks the non source devices on the circuit


	/*******************************
			Constructors
	*******************************/
	public ResistorNetwork(int V)						
	{
		this.V = V;
		this.E = 0;

		adjTo = (ArrayList<WireComponent>[]) new ArrayList[V];
		for (int i = 0; i < V; i++)
		{
			adjTo[i] = new ArrayList<WireComponent>();
		}
	}

	//copy the given resistor network 
	//adds edges with edge id of the number of edges in this
	public ResistorNetwork(ResistorNetwork network)
	{
		this.V = network.V();
		this.E = 0;

		adjTo = (ArrayList<WireComponent>[]) new ArrayList[V];
		for (int i = 0; i < V; i++)
		{
			adjTo[i] = new ArrayList<WireComponent>();
		}

		//now to copy over edges and make new ones!
		for (int i = 0; i < V; i++)
		{
			Iterable<WireComponent> toIterate = network.connectedTo(i);

			for (WireComponent wire: toIterate)
			{
				int v = wire.from();
				int w = wire.to();

				double resistance = wire.resistance();
				double voltage = wire.voltage();

				WireComponent toAdd = new WireComponent(v,w,resistance, voltage, this.E());

				this.addComponent(toAdd);
			}
		}
	}


	/*******************************************
				Getters n Setters
	*******************************************/
	//returns the components which this node is connected to
	public Iterable<WireComponent> connectedTo(int v)
	{
		verifyVertex(v);
		return adjTo[v];
	}

	public int V(){return this.V;}
	public int E(){return this.E;}



	/**********************************
				Mutators
	**********************************/
	//add the component to the network
	//if it is a source add it to the sources network
	public void addComponent(WireComponent component)
	{
		verifyVertex(component.to());
		verifyVertex(component.from());

		E++;

		adjTo[component.from()].add(component);
		adjTo[component.to()].add(component);

		if(component.isSource())
		{
			sources.add(component);
		}
		else
		{
			others.add(component);
		}

	}

	public void turnToShort(WireComponent component)
	{
		verifyVertex(component.to());
		verifyVertex(component.from());

		//this will be reduntant, get ready
		Iterable<WireComponent> to = adjTo[component.to()];
		Iterable<WireComponent> from = adjTo[component.from()];

		for (WireComponent wire : to)
		{
			if (wire.ID() == component.ID())
			{
				wire.toShort();
			}
		}
		for (WireComponent wire : from)
		{
			if (wire.ID() == component.ID())
			{
				wire.toShort();
			}
		}
	}

	//to turn one of the components inta 

	/**************************************
		    Helpers and Weird Shit
	**************************************/
	//verifies the vextex argument passed
	private void verifyVertex(int v)
	{
		if (v < 0 || v >= V){throw new UnsupportedOperationException("the vertex " + v + " is not included in this graph");}
	}

}