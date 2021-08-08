# Season Calendar

The season calendar is stored in the file `STRINFO.DAT` in 508 byte blocks.

`calendar` structure: 

```
0   24  char[24]          name    
24  4   int               region  
28  20  date              race_date    
48  4   int               track_sprite (offset into f1_e.rsc)
52  4   int               
56  4   int               laps_race
60  4   int               movie_id
64  4   int               lap_length (in meters)
68  4   int               race_distance (in meters) 
72  4   int     
76  4   int     
80  40  char[40]          country 
120 132 calendar_winner   last_winner 
252 60  char[60]          feature1    
312 60  char[60]          feature2    
372 60  char[60]          feature3    
432 60  char[60]          feature4    
492 4   int     
496 4   int     
500 4   int     
504 4   int     
```

`date` structure:
[date](../basic/date.md)

`calendar_winner` structure:

```
0   40  char[40]    first   
40  4   char[4]     first_short 
44  40  char[40]    second  
84  4   char[4]     second_short    
88  40  char[40]    third   
128 4   char[4]     third_short 
```