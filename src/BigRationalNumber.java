import java.math.BigInteger;

public class BigRationalNumber 
{
	private BigInteger numerator;
	private BigInteger denominator;
	
	public BigRationalNumber(BigInteger numerator)
	{
		this.numerator = numerator;
		this.denominator = BigInteger.ONE;
		
		reduce();
	}
	
	public BigRationalNumber(BigInteger numerator, BigInteger denominator)
	{
		this.numerator = numerator;
		this.denominator = denominator;
		
		reduce();
	}
	
	public void add(BigInteger someNumber)
	{
		numerator = numerator.add(someNumber.multiply(denominator));
		reduce();
	}
	
	public void add(BigRationalNumber someRationalNumber)
	{
		numerator = numerator.multiply(someRationalNumber.getDenominator()).add(
					someRationalNumber.getNumerator().multiply(denominator));
		denominator = denominator.multiply(someRationalNumber.getDenominator());
		reduce();
	}
	
	public void subtract(BigInteger someNumber)
	{
		numerator = numerator.subtract(someNumber.multiply(denominator));
		reduce();
	}
	
	public void subtract(BigRationalNumber someRationalNumber)
	{
		numerator = numerator.multiply(someRationalNumber.getDenominator()).subtract(
					someRationalNumber.getNumerator().multiply(denominator));
		denominator = denominator.multiply(someRationalNumber.getDenominator());
		reduce();
	}
	
	public void multiply(BigInteger someNumber)
	{
		numerator = numerator.multiply(someNumber);
		reduce();
	}
	
	public void multiply(BigRationalNumber someRationalNumber)
	{
		numerator = numerator.multiply(someRationalNumber.getNumerator());
		denominator = denominator.multiply(someRationalNumber.getDenominator());
		reduce();
	}
	
	public void divide(BigInteger someNumber)
	{
		denominator = denominator.multiply(someNumber);
		reduce();
	}
	
	public void divide(BigRationalNumber someRationalNumber)
	{
		numerator = numerator.multiply(someRationalNumber.getDenominator());
		denominator = denominator.multiply(someRationalNumber.getNumerator());
		reduce();
	}
	
	public BigInteger getNumerator()
	{
		return numerator;
	}
	
	public BigInteger getDenominator()
	{
		return denominator;
	}
	
	private void reduce()
	{
		BigInteger gcd = findGCD(numerator, denominator);
		
		numerator = numerator.divide(gcd);
		denominator = denominator.divide(gcd);
	}
	
	public static BigInteger findGCD(BigInteger numOne, BigInteger numTwo)
	{
		if (numOne.compareTo(numTwo) == 0)
		{
			return numOne;
		}
		else
		{
			BigInteger smallNum = numOne.min(numTwo);
			BigInteger largeNum = numOne.max(numTwo);
			
			BigInteger remainder = largeNum.mod(smallNum);
			
			if (remainder.compareTo(BigInteger.ZERO) == 0)
			{
				return smallNum;
			}
			else
			{
				return findGCD(remainder, smallNum);
			}
		}
	}
}
