# Track Models 

Track models are stored in the file ```STRECKEN.DAT``` in 568 byte blocks.

`track` structure:

```
0   60  char[60]    name    
60  12  char[12]    camera_name 
72  200 track_unknown[25]   unk 
272 4   int fuel    
276 4   int spriteImage 
280 4   int spriteTrackscreen   
284 4   int segments_total  
288 4   float       
292 4   int     
296 4   int unk_index   
300 4   int padding2    
304 4   int     
308 4   int     
312 4   int     
316 4   int     
320 140000  track_segment[7000] segments    
140320  15840   track_pitlane[1320] pitlane 
156160  4   int padding1    
156164  4   int pitBegin    
156168  4   int pitExit 
156172  4   int     
156176  4   int     
156180  240 track_camera[30]    cameras 
156420  4   int unused  
156424  4   track_number    track_number    
156428  48  int[12] garages 
156476  4   int weather 
156480  4   int spriteStandings 
156484  4   int region  
156488  4   int pitlane_start   
156492  4   int pitlane_end 
```

`track_camera` structure:

```
0   4   int number  
4   4   int position    (which segment))
```

`track_segment` structure:

```
0   4   int x   
4   4   int y   
8   4   int z   
12  4   int racing_line_a   
16  4   int racing_line_b   
```

`track_pitlane` structure:

```
0   4   int x   
4   4   int y   
8   4   int z   
```