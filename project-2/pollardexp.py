import random
import time
import fileinput
import sys
from collections import Counter
from collections import deque 
#N=238134299340340 #2^2 3^5 5^1 7^2
#N=9204051651*10**(60+0)+1

def factoring(N): #TODO: fix rabin miller check when d == f
	primes = []
	temps = deque()
	temps.append(N)
	while temps:
		f = temps.pop()
		if isprime(f): 
			primes.append(f)
			continue
		d = pollard(f)
		if (d == f):
			temps.append(d)
		else:
			temps.append(d)
			temps.append(f/d)
	return primes

def pollard (N):
	if(N % 2 == 0):
		return 2
	x=random.randint(0, N-1) #small integers
	p=random.randint(0, N-1)
	y=x	
	d=1
	while d==1:
		x = ((x)%N+p)%N # TEST BRENTS 	
		y = (((y)%N+p)^2+p)%N
		d=gcd_iter(y-x, N)
		if d == N:
			return d
	return d

def pollard_brent(N):
    if N%2==0:
        return 2
    y = random.randint(1, N-1)
    p = random.randint(1, N-1)
    m = random.randint(1, N-1)
    d,r,q = 1,1,1
    while d==1:
        x = y
        for i in range(r):
            y = ((y*y)%N+p)%N
            k = 0
        while (k<=r and d==1):
            ys = y
            for i in range(min(m,r-k)):
                y = ((y*y)%N+p)%N
                q = q*(abs(x-y))%N
            d = gcd_iter(q,N)
            k = k + m
        r = r*2
    if d==N:
		while True:
			ys = ((ys*ys)%N+p)%N
			d = gcd_iter(abs(x-ys),N)
			if d>1:
				break
    return d 

# Should look at Miller Rabbin instead (a probabilistic algorithm). 
#This is a simple trial division algorithm
'''def isprime(x):
	if x < 2: return False
	for i in range (2, int(x**0.5)+1):
		if x % i == 0:
			return False
	return True'''
def isprime(x):
	if(x<2):
		return False
	if(x!=2 and x%2==0):
		return False
  	s=x-1
  	while(s%2==0):
		s>>=1
  	for i in xrange(10):
		a=random.randrange(x-1)+1
		temp=s
		mod=pow(a,temp,x)
		while(temp!=x-1 and mod!=1 and mod!=x-1):
			mod=(mod*mod)%x
			temp=temp*2
		if(mod!=x-1 and temp%2==0):
			return False
	return True

def gcd2(x, y):
	while True:
		x %= y
		if x == 0:
			return y
		y %= x
		if y == 0:
			return x


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
		y>>=1;
		x>>=1
		k<<=1

	t = -x if y&1 else y
	while t:
		while t&1==0:
			t>>=1
		if t>0:
			y=t
		else:
			x=-t
		t=y-x
	return y*k
	

def gcdR(x, y):
	return gcd(x, x % y) if y else abs(x)

factors = []
for line in fileinput.input():
	line = int(line)
	factors.append(line)
	if line == 0:
		break

start = time.clock()
for j in range(0, 100):
	for line in factors:
		if line == 0:
			break
		primes = factoring(line)
		'''primes.sort()
		i = 0
		while i < len(primes):
			exp = primes.count(primes[i])
			print primes[i],
			sys.stdout.softspace=0
			print "^",
			sys.stdout.softspace=0
			print exp,
			i += exp'''
time = (time.clock()-start)/100
print time
