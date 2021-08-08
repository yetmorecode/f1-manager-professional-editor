# Extending F1.exe

F1.exe is in Linear Executable Format with a single .code and .data segment (LE objects). Since references between those segments have relocation data, it is possible to fairly easy modify the .code and .data segments if needed.

Extending the .code segment is useful for fixing or replacing the original game logic.

Extending the .data segment is useful for adding support for more tracks, drivers, nationalities, etc.

## Linear Executable Objects

A LE-Executable describes a list of objects, each containing a list of pages to be mapped from the .exe file into memory (usually by a DOS extender as DOS4GW or dos32a). F1.exe has 3 objects defined:

 - object #1: .code
 - object #2: .debug
 - object #3: .data
 
When those objects are compiled and linked, the linker assumes a certain memory addresses for each object (as noted above).

## Extending Code

## Extending Data