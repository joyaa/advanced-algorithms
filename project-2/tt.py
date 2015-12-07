import random
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
        d = pollard_brent(f)
        if (d == f):
            temps.append(d)
        else:
            temps.append(d)
            temps.append(f/d)
    return primes


def pollard_brent(N):
    if N%2==0:
        return 2
    y = random.randint(1, N-1)
    p = random.randint(1, N-1)
    m = 1000
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
            d = gcd(q,N)
            k = k + m
        r <<= 1
    if d==N:
        while True:
            ys = ((ys*ys)%N+p)%N
            d = gcd(abs(x-ys),N)
            if d>1:
                break
    return d 

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
                mod=powfunc(a,temp,x)
                while(temp!=x-1 and mod!=1 and mod!=x-1):
                        mod=(mod*mod)%x
                        temp=temp*2
                if(mod!=x-1 and temp%2==0):
                        return False
        return True

def powfunc(a,b,c):
    x = 1
    while(b>0):
        if(b&1==1): x = (x*a)%c
        a=(a*a)%c
        b >>= 1
    return x%c

def gcd(x, y):
    while y:
        x, y = y, x%y
    return abs(x)

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
