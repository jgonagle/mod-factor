import java.math.BigInteger;

public class SmoothPair 
{
	private BigInteger exponent;
	private BigInteger[] smoothPowerVector;
	
	public SmoothPair(BigInteger exponent, BigInteger[] smoothPowerVector)
	{
		this.exponent = exponent;
		this.smoothPowerVector = smoothPowerVector;
	}
	
	public BigInteger getExponent()
	{
		return exponent;
	}
	
	public BigInteger[] getSmoothPowerVector()
	{
		return smoothPowerVector;
	}
	
	public void printSmoothPair()
	{
		System.out.print("(" + exponent + "): [");
		
		for (int h = 0; h < smoothPowerVector.length; h++)
		{
			System.out.print(smoothPowerVector[h]);
			
			if ((h + 1) == smoothPowerVector.length)
			{
				System.out.println("]");
			}
			else
			{
				System.out.print(",");
			}
		}
	}
}
