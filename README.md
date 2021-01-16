# TCP Socket Programming in JAVA


### Description
This is a program that involves a client and a server connected by a TCP Socket.

The client sends server 4 values, for example X, n, B, C
- X is the adjacency matrix of a directed graph with 5 nodes A B C D E 
- n is the length of the path from node B to node C.
- 
The server responds back with two responses:
- positive Y response (or negative N response) if there exists (or doesn't exist) a path of length n from B to C.
- the image of the directed graph with nodes A B C D E proving the validity of the response.

The server uses the Java Graphics module to build the graph and captures an image of it sending it to the client in raw form. The client receives the raw image and displays it in a Java Applet.


## Example
### Input (on server)
>  X :
> |   | A | B | C | D | E |
> | ----- | ----- | ----- | ----- | ----- | ----- |
> | A |  | 0 | 1 | 1 | 1 | 0 |
> | B | 0 | 0 | 0 | 1 | 1 |
> | C | 1 | 0 | 0 | 0 | 1 |
> | D | 0 | 0 | 0 | 0 | 0 |
> | E | 1 | 1 | 0 | 1 | 0 |

> n: 2

> B: C

> C: D

### Result (on client)

<img src="https://github.com/ishitb/TCP_Sockets_Java/blob/main/Result.png"/>
