### Data Race
Problem that occurs when two or more concurrent threads access the same memory location and at least one thread is modifying it

 
### Critical Section
Code segment that accesses a shared resource.
Keep protected sections of code as short as possible.

### Mutex (Lock)
Mechanism to implement mutual exclusion. Only one thread or process can possess at a time. Limits access to critical section.

### Atomic Operations
Execute as a single action, relative to other threads.
Cannot be interrupted by other concurrent threads.

### Deadlock
All processes and threads are unable to continue executing.

### Reentrant Mutex (Recursive Mutex/Lock)
Can be locked multiple times by the same thread.
Must be unlocked as many times as it was locked.

### Try Lock
Non-blocking lock/acquire method for mutex.

### Read-write lock
Use when there are much more reading than writing threads.

### Deadlock

### Starvation
A process or thread is perpetually denied the resource it needs.

### Livelock
Multiple threads or processes are actively responding to each other to resolve conflict, but that prevents them from making progress.

 

