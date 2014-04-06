/**
 *  Java Grinder
 *  Author: Michael Kohn
 *   Email: mike@mikekohn.net
 *     Web: http://www.mikekohn.net/
 * License: GPL
 *
 * Copyright 2014 by Michael Kohn
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

#include "TI84C.h"
#include "Z80.h"

#define BCALL(a) \
  fprintf(out, "  rst 0x28\n"); \
  fprintf(out, "  .db %s&0xff, %s>>8\n", #a, #a);
  //fprintf(out, "  .db %s>>8, %s&0xff\n", #a, #a);

TI84C::TI84C()
{
}

TI84C::~TI84C()
{
}

int TI84C::open(const char *filename)
{
  if (Z80::open(filename) != 0) { return -1; }

  //fprintf(out, ".include \"ti84c.inc\"\n\n");
  fprintf(out, ".include \"ti84plus.inc\"\n\n");
  fprintf(out, "ram_start equ appData\n");
  fprintf(out, "heap_ptr equ ram_start\n");

  return 0;
}

int TI84C::start_init()
{
  fprintf(out, ".org 0x4000\n");

  // http://wikiti.brandonw.net/index.php?title=84PCSE:OS:Applications
  fprintf(out,
    "  ;Header (128 bytes)\n"
    "  .db 0x80, 0x0f\n"
    "  .db 0x00, 0x00, 0x00, 0x00\n"
    "  .db 0x80, 0x12\n"
    //"  .db 0x01, 0x0f\n"
    "  .db 0x01, 0x04\n"
    "  .db 0x80, 0x21\n"
    "  .db 0x01\n"
    "  .db 0x80, 0x31\n"
    "  .db 0xa1\n"
    "  .db 0x80, 0x48\n"
    "  .db \"HelloApp\" ;make sure this is 8 bytes\n"
    "  .db 0x80, 0x81\n"
    "  .db 0x01\n"
    "  .db 0x80, 0x90\n"
    "  .db 0x03, 0x26, 0x09, 0x04\n"
    "  .db 0x1e, 0xff, 0x2b, 0x57\n"
    "  .db 0x02, 0x0d, 0x40, 0xa1, 0x6b, 0x99, 0xf6, 0x59, 0xbc, 0x67\n"
    "  .db 0xf5, 0x85, 0x9c, 0x09, 0x6c, 0x0f, 0xb4, 0x03, 0x9b, 0xc9\n"
    "  .db 0x03, 0x32, 0x2c, 0xe0, 0x03, 0x20, 0xe3, 0x2c, 0xf4, 0x2d\n"
    "  .db 0x73, 0xb4, 0x27, 0xc4, 0xa0, 0x72, 0x54, 0xb9, 0xea, 0x7c\n"
    "  .db 0x3b, 0xaa, 0x16, 0xf6, 0x77, 0x83, 0x7a, 0xee, 0x1a, 0xd4\n"
    "  .db 0x42, 0x4c, 0x6b, 0x8b, 0x13, 0x1f, 0xbb, 0x93, 0x8b, 0xfc\n"
    "  .db 0x19, 0x1c, 0x3c, 0xec, 0x4d, 0xe5, 0x75\n"
    "  .db 0x80, 0x7f\n"
    "  .db 0x00, 0x00, 0x00, 0x00\n"
    "  .db 0x00, 0x00, 0x00, 0x00\n"
    "  .db 0x00, 0x00, 0x00, 0x00\n"
    "  .db 0x00, 0x00, 0x00, 0x00\n"
    "  .db 0x00, 0x00, 0x00, 0x00\n\n");

  // Add any set up items (stack, registers, etc).
  fprintf(out, "start:\n");

  return 0;
}

int TI84C::ti84c_clearScreen()
{
  BCALL(_ClrLCDFull);

  return 0;
}

int TI84C::ti84c_clearRect()
{
  fprintf(out, "  pop hl\n");
  fprintf(out, "  pop de\n");
  fprintf(out, "  pop bc\n");
  fprintf(out, "  ld b,c\n");
  fprintf(out, "  pop bc\n");
  BCALL(_ClearRect);
  stack -= 4;

  return 0;
}

int TI84C::ti84c_drawLine()
{
  fprintf(out, "  pop de\n");
  fprintf(out, "  pop bc\n");
  fprintf(out, "  pop ix\n");
  fprintf(out, "  pop hl\n");
  fprintf(out, "  ld b,l\n");
  fprintf(out, "  pop hl\n");
  fprintf(out, "  ld a,l\n");
  fprintf(out, "  ld hl,ix\n");
  BCALL(_ILine);
  stack -= 5;

  return 0;
}

int TI84C::ti84c_drawPoint()
{
  fprintf(out, "  pop de\n");
  fprintf(out, "  pop bc\n");
  fprintf(out, "  pop hl\n");
  fprintf(out, "  ld a,l\n");
  BCALL(_IPoint);
  stack -= 3;

  return 0;
}

int TI84C::ti84c_fillRect()
{
  fprintf(out, "  pop hl\n");
  fprintf(out, "  pop de\n");
  fprintf(out, "  pop bc\n");
  fprintf(out, "  ld b,c\n");
  fprintf(out, "  pop bc\n");
  BCALL(_FillRect);
  stack -= 4;

  return 0;
}

int TI84C::ti84c_print()
{
  fprintf(out, "  pop hl\n");
  BCALL(_PutS);
  stack--;

  return 0;
}

int TI84C::ti84c_printCenter()
{
  fprintf(out, "  pop hl\n");
  BCALL(_CenterPutS);
  stack--;

  return 0;
}

int TI84C::ti84c_printHL()
{
  BCALL(_DispHL);

  return 0;
}

int TI84C::ti84c_putc()
{
  fprintf(out, "  pop hl\n");
  fprintf(out, "  ld a,l\n");
  BCALL(_PutC);
  stack--;

  return 0;
}

int TI84C::ti84c_setCursorX()
{
  fprintf(out, "  pop af\n");
  fprintf(out, "  ld (curCol), a\n");
  stack--;

  return 0;
}

int TI84C::ti84c_setCursorY()
{
  fprintf(out, "  pop af\n");
  fprintf(out, "  ld (curRow), a\n");
  stack--;

  return 0;
}

int TI84C::ti84c_setDrawBGColor()
{
  fprintf(out, "  pop hl\n");
  fprintf(out, "  ld (penBGColor), hl\n");
  stack--;

  return 0;
}

int TI84C::ti84c_setDrawBGWhite()
{
  BCALL(_SetPenBG_White);

  return 0;
}


int TI84C::ti84c_setDrawColor()
{
  fprintf(out, "  pop hl\n");
  fprintf(out, "  ld (penFGColor), hl\n");
  stack--;

  return 0;
}

int TI84C::ti84c_setFillColor()
{
  fprintf(out, "  pop hl\n");
  fprintf(out, "  ld (fillRectColor), hl\n");
  stack--;

  return 0;
}

int TI84C::ti84c_setTextBGColor()
{
  fprintf(out, "  pop hl\n");
  fprintf(out, "  ld (curBGColor), hl\n");
  stack--;

  return 0;
}

int TI84C::ti84c_setTextColor()
{
  fprintf(out, "  pop hl\n");
  fprintf(out, "  ld (curFGColor), hl\n");
  stack--;

  return 0;
}


