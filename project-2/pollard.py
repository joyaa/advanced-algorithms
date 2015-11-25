import random
N=238140 #6*42*3*5*7*9

def pollard (N):
	i=1
	x=random.randint(0, N-1) #small integers
	y=x	
	k=2
	d=1
	while d==1 or d==N:
		i=i+1
		x = (x*x+1)%N	
		d=gcd(y-x, N)
		if i==k:
			y=x
			k=2*k
	print d
	return d

#http://www.mathblog.dk/gcd-faceoff/
#http://www.csie.nuk.edu.tw/~cychen/gcd/Two%20fast%20GCD%20algorithms.pdf
#https://en.wikipedia.org/wiki/Binary_GCD_algorithm
def gcd(x, y):
	while y:
		x, y = y, x%y
	return abs(x)
# Iterative binary algorithm ???
def gcd_iter(x, y):
	x = abs(x)
	if x == 0:
		return y
	#y >= x > 0
	k = 1
	while y&1==0 and x&1==0: #both even
		y>>=1; x>>=1
		k<<=1

	t = -x if y&1 else y
	while t:
		while t&1==0:
			t>>=1
		if t>0:
			y=t
		else
			x=-t
		t=y-x
	return
	

def gcdR(x, y):
	return gcd(x, x % y) if y else abs(x)

pollard(N)
