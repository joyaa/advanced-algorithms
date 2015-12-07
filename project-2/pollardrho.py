import random
import time
import fileinput
import sys
from collections import deque 

def factoring(N):
    primes = []
    temps = deque()
    temps.append(N)
    while temps:
        f = temps.pop()
        if isprime(f): 
            primes.append(f)
            continue
        d = pollard_rho(f)
        if (d == f):
            temps.append(d)
        else:
            temps.append(d)
            temps.append(f/d)
    return primes

def pollard_rho(N):
    if(N % 2 == 0):
        return 2
    x=random.randint(0, N-1) #small integers
    p=random.randint(0, N-1)
    y=x 
    d=1
    while d==1:
        x = ((x*x)+p)%N 
        y = (y*y+p)%N
        y = (y*y+p)%N
        d=gcd(y-x, N)
        if d == N:
            return d
    return d

'''
def isprime(x):
    #if x < 2: return False
    if x == 2: return True
    if x < 2 or x%2 == 0: return False

    for i in range (2, int(x**0.5)+1):
        if x % i == 0:
            return False
    return True                                                 
'''
#Miller-Rabbin
def isprime(x):
        if(x<2):
                return False
        if(x!=2 and x%2==0):
                return False
        s=x-1
        while(s%2==0):
                s>>=1
        for i in xrange(10):
                a=1+random.randint(1, x-1)
                temp=s
                mod=pow(a,temp,x)
                while(temp!=x-1 and mod!=1 and mod!=x-1):
                        mod=(mod*mod)%x
                        temp=temp*2
                if(mod!=x-1 and temp%2==0):
                        return False
        return True


#Calculate (a ** b) % c
def powfunc(a,b,c):
    x = 1
    while(b>0):
        if(b&1==1): 
            x = (x*a)%c
        a=(a*a)%c
        b >>= 1
    return x

def gcd(x, y):
    while y:
        x, y = y, x%y
    return abs(x)
'''
# Iterative binary algorithm ???
def gcd(x, y):
    x = abs(x)
    if x == 0:
        return y
    #y >= x > 0
    k = 1
    while y&1==0 and x&1==0: #both even
        y>>=1
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
'''

start = time.clock()
for line in fileinput.input():
    line = int(line)
    if line == 0:
        break
    primes = factoring(line)
    primes.sort()
    #for i in range(0, len(primes)):
    i = 0
    while i < len(primes):
        exp = primes.count(primes[i])
        print primes[i],
        sys.stdout.softspace=0
        print "^",
        sys.stdout.softspace=0
        print exp,
        i += exp
    print 
time = (time.clock()-start)
print time
'''
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
time = (time.clock()-start)/100
print time
#'''