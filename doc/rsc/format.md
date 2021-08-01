# PROLINE Resource File Format

This specification refers to resource archives as used by the game F1 manager professional. The file extension is *.rsc.

## Signature

The resource archives start with the signature "PROLINE Resource File  (c) 1997 by PROLINE Software GmbH"

![Alt text](signature.png?raw=true "Signature")

## General Structure

The different resources within a single archive are referenced directly by the executable, leaving the file itself without any
map of itself. It is impossible to reconstruct the resource without reverse engineering their offsets from the game executable itself. However, it's not too hard to make sense of them.

So far I've seen three different resource types used:

* [VGA color palettes](#vga-color-palettes)
* [PCX encoded images](#pcx-images)
* [Transparent sprites (variable size)](#sprites)

Byte order is little-endian, e.g. a dword of value 0x12345678 would be found as byte sequence "78 56 34 12" in the file, a word value 0x1234 as "34 12", etc.

It is of help to have a simple understanding of [VGA colors](https://bos.asmhackers.net/docs/vga_without_bios/docs/palettesetting.pdf) and [VESA](https://en.wikipedia.org/wiki/VESA_BIOS_Extensions) / [the framebuffer](https://en.wikipedia.org/wiki/Framebuffer).

## VGA color palettes

![Alt text](palette_example.png?raw=true "Signature")

Color palettes are described by their size followed by the 256 rgb colors as supposed to be fed into the VGA DAC (256 * 3 bytes).

```
ddw size
db COLOR0_R
db COLOR0_G
db COLOR0_B
db COLOR1_R
db COLOR1_G
db COLOR1_B
db COLOR2_R
db COLOR2_G
db COLOR2_B
...
```

So far I've only seen full color palettes of ```size == 0x300``` (256 * 3). Some of those colors are dynamically overwritten when the palette is loaded, for example color 0  is usually cleared out to black or team specific colors are loaded into some part of the palette.

The color palette for the main menu background is found at 0x1014d36 (0x300 bytes in size) and shown below:

![Alt text](palette.png?raw=true "Palette")

Below is an decompiled and annotated excerpt from the code used for loading palettes from resource files:

![Alt text](palette_code.png?raw=true "Palette Loading Code")

- At line 17 ```size``` is read.
- At line 18 The palette itself of given ```size``` is read
- Below that point you can see how certain colors are overwritten (lines 23-25 or 34 to 36), or how a whole block of colors is overwritten (lines 28-31).

The full function is quite more complex and seems to handle a lot of dynamic edge cases I've not been able to fully understand yet.

## PCX encoded images

Note: Turns out this is just [PCX file format](http://www.shikadi.net/moddingwiki/PCX_Format).

![Alt text](backgrounds.png?raw=true "Tileset Loading Code")

```
ddw      SIZE
db[128]  PCX HEADER
db[SIZE-128] PCX IMAGE DATA
```

```SIZE``` describes the actual size of header + image data in the resource file. 

The data is RLE encoded. This can be seen for example in the mainmenu background image (offset 0xa76d4d, palette 0x1014d36):

![Alt text](fullscreen_packing.png?raw=true "Fullscreen Packing")

The pixel data highlighted of ```c3 31 c4 33``` will actually be unpacked to ```31 31 31 33 33 33 33``` as seen in the decompliation below:

![Alt text](fullscreen_code.png?raw=true "Fullscreen Image Loading Code")

- Lines 56-57 open the file and seek to the resource offset
- Lines 58 reads the size of the complete resource (header + image data)
- Lines 59-63 allocate memory for the resource and read it
- Line 66 sets up the first 128 bytes (0x80) as a header structure
- Line 67 advances ```resource_buffer_offset``` to the actual pixel data
- Lines 69 and following loop through the pixel data
- Lines 73 - 79 unpack a byte if the 6th bit is set
- Lines 80-85 write a simple color value (without unpkacing)

## Sprites

The game can also load sets of same-sized transparent sprites as one logical resource (e.g. a list of flags, different states of buttons, an graphical alphabet, etc.). Those sprites are found with a 16 byte header, containing an unknown dword, the width and height for all images forund and the number of images in the map (each as dword). Followed by the actual list of items, each consisting of an dword describing its data size followed by the actual data.

```
ddw                 unknown
ddw                 width
ddw                 height
ddw                 number of items
ddw                 item1_data_size
db[item1_data_size] item1_data
ddw                 item2_data_size
db[item2_data_size] item2_data
...
```

![Alt text](tileset_code.png?raw=true "Tileset Loading Code")

Despite the above code just copying the pixel data directly from the resource file as it is, it is not suitable for directly writing it to the screen (i.e. the framebuffer). The encoded image data can "skip" pixels in the framebufer by using the below described encoding scheme. This is used for creating various shapes and transparent areas. 

![Alt text](tileset_skip.png?raw=true "Tileset Loading Code")

The black area in the lower left corner of the poster asset will actually not be drawn on the screen, but skipped when drawn on the framebuffer. If a color byte of ```0``` is encountered in the item's pixel data, the next byte will be read as ```skip_count``` and ```skip_count``` pixels will be skipped in the framebuffer when drawing the image.

Some more examples of this technique used (with more wrong palettes ;)). The black areas will actually not be drawn on the screen:

![Alt text](tileset_skip2.png?raw=true "Tileset Loading Code")

## Other assets

There will sure be more assets to be found, espcially those fancy 3D models for the tracks. Just not there yet.
