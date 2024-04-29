import java.util.*;

public class HR_Application {
	
	Scanner sc=new Scanner(System.in);
	
	
	public void enterDetails()
	{

		String [] departmentName = new String [3];
		String [] address = new String [3];
		String [] employeeName = new String [6];
		int [] departmentID = new int [6];
		double [] grossSalary = new double [6];
		String dID;
		String gross;	
		int option;
		int test;

		
		{
			System.out.println("***Enter department details***");
			System.out.println("");
			
			for (int i=0; i<departmentName.length; i++)
			{	
				System.out.println("Enter name for department "+(i+1));
				departmentName [i] = sc.nextLine();
				System.out.println("Enter "+departmentName[i]+" address ");
				address [i] = sc.nextLine();
			}
			
			for (int i=0; i<departmentName.length; i++)
			{	
				System.out.println("");
				System.out.println("Department ID - " + (i+1));
				System.out.println("Department Name - " + departmentName [i]);
				System.out.println("Department Adress - " + address [i]);
				System.out.println("");
			}
			
			System.out.println("");
			System.out.println("***Enter employee details***");
			System.out.println("");
			
			for (int i=0; i<employeeName.length; i++)
			{	System.out.println("Enter name for employee "+(i+1));
				employeeName [i] = sc.nextLine();
				System.out.println("Enter department ID for "+employeeName[i]);
				dID = sc.nextLine();
				test= Integer.parseInt(dID);
					while (test != 1 && test != 2 && test != 3)
						{
							System.out.println("Error - please enter either 1, 2, or 3 for department ID");
							dID = sc.nextLine();
							test= Integer.parseInt(dID);
						}
				departmentID [i]= Integer.parseInt(dID);
				System.out.println("Gross salary for "+employeeName[i]);
				gross = sc.nextLine();
				grossSalary [i]= Double.parseDouble(gross);
			}
				
			for (int i=0; i<employeeName.length; i++)
			{	
				System.out.println("");
				System.out.println("Employee ID - " + (i+1));
				System.out.println("Employee Name - " + employeeName [i]);
				System.out.println("Employee Department ID - " + departmentID [i]);
				System.out.println("Employee Gross Salary - £" + grossSalary [i]);
				System.out.println("");
			}
			
			do {
				System.out.println("Please Select an Option (Input 1, 2, 3, 4, 5, or 6)");
				System.out.println("");
				System.out.println("Option 1 - Sort by department");
				System.out.println("Option 2 - Sort by gross salary");
				System.out.println("Option 3 - Calculate Tax");
				System.out.println("Option 4 - Find highest salary");
				System.out.println("Option 5 - Finding lowest salary");
				System.out.println("Option 6 - Exit Program");
				option = sc.nextInt();
				switch(option) {
					case 1:
					    sortDepartment(employeeName, departmentID);
						break;
					case 2:
						sortSalary(grossSalary, employeeName);
						break;
					case 3:
						displayTax(grossSalary, employeeName);
						break;
					case 4:
						highestSalary(grossSalary, employeeName);
						break;
					case 5:
						lowestSalary(grossSalary, employeeName);
						break;

					default:
						if(option != 6) System.out.println("Unknown option - please enter a number between 1 and 6");
				
				}
			} while (option != 6);
			
		}
		}
			
			public void sortDepartment(String employeeName[], int departmentID[])
			{
				
				int i;
				for (i=0; i<departmentID.length; i++)
					{
					if (departmentID[i] == 1)
						{
						System.out.println(employeeName[i]+" | department ID - "+departmentID[i]);
						}
					}
				for (i=1; i<departmentID.length; i++)
					{
					if (departmentID[i] == 2)
						{
						System.out.println(employeeName[i]+" | department ID - "+departmentID[i]);
						}
					}
				for (i=1; i<departmentID.length; i++)
					{
					if (departmentID[i] == 3)
						{
						System.out.println(employeeName[i]+" | department ID - "+departmentID[i]);
						}
					}
			}
			
