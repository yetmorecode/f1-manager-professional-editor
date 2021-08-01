# Drivers 

Drivers are stored in the file ```FAHRER.DAT``` in 568 byte blocks.

`driver` structure:

```
0   40  char[40]    fullname    
40  20  char[20]    shortname   
60  4   int original_salary 
64  4   int original_point_bonus    
68  4   int original_title_bonus    
72  4   int testings_driven 
76  4   int races_driven    
80  4   int races_finished  
84  4   int     
88  4   int participant_index   
92  4   int team    
96  4   int next_season_team    
100 4   int     
104 4   int height  
108 4   int country 
112 4   int age 
116 4   int weight  
120 4   int total_points    
124 4   int poles   
128 4   int fastest_laps    
132 4   int total_wins  
136 4   int worldtitles 
140 4   int accidents   
144 4   int contract_left_months    
148 4   int salary  
152 4   int under_contract  ?
156 4   int point_bonus 
160 4   int title_bonus 
164 4   int intelligence    
168 4   int talent  
172 4   int character   1-5
176 4   int experience  
180 4   int hidden_stat ?
184 4   int fitness 
188 4   int concentration   
192 4   int motivation  
196 4   int aggressiveness  
200 4   int     
204 4   int     
208 4   int     
212 4   int image_index 
216 4   int start_no    
220 72  int[18] points  
292 72  int[18]     
364 144 driver_race_finish[18]  positions   
508 4   int birth_month 
512 4   int end_career_soon 
516 4   int     
520 4   int no_negotiation  
524 4   int     
528 4   int     
532 4   int     
536 4   int     
540 4   int     
544 4   int     
548 4   int     
552 4   int     
556 4   int     
560 4   int     
564 4   int is_active   
```

`driver_race_finish` structure:

```
0   4   int position    
4   4   int status  (dropout reason, etc)
```

`load_drivers` function (decompiled):

```c
void load_drivers(void)

{
  char *filename;
  int length;
  char *mode;
  undefined4 *from;
  driver *to;
  byte bVar1;
  undefined4 driver_buffer [142];
  int i;
  
  bVar1 = 0;
  filename = sprintf_data97e_filename("FAHRER.DAT");
  fd_drivers = file_open(filename,mode);
  i = 1;
  while( true ) {
    
    // read a single driver (0x238) from file
    length = file_read((byte *)driver_buffer,1,0x238,fd_drivers);
    if (length == 0) break;
    
    // copy into global driver array
    length = 0x8e;  (0x8e dwords = 0x238 bytes)
    from = driver_buffer;
    to = &drivers + i;
    while (length != 0) {
      length = length + -1;
      *(undefined4 *)to->fullname = *from;
      from = from + (uint)bVar1 * -2 + 1;
      to = (driver *)((int)to + ((uint)bVar1 * -2 + 1) * 4);
    }
    
    // init some fields
    (&drivers)[i].field_0xcc = 1;
    (&drivers)[i].participant_index = -0x1;
    (&drivers)[i].team = 0;
    
    i = i + 1;
  }
  file_close(fd_drivers);
  return;
}
```