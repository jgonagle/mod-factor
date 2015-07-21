import java.math.BigInteger;
import java.util.ArrayList;

public class ModFactor 
{
	public static final int[] primeInts = {2,      3,     5,      7,     11,     13,     17,     19,     23,     29, 
									       31,     37,     41,    43,    47,     53,     59,     61,     67,     71, 
									       73,     79,     83,    89,    97,     101,    103,    107,    109,    113, 
									       127,    131,    137,   139,   149,    151,    157,    163,    167,    173, 
									       179,    181,    191,   193,   197,    199,    211,    223,    227,    229};
	
	public static BigInteger[] primes;
	
	static
	{
		primes = new BigInteger[primeInts.length];
		
		for (int i = 0; i < primes.length; i++)
		{
			primes[i] = new BigInteger(Integer.toString(primeInts[i]));
		}
	}
	
	private BigInteger cipherText;
	private BigInteger publicKey;
	private BigInteger modulus;	
	private int smoothness;

	private ArrayList<SmoothPair> independentSmoothPairs;
	private ArrayList<SmoothPair> dependentSmoothPairs;
	
	private ReductionMatrix reductionMatrix;
	
	public ModFactor(BigInteger cipherText, BigInteger publicKey, 
						 BigInteger modulus, int smoothness)
	{
		if ((cipherText.compareTo(BigInteger.ZERO) == 1) &&
			(publicKey.compareTo(BigInteger.ZERO) == 1) &&
			(modulus.compareTo(BigInteger.ZERO) == 1) &&
			(cipherText.compareTo(modulus) == -1) &&
			(smoothness > 0))
		{
			this.cipherText = cipherText;
			this.publicKey = publicKey;
			this.modulus = modulus;
			this.smoothness = smoothness;			
			
			independentSmoothPairs = new ArrayList<SmoothPair>();
			dependentSmoothPairs = new ArrayList<SmoothPair>();
			
			findIndependentSmoothPairs();
			findDependentGroupOrders();
		}
	}

	private void findIndependentSmoothPairs() 
	{
		BigInteger currentNum = BigInteger.ONE;
		
		SmoothPair currentSmoothPair;
		BigInteger[] smoothPowerVector = new BigInteger[smoothness];
		
		reductionMatrix = new ReductionMatrix(smoothness);			
		
		for (BigInteger vectorExponent = BigInteger.ONE; 
			 (vectorExponent.compareTo(modulus) == -1); 
			 vectorExponent = vectorExponent.add(BigInteger.ONE))
		{
			currentNum = currentNum.multiply(cipherText).mod(modulus);
			
			if ((smoothPowerVector = getSmoothPowerVector(currentNum)) != null)
			{
				currentSmoothPair = new SmoothPair(vectorExponent, smoothPowerVector);
				
				if ((independentSmoothPairs.size() < smoothness) && 
					reductionMatrix.isIndependent(smoothPowerVector))
				{
					independentSmoothPairs.add(currentSmoothPair);
					System.out.print("*");
				}
				else
				{
					dependentSmoothPairs.add(currentSmoothPair);
				}				
				
				currentSmoothPair.printSmoothPair();
				
				if ((independentSmoothPairs.size() == smoothness) &&
					(dependentSmoothPairs.size() >= 1))
				{
					break;
				}
			}
		}
		
		reductionMatrix.printReductionMatrix();
		reductionMatrix.printReductionCoefficients();
	}
	
	private void findDependentGroupOrders() 
	{
		BigInteger orderGCD = BigInteger.ZERO;
		BigInteger elementOrder;
		
		for (SmoothPair smoothPair : dependentSmoothPairs)
		{
			elementOrder = findElementOrder(smoothPair);
		
			if (orderGCD.compareTo(BigInteger.ZERO) != 0)
			{
				orderGCD = elementOrder;
			}
			else
			{
				orderGCD = orderGCD.gcd(elementOrder);
			}
			
			//System.out.println("(" + smoothPair.getExponent() + "): " + elementOrder);
		}
		
		System.out.println("\n(Order GCD): " + orderGCD);
	}
	
	private BigInteger findElementOrder(SmoothPair smoothPair)
	{
		BigInteger[] linearCombination = new BigInteger[smoothness];
		BigInteger elementOrder = BigInteger.ZERO;
		
		linearCombination = reductionMatrix.solveVector(smoothPair.getSmoothPowerVector());
				
		for (int vectorNum = 0; vectorNum < smoothness; vectorNum++)
		{
			elementOrder = elementOrder.add(linearCombination[vectorNum].multiply(
										independentSmoothPairs.get(vectorNum).getExponent()));
		}
		
		elementOrder = (elementOrder.add((smoothPair.getExponent()).multiply(linearCombination[smoothness]))).abs();
	
		return elementOrder;
	}

	private BigInteger[] getSmoothPowerVector(BigInteger someNum)
	{
		BigInteger[] smoothPowerVector = new BigInteger[smoothness];
		BigInteger remainder = new BigInteger(someNum.toString());
		
		if (someNum.compareTo(BigInteger.ZERO) == 1)
		{
			for (int i = 0; i < smoothness; i++)
			{
				smoothPowerVector[i] = BigInteger.ZERO;
				
				while (remainder.mod(primes[i]).compareTo(BigInteger.ZERO) == 0)
				{
					smoothPowerVector[i] = smoothPowerVector[i].add(BigInteger.ONE);
					remainder = remainder.divide(primes[i]);
				}
			}
		}
		
		if (remainder.compareTo(BigInteger.ONE) == 0)
		{
			return smoothPowerVector;
		}
		else
		{
			return null;
		}
	}
	
	private static boolean areCoprime(BigInteger numOne, BigInteger numTwo)
	{
		return (numOne.gcd(numTwo).compareTo(BigInteger.ONE) == 0);
	}
	
	public static void main (String[] args)
	{
		ModFactor breaker = new ModFactor((new BigInteger("32351")),
												  (new BigInteger("17")),
												  (new BigInteger("47920243267321")), 
												  50);
	}
}
