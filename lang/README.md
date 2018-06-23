JIT - Just In Time compiler - a technique of code compilation during runtime as opposed to Ahead Of Time compilation (C, C++)

25% lines of VM is C1/C2 - two compilers (aka client (Swing apps) and server)

C1
    - fast compiler but the output code is not that fast (however much faster than the original one)
    - trivial methods up to 6 bytes (bytecode) are inlined by default (MaxTrivialSize)
    - and up 35 bytes (bytecode) for not hot code (MaxInlineSize)
    - uses linear registry allocation

C2
    - slower, generates faster code (compared to C1)
    - by default limited by bytecode size of 325 (FreqInlineSize)
    - uses graph coloring algorithm for registry allocation
    - based on templates (bytecode to machine code)

Tiered Compilation
    - enabled by default since Java 8
    - uses both compilers for better JVM start up time


```
[11:04:30] classes>java  org.am061.java.lang.LangApplication
Fibonacci number for 35 is 9227465, computed in 47 ms

[11:17:52] classes>java -Xint org.am061.java.lang.LangApplication
Fibonacci number for 35 is 9227465, computed in 2249 ms
```

The execution time can be even better than in case of C++
IO is slower in Java

JIT compiles methods, not classes
JIT does not like exceptions (! in the summary below) - it does not compile this code

```
[11:24:44] classes>java -XX:+PrintCompilation  org.am061.java.lang.LangApplication
     61    1       3       java.lang.String::hashCode (55 bytes)
     63    3       3       java.lang.String::charAt (29 bytes)
     63    2       3       java.lang.String::length (6 bytes)
     63    4     n 0       java.lang.System::arraycopy (native)   (static)
     64    5       3       java.lang.String::equals (81 bytes)
     64    6       3       java.lang.String::indexOf (70 bytes)
     64    7       3       java.lang.Object::<init> (1 bytes)
     65    8       3       java.lang.AbstractStringBuilder::ensureCapacityInternal (27 bytes)
     65   11       3       java.lang.Math::min (11 bytes)
     65    9       3       java.util.Arrays::copyOfRange (63 bytes)
     66   10       3       java.lang.String::<init> (82 bytes)
     67   12       3       java.lang.String::getChars (62 bytes)
     67   13       3       java.lang.String::indexOf (7 bytes)
     73   14       1       java.lang.ref.Reference::get (5 bytes)
     79   16       2       org.am061.java.lang.Fibonacci::fib (21 bytes)
     79   15       1       java.lang.ThreadLocal::access$400 (5 bytes)
     79   17       4       org.am061.java.lang.Fibonacci::fib (21 bytes)
     80   16       2       org.am061.java.lang.Fibonacci::fib (21 bytes)   made not entrant
    119   18       3       java.lang.Number::<init> (5 bytes)
    119   19       3       java.lang.Long::<init> (10 bytes)
    121   20       3       java.lang.System::getSecurityManager (4 bytes)
    123   21       3       java.util.HashMap::hash (20 bytes)
    124   22       3       sun.util.locale.LocaleUtils::isUpper (18 bytes)
    125   23       4       java.lang.String::charAt (29 bytes)
    126   24   !   3       java.io.BufferedReader::readLine (304 bytes)
    126    3       3       java.lang.String::charAt (29 bytes)   made not entrant
    127   25       3       java.lang.AbstractStringBuilder::append (50 bytes)
    127   27       3       java.lang.String::lastIndexOf (52 bytes)
    128   26       3       java.lang.StringBuilder::append (8 bytes)
    128   28       1       java.io.File::getPath (5 bytes)
    129   29       3       java.lang.String::startsWith (7 bytes)
    130   30       3       java.lang.Character::toLowerCase (9 bytes)
    130   31       3       java.lang.CharacterDataLatin1::toLowerCase (39 bytes)
    130   32       3       sun.nio.cs.UTF_8$Encoder::encode (359 bytes)JITWatch
    130   34       3       java.lang.String::startsWith (72 bytes)
    131   33       1       java.util.ArrayList::size (5 bytes)
    131   36     n 0       sun.misc.Unsafe::getObjectVolatile (native)   
    131   35       3       java.util.concurrent.ConcurrentHashMap::tabAt (21 bytes)
    132   37       3       java.io.BufferedInputStream::getBufIfOpen (21 bytes)
    132   38  s    3       java.io.BufferedInputStream::read (49 bytes)
    132   39       3       java.io.DataInputStream::readInt (72 bytes)
Fibonacci number for     
    134   40       1       java.lang.Object::<init> (1 bytes)
    134    7       3       java.lang.Object::<init> (1 bytes)   made not entrant
    134   41       3       java.util.HashMap$Node::<init> (26 bytes)
    134   42       3       java.lang.String::substring (79 bytes)
    135   43       3       java.util.HashMap::newNode (13 bytes)
    135   44       3       java.lang.AbstractStringBuilder::<init> (12 bytes)
35 is 9227465, computed in 42 ms
    135   45       3       java.util.concurrent.ConcurrentHashMap::spread (10 bytes)
```

Second column - compilation identifier
Third column - four levels of compilation 
    - 4 = C2
    - 1,2,3 = C1 - different levels of optimizations
    - 0 = interpreter

```
[11:57:01] classes>java  -XX:TieredStopAtLevel=3  org.am061.java.lang.LangApplication
Fibonacci number for 35 is 9227465, computed in 114 ms
```

A hot code - a code which reached invocation threshold

2000 invocations for C1
10000 invocations for C2


Optimizations are done based on two assumptions:
    - data is close to processor (cached)
    - sequential code executes much faster than code full of if's, function calls, etc


The loop JVM works in:
    - Interpreter
    - Profile the app for some time
    - Compile Hot Code and run it
    - If sth changed - Deoptimize - throw away the code 
