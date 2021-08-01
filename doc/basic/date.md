Dates throughout the game are represents as 20-byte structure:

```
0   4   dword   hour    
4   4   dword   day 
8   4   dword   month   
12  4   dword   year    
16  4   dword   millis  
```

```c
struct date {
    dword hour;
    dword day;
    dword month;
    dword year;
    dword millis;
};
```