			public void sortSalary(double grossSalary[], String employeeName[])
			{	
				int i;
				int j;
				double [] sortedSalary=new double [6];
				
				for (i=0; i<grossSalary.length; i++)
					{
					sortedSalary[i]=grossSalary[i];
					}
				
				heapsort(sortedSalary);

				for (i=0, j=0; i<grossSalary.length;)
					
						{
							if (grossSalary[j]==sortedSalary[i]) 
							{
							System.out.println(employeeName[j]+" | Gross Salary - "+sortedSalary[i]);
								j=0;
								i++;
							}
							else if (grossSalary[j]!=sortedSalary[i] && j < 5) 
							{
								j++;
							}
							else if (grossSalary[j]!=sortedSalary[i] && j == 5)
							{
								j=0;
							}
						}
					
			}
			
			public double calculateTax(double gross)
			{
				double tax = 0;
				
				if (gross <= 12570)
				{
					tax = 0;
				}
				else if (gross > 12570 && gross <= 50270)
				{
					tax = (gross-12570)*0.2;
				}
				else if (gross > 50270 && gross <= 150000)
				{
					tax = ((37700*0.2)+((gross-50270)*0.4));
				}
				else if (gross > 150000)
				{
					tax = ((37700*0.2)+(99730*0.4)+((gross-150000)*0.45)); 
				}
				
				return tax;
			}
			
			public void displayTax(double grossSalary[], String employeeName[])
			{
				int i;
				double [] taxedSalary=new double [6];
				
				for (i=0; i<grossSalary.length; i++)
				{
					taxedSalary[i] = calculateTax(grossSalary[i]);
					System.out.println("Employee Name - "+employeeName[i]+" | Salary After Tax - £"+(grossSalary[i]-taxedSalary[i]));
				}
			}
			
			public void highestSalary(double grossSalary[], String employeeName[])
			{

				int i;
				int n = grossSalary.length;
				
				double [] sortedSalary = new double [6];
				
				for (i=0; i<n; i++)
					{
					sortedSalary[i] = grossSalary[i];
					}
				
				heapsort(sortedSalary);
				
				for (i=0; i<grossSalary.length; i++)
					{
					if (sortedSalary[sortedSalary.length-1]==grossSalary[i])
						{
						System.out.println("Highest Salary is "+employeeName[i]+" | £"+(sortedSalary[sortedSalary.length-1]-calculateTax(sortedSalary[sortedSalary.length-1])));
						}
						
					}

			}
		
			public void lowestSalary(double grossSalary[], String employeeName[])
			{
				int i;
				int n = grossSalary.length;
				
				double [] sortedSalary = new double [6];
				
				for (i=0; i<n; i++)
					{
					sortedSalary[i] = grossSalary[i];
					}
				
				heapsort(sortedSalary);
				
				for (i=0; i<grossSalary.length; i++)
					{
					if (sortedSalary[0]==grossSalary[i])
						{
						System.out.println("Lowest Salary is "+employeeName[i]+" | £"+(sortedSalary[0]-calculateTax(sortedSalary[0])));
						}
						
					}
			}
		
			// heapsort method
			public void heapsort(double sortedSalary[])
		    {
		        int n = sortedSalary.length;
		  
		        for (int i = n / 2 - 1; i >= 0; i--)
		            heapify(sortedSalary, n, i);
		  
		        // One by one extract an element from heap
		        for (int i=n-1; i>=0; i--)
		        {
		            // Move current root to end
		            double root = sortedSalary[0];
		            sortedSalary[0] = sortedSalary[i];
		            sortedSalary[i] = root;
		  
		            // call max heapify on the reduced heap
		            heapify(sortedSalary, i, 0);
		        }
		    }
		  
			// max heapify method
		    public void heapify(double sortedSalary[], int n, int i)
		    {
		        int largest = i;
		        int l = 2*i + 1;
		        int r = 2*i + 2; 
		  
		        // If left child is larger than root
		        if (l < n && sortedSalary[l] > sortedSalary[largest])
		            largest = l;
		  
		        // If right child is larger than largest so far
		        if (r < n && sortedSalary[r] > sortedSalary[largest])
		            largest = r;
		  
		        // If largest is not root
		        if (largest != i)
		        {
		            double swap = sortedSalary[i];
		            sortedSalary[i] = sortedSalary[largest];
		            sortedSalary[largest] = swap;
		  
		            // recursively heapify the affected sub-tree
		            heapify(sortedSalary, n, largest);
		        }
		    }
		public static void main(String[] args)
		{
			HR_Application Begin=new HR_Application();
			Begin.enterDetails();
		}
}
