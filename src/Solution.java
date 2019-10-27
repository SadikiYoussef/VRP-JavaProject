import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public  class Solution {
	public int NoOfVehicles;
	public int NoOfCustomers;
	public Vehicle[] Vehicles;
    public double Cost = 0;

    public List<Object> SolRealis = new ArrayList<>();
   
    
    //Tabu Variables
    public Vehicle[] VehiclesForBestSolution;
    public double BestSolutionCost;

    public double getCost() {
		return Cost;
	}

	public void setCost(double cost) {
		Cost = cost;
	}

	public ArrayList<Double> PastSolutions;

    public Solution(int CustNum, int VechNum , int VechCap)
    {
        this.NoOfVehicles = VechNum;
        this.NoOfCustomers = CustNum;
        this.Cost = 0;
        Vehicles = new Vehicle[NoOfVehicles];
    
        for (int i = 0 ; i < NoOfVehicles; i++)
        {
            Vehicles[i] = new Vehicle(i+1,VechCap);
        }
    }

    public boolean UnassignedCustomerExists(Node[] Nodes)
    {
        for (int i = 1; i < Nodes.length; i++)
        {
            if (!Nodes[i].IsRouted)
                return true;
        
        }
        return false;
    }

    public void GreedySolution(Node[] Nodes , double[][] CostMatrix) {

        double CandCost,EndCost;
        int VehIndex = 0;

        while (UnassignedCustomerExists(Nodes)) {

            int CustIndex = 0;
            Node Candidate = null;
            double minCost = (float) Double.MAX_VALUE;

            if (Vehicles[VehIndex].Route.isEmpty())
            {
                Vehicles[VehIndex].AddNode(Nodes[0]);
            }

            for (int i = 1; i <= NoOfCustomers; i++) {
                if (Nodes[i].IsRouted == false) {
                    if (Vehicles[VehIndex].CheckIfFits(Nodes[i].demand)) {
                        CandCost = CostMatrix[Vehicles[VehIndex].CurLoc][i];
                        if (minCost > CandCost) {
                            minCost = CandCost;
                            CustIndex = i;
                            Candidate = Nodes[i];
                        }
                    }
                }
            }
            
            
            Random ran = new Random();
            int i = 1+ran.nextInt(NoOfCustomers);
            if (Nodes[i].IsRouted == false) {
            	if (Vehicles[VehIndex].CheckIfFits(Nodes[i].demand)) {
                	CandCost = CostMatrix[Vehicles[VehIndex].CurLoc][i];                 
                	minCost = CandCost;
                    CustIndex = i;
                    Candidate = Nodes[i];
                }
            }
            
            if ( Candidate  == null){//Not a single Customer Fits
            	
                if ( VehIndex+1 < Vehicles.length ){ //We have more vehicles to assign
                
                    if (Vehicles[VehIndex].CurLoc != 0) {//End this route
                        EndCost = CostMatrix[Vehicles[VehIndex].CurLoc][0];
                        Vehicles[VehIndex].AddNode(Nodes[0]);
                        this.Cost +=  EndCost;
                    }
                    VehIndex = VehIndex+1; //Go to next Vehicle
                }
                else //We DO NOT have any more vehicle to assign. The problem is unsolved under these parameters
                {
                    System.out.println("\nThe rest customers do not fit in any Vehicle\n" +
                            "The problem cannot be resolved under these constrains");
                    System.exit(0);
                }
            }
            else
            {
                Vehicles[VehIndex].AddNode(Candidate);//If a fitting Customer is Found
                Nodes[CustIndex].IsRouted = true;
                this.Cost += minCost;
            }
        }

        EndCost = CostMatrix[Vehicles[VehIndex].CurLoc][0];
        Vehicles[VehIndex].AddNode(Nodes[0]);
        this.Cost +=  EndCost;
        
        
        /// for clears  routed
        for (int i = 1; i <= NoOfCustomers; i++) {
        	Nodes[i].IsRouted = false;
        }

    }
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	String retour;
    	retour = "v "+NoOfVehicles + " " +" nc "+ NoOfCustomers +"c " + Cost +" ";
    	return retour;
    }
    
   
    
    
    public void SolutionPrint(String Solution_Label){//Print Solution In console
        System.out.println("=========================================================");
        SolRealis.clear();
        System.out.println(Solution_Label+"\n");
        for (int j=0 ; j < NoOfVehicles ; j++)
        {
            if (! Vehicles[j].Route.isEmpty())
            {   System.out.print("Vehicle " + j + ":");
                int RoutSize = Vehicles[j].Route.size();
                for (int k = 0; k < RoutSize ; k++) {
                    if (k == RoutSize-1)
                    { System.out.print(Vehicles[j].Route.get(k).NodeId );
                    SolRealis.add(Vehicles[j].Route.get(k).NodeId) ;
                    }
                    else
                    { System.out.print(Vehicles[j].Route.get(k).NodeId+ "->"); 
                    SolRealis.add(Vehicles[j].Route.get(k).NodeId);
                    }
                }
                int sss = SolRealis.size();
                System.out.println();
                SolRealis.remove(sss-1);
            }
        }
        System.out.println("\nSolution Cost "+this.Cost+"\n");
        System.out.println(SolRealis);
        SolRealis.toString();
        
    }
}