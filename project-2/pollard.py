import random
N=20

def pollard (N):
	i=1
	x=random.randint(0, N-1) #small integers
	y=x	
	k=2
	d=1
	while d==1 or d==N:
		i=i+1
		x = (x*x-1)%N	
		d=gcd(abs(y-x), N)
		if i==k:
			y=x
			k=2*k
	print d
	return d


def gcd(diff,N):
	while N:
		diff, N = N, diff%N
	return diff

def gcdR(diff, N):
	return gcd(N, diff % N) if N else abs(diff)

pollard(N)
