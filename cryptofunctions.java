package com.utils.crypto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class cryptofunctions {

	//square and multiply function takes 3 bigintegers - u ,p (Mod),exp (Exponent)
	public static BigInteger sqandmul(BigInteger u,BigInteger p,BigInteger exp)
	{
		BigInteger result,m,b=BigInteger.ZERO,A=BigInteger.ONE,twopow=new BigInteger("2"),prevA=BigInteger.ONE;
		int mul=0;
		
		//Handling cases where exponent is ONE.
		if(exp.compareTo(BigInteger.ONE)==0)
		{
			result = u.modPow(exp, p);
			System.out.println("This is not for exponent 1");
			System.exit(0);
		}		
		m=exp;
		if(m.compareTo(p)==1)
		{
			exp=exp.mod(p.subtract(BigInteger.ONE));
		}
		System.out.println("");
		//System.out.println("m\tb\tu\tA\t");
		for(int i=0;exp.compareTo(BigInteger.ONE)==1;i++) 
		{
			if(i==0) //handling case where i=0; This is where the 1st row in the table gets created
			{
				m=exp; //m is given exp's value
				b=m.mod(new BigInteger("2")); //b:=m (mod2)
				//b=m.remainder(new BigInteger("2")); //b:=m (mod2)
				twopow=new BigInteger("2").pow(i);
				//u=u.modPow(twopow, p);  //u:=(u^2) modp
				u=u.pow(twopow.intValueExact()).remainder(p); //u:=(u^2) modp
				if(b.compareTo(BigInteger.ONE)==0)
				{
					A=(prevA.multiply(u)).remainder(p);
					mul++;
					prevA=A;
				}
				else
				{
					A=prevA;
				}
				//System.out.println(m+"\t"+b+"\t"+u+"\t"+A);
			}
			else //After the 1st row is created, the code in else block will execute
			{
				m=(exp.subtract(b)).divide(new BigInteger("2")); //m:=(m-b)/2
				//b=m.mod(new BigInteger("2")); //b:=m (mod2)
				b=m.remainder(new BigInteger("2")); //b:=m (mod2)
				//u=u.modPow(new BigInteger("2"), p); //u:=(u^2) modp
				u=u.pow(2).remainder(p);//u:=(u^2) modp
				if(b.compareTo(BigInteger.ONE)==0) // Checking if b's value is one.
				{//if b=1, then we take previously obtained value of A and multiply with u obtained above
					A=(prevA.multiply(u)).mod(p);
					//A=(prevA.multiply(u)).remainder(p);
					mul++; //this is a counter for counting number of multiplications 
					prevA=A; //Storing current A's value in "prevA"
				}
				else //if b's value is 0, then we just carry forward the current prevA to current A
				{
					A=prevA;
				}
				//System.out.println(m+"\t"+b+"\t"+u+"\t"+A);
				exp=m;  //Sending new value of exp as m. This will be used in next iteration
			}		
		}
		//System.out.println("The ans is congruent to "+A+" mod "+p+" and the calculations take "+mul+" multiplications!");
		int[] a = {A.intValue(),mul}; //returning INT array with inverse and number of multiplications
		return A;
	}
	//Generator gets two inputs
	//1. arraylist of prime numbers
	//2. prime number
	public static ArrayList<BigInteger> generators(ArrayList<BigInteger> primeno, BigInteger pn ) {
		Set<BigInteger> s = new LinkedHashSet<>(primeno); //used Set to created a sorted list 
		BigInteger bi=pn.subtract(BigInteger.ONE);
		ArrayList<BigInteger> generators = new ArrayList<BigInteger>();
		for (BigInteger i=new BigInteger("2");i.compareTo(new BigInteger("50"))==-1;) { //i starts from 2 and goes till 50, generating numbers within 50
			ArrayList<BigInteger> possiblegenerators = new ArrayList<BigInteger>();
			for (BigInteger bintegerinner : s) {
				//BigInteger result = i.modPow(bi.divide(bintegerinner), pn);
				BigInteger result = sqandmul(i,pn,bi.divide(bintegerinner)); 
				//BigInteger result = i.pow((bi.divide(bintegerinner)).intValueExact()).remainder(pn);
				possiblegenerators.add(result);				
			}
			if(!(possiblegenerators.contains(BigInteger.ONE)))// we are checking if a generator is bad.
			{
				generators.add(i); // We add only the generators which do not have result as ONE. result = i.modPow(bi.divide(bintegerinner), pn)
			}
			i=i.nextProbablePrime(); // here we are taking only primes. This method is present in BigInteger class. It increases efficiency of the program.			
		}		
		return generators;		
	}

	public static ArrayList<BigInteger> PrimeFactorization(BigInteger primeno) 
	{
		ArrayList<BigInteger> primes = new ArrayList<BigInteger>(); // list of prime factors in the prime factorization
		BigInteger i = new BigInteger("2");
		while (i.compareTo(primeno) <= 0) { //running while loop until the value of i reaches prime number 
			if (primeno.mod(i).equals(BigInteger.ZERO)) {  //checking if prime number is divisible by i
				primes.add(i); //Adding i into prime number arraylist
				primeno = primeno.divide(i); 
				i = new BigInteger("2"); 
			} else {
				if (i.equals(new BigInteger("2"))) {
					i = i.add(BigInteger.ONE); 
				} else {
					i = i.add(new BigInteger("2"));
				}
			}
		}
		if (!primeno.equals(BigInteger.ONE)) primes.add(primeno);
		Collections.sort(primes);
		return primes;
	}
	//Fish Algo takes 2 inputs - 1) a	2)b 
	//ax=1(modb) where gcd(a,b)=1.
	public static BigInteger FISHAlgo(BigInteger a, BigInteger b) {
		BigInteger u = a;
		BigInteger v = b;
		BigInteger  x3, y3;
		if (u.compareTo(v) ==-1) {
			x3 = v; 
			y3 = u;
		} else { 
			x3 = u; 
			y3 = v;
		}
		//creating 2 arraylist. Since arraylist expand and need ot be given any size while initializing
		ArrayList<BigInteger> list1 = new ArrayList<BigInteger>();
		ArrayList<BigInteger> list2 = new ArrayList<BigInteger>();
		System.out.println("Calc inverse!!");
		//setting value of a and b in the grid.
		list1.add(v);
		list1.add(u);
		//setting value ZERO is the 2nd row
		list2.add(BigInteger.ZERO);
		while (y3.compareTo(BigInteger.ONE) == 1) { // Looping till y3 becomes ONE
			BigInteger t1=x3.divide(y3); //(x3/y3)	taking the quotient and putting it in list2
			list2.add(t1);
			BigInteger t2=x3.remainder(y3); //(x3/y3) taking the remainder and putting it adjacent in list1
			list1.add(t2); 
			x3=y3; //Assigning new value to x3
			y3=t2; //Assigning new value to y3
		}
		list2.add(BigInteger.ZERO);
		int l2size=list2.size(); 
		BigInteger list3[] = new BigInteger[l2size]; // creating the 3rd row array with the help of the size of list1 or list2 
		int l3size=list3.length;
		list3[l3size-1]=BigInteger.ZERO; // Assigning the last column of 3rd row to 0
		list3[l3size-2]=BigInteger.ONE; // Assigning the 2nd last column of 3rd row to 1
		int j=1;
		BigInteger up,down,side;
		for(int i=l3size-1; i>=0;i--)
		{
			up=list2.get(i-j); // taking up,down and side value starting from extreme right of the grid
			down=list3[i-j];
			side=list3[i];
			if(i-j-1<0) break;
			list3[i-j-1]=(up.multiply(down)).add(side);
		}
		System.out.println();
		//printing out arraylists
		/*
		Iterator<BigInteger> itr = list1.iterator();
		while (itr.hasNext()) {
			BigInteger element = itr.next();
			System.out.print(element + "\t");
		}
		System.out.println();
		Iterator<BigInteger> itr1 = list2.iterator();
		while (itr1.hasNext()) {
			BigInteger element = itr1.next();
			System.out.print(element + "\t");
		}
		System.out.println();
		for(BigInteger n: list3) {
			System.out.print(n+"\t");
		}
		System.out.println();
		*/
		//checking for number of columns. If its even, then the bottom leftmost value is the inverse
		if(l3size%2==0)
		{
			System.out.println("Checking - "+ (list3[0].multiply(u)).mod(v));
			return  list3[0]; 
		}
		else //checking for number of columns. If its odd, then the bottom leftmost value must be subtracted from v to get the inverse
		{
			System.out.println("Checking - "+ ((v.subtract(list3[0])).multiply(u)).mod(v));
			return  v.subtract(list3[0]);			
		}
	}
	//GCD Takes 2 bigintegers and returns the output
	public static BigInteger getGcd(BigInteger b, BigInteger d)
	{
		BigInteger gcd = BigInteger.ONE,i= BigInteger.ONE,j= BigInteger.ONE;
		if(b.compareTo(d)==1)//checks if b>d
		{
			for(i = d; i.compareTo(BigInteger.ONE)==1;)
			{
				if(b.remainder(i).compareTo(BigInteger.ZERO)==0 && d.remainder(i).compareTo(BigInteger.ZERO)==0) //checking if b%i and d%i == 0
				{
					return i;
				}
				i=new BigInteger(Integer.toString((i.intValue()-1)));
			}
		}
		else
		{
			for( j = b; j.compareTo(BigInteger.ONE)==1;)
			{
				if(b.remainder(j).compareTo(BigInteger.ZERO)==0 && d.remainder(j).compareTo(BigInteger.ZERO)==0) //checking if b%j and d%j == 0
				{
					return j;
				}
				j=new BigInteger(Integer.toString((j.intValue()-1)));
			}
		}   
		return gcd;
	}
}
