import java.math.BigInteger;

public class ReductionMatrix 
{	
	private int smoothness;
	private int numIndependentPairs;
	
	private BigInteger[][] reductionMatrix;
	private BigInteger[][] reductionCoefficients;
	
	private int[] vectorPivots;
	
	public ReductionMatrix(int smoothness)
	{
		this.smoothness = smoothness;
		numIndependentPairs = 0;
		
		reductionMatrix = new BigInteger[smoothness][smoothness];
		reductionCoefficients = new BigInteger[smoothness][smoothness];
		
		vectorPivots = new int[smoothness];
	}
	
	public boolean isIndependent(BigInteger[] vector)
	{
 		if (numIndependentPairs == 0)
		{			
			boolean pivotFound = false;
			
			for (int powerNum = 0; powerNum < smoothness; powerNum++)
			{
				if ((!pivotFound) && (vector[powerNum].compareTo(BigInteger.ZERO) != 0))
				{
					pivotFound = true;
					vectorPivots[0] = powerNum;
				}
				
				reductionMatrix[0][powerNum] = vector[powerNum];
				reductionCoefficients[0][powerNum] = BigInteger.ZERO;
			}
			
			reductionCoefficients[0][0] = BigInteger.ONE;
			
			numIndependentPairs++;
			
			return true;
		}
		else
		{			
			BigInteger[] workingVector = new BigInteger[smoothness];
			BigInteger[] workingReductionCoefficients = new BigInteger[smoothness];
			
			BigInteger pivotPower, lowerPower;
			BigInteger pivotMultiplier, lowerMultiplier;
			BigInteger powerGCD;
			
			for (int i = 0; i < smoothness; i++)
			{
				workingVector[i] = vector[i];
				workingReductionCoefficients[i] = BigInteger.ZERO;
			}
			
			workingReductionCoefficients[numIndependentPairs] = BigInteger.ONE;
			
			for (int vectorNum = 0; vectorNum < numIndependentPairs; vectorNum++)
			{				
				pivotPower = reductionMatrix[vectorNum][vectorPivots[vectorNum]];
				lowerPower = workingVector[vectorPivots[vectorNum]];
				
				if (lowerPower.compareTo(BigInteger.ZERO) != 0)
				{
					powerGCD = pivotPower.gcd(lowerPower);
					
					pivotMultiplier = lowerPower.divide(powerGCD);
					lowerMultiplier = pivotPower.divide(powerGCD);
					
					for (int powerNum = 0; powerNum < smoothness; powerNum++)
					{
						workingVector[powerNum] = (workingVector[powerNum].multiply(lowerMultiplier)).subtract( 
												  (reductionMatrix[vectorNum][powerNum].multiply(pivotMultiplier)));
						
						workingReductionCoefficients[powerNum] = (workingReductionCoefficients[powerNum].multiply(lowerMultiplier)).subtract(
																 (reductionCoefficients[vectorNum][powerNum].multiply(pivotMultiplier)));
					}
				}
			}
			
			boolean isIndependent = false;
			boolean pivotFound = false;
			
			BigInteger rowGCD = BigInteger.ZERO;
			
			for (int powerNum = 0; powerNum < smoothness; powerNum++)
			{
				if (workingVector[powerNum].compareTo(BigInteger.ZERO) != 0)
				{
					if (!pivotFound)
					{
						vectorPivots[numIndependentPairs] = powerNum;
						pivotFound = true;
						isIndependent = true;
						
						rowGCD = workingVector[powerNum];
					}
					else
					{
						rowGCD = rowGCD.gcd(workingVector[powerNum]);
					}
				}
			}
			
			if (isIndependent)
			{				
				for (int powerNum = 0; powerNum < smoothness; powerNum++)
				{
					reductionMatrix[numIndependentPairs][powerNum] = workingVector[powerNum].divide(rowGCD);
					reductionCoefficients[numIndependentPairs][powerNum] = workingReductionCoefficients[powerNum];
				}

				numIndependentPairs++;
				
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	public BigInteger[] solveVector(BigInteger[] vector)
	{
		BigInteger[] workingVector = new BigInteger[smoothness];
		BigInteger[] solution = new BigInteger[smoothness + 1];
		
		for (int powerNum = 0; powerNum < smoothness; powerNum++)
		{
			workingVector[powerNum] = vector[powerNum];
			solution[powerNum] = BigInteger.ZERO;
		}
		
		solution[smoothness] = BigInteger.ONE;
				
		BigInteger upperPower, lowerPower;
		BigInteger upperMultiplier, lowerMultiplier;
		BigInteger powerGCD = BigInteger.ZERO;
		
		for (int vectorNum = 0; vectorNum < smoothness; vectorNum++)
		{				
			upperPower = reductionMatrix[vectorNum][vectorPivots[vectorNum]];
			lowerPower = workingVector[vectorPivots[vectorNum]];
			
			if (lowerPower.compareTo(BigInteger.ZERO) != 0)
			{
				powerGCD = upperPower.gcd(lowerPower);
				
				upperMultiplier = lowerPower.divide(powerGCD);
				lowerMultiplier = upperPower.divide(powerGCD);
				
				for (int powerNum = 0; powerNum < smoothness; powerNum++)
				{
					workingVector[powerNum] = (workingVector[powerNum].multiply(lowerMultiplier)).subtract( 
											  (reductionMatrix[vectorNum][powerNum].multiply(upperMultiplier)));
					
					solution[powerNum] = (solution[powerNum].multiply(lowerMultiplier)).subtract(
										 (reductionCoefficients[vectorNum][powerNum].multiply(upperMultiplier)));
				}
				
				solution[smoothness] = solution[smoothness].multiply(lowerMultiplier);
			}
		}
		
		return solution;
	}
	
	public void printVectorPivots()
	{
		print1DColumnVector(vectorPivots);
	}
	
	public void printReductionMatrix()
	{
		print2DArray(reductionMatrix);
	}
	
	public void printReductionCoefficients()
	{
		print2DArray(reductionCoefficients);
	}
	
	private static void print2DArray(BigInteger[][] array)
	{
		System.out.println("");
		
		for (int i = 0; i < array.length; i++)
		{
			for (int j = 0; j < array[0].length; j++)
			{
				System.out.print(array[i][j]);
				
				if (j == (array[0].length - 1))
				{
					System.out.println("");
				}
				else
				{
					System.out.print(",");
				}
			}
		}
		
		System.out.println("");
	}
	
	private static void print1DColumnVector(int[] array)
	{
		System.out.println("");
		
		for (int i = 0; i < array.length; i++)
		{
			System.out.println(array[i]);
		}
		
		System.out.println("");
	}
}
