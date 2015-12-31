import net.mikekohn.java_grinder.Atari2600;
import net.mikekohn.java_grinder.Memory;

public class Atari2600JavaDemo
{
  public static byte ship0[] =
  {
    (byte)0b00011000,
    (byte)0b00011000,
    (byte)0b10111101,
    (byte)0b11100111,
    (byte)0b11111111,
    (byte)0b10011001,
    (byte)0b00000000,
  };

  public static byte ship1[] =
  {
    (byte)0b10011001,
    (byte)0b11111111,
    (byte)0b11100111,
    (byte)0b10111101,
    (byte)0b00011000,
    (byte)0b00011000,
    (byte)0b00000000,
  };

  public static byte shot[] =
  {
    (byte)0b11111111,
    (byte)0b11111111,
    (byte)0b00000000,
  };

  public static byte background[] =
  {
    (byte)0b11110000, (byte)0b11111111, (byte)0b11111111,
    (byte)0b00010000, (byte)0b00000000, (byte)0b00000000,
    (byte)0b00010000, (byte)0b00000000, (byte)0b00000000,
    (byte)0b00010000, (byte)0b00000000, (byte)0b00000000,
    (byte)0b00010000, (byte)0b00000000, (byte)0b00000000,
    (byte)0b00010000, (byte)0b00000000, (byte)0b00000000,
    (byte)0b00010000, (byte)0b01111100, (byte)0b00000000,
    (byte)0b00010000, (byte)0b01111100, (byte)0b00000000,
    (byte)0b00010000, (byte)0b00111000, (byte)0b10000000,
    (byte)0b00010000, (byte)0b00000000, (byte)0b10000000,
    (byte)0b00010000, (byte)0b00000000, (byte)0b00000000,
    (byte)0b00010000, (byte)0b00000000, (byte)0b00000000,
    (byte)0b00010000, (byte)0b00000000, (byte)0b00000000,
    (byte)0b00010000, (byte)0b00000000, (byte)0b00011110,
    (byte)0b00010000, (byte)0b00111000, (byte)0b00111111,
    (byte)0b00010000, (byte)0b00111000, (byte)0b00111111,
    (byte)0b00010000, (byte)0b00000000, (byte)0b00011110,
    (byte)0b00010000, (byte)0b00000000, (byte)0b00000000,
    (byte)0b00010000, (byte)0b00000000, (byte)0b00000000,
    (byte)0b00010000, (byte)0b00000000, (byte)0b00000000,
    (byte)0b00010000, (byte)0b00000000, (byte)0b10000000,
    (byte)0b00010000, (byte)0b00111000, (byte)0b10000000,
    (byte)0b00010000, (byte)0b01111100, (byte)0b00000000,
    (byte)0b00010000, (byte)0b01111100, (byte)0b00000000,
    (byte)0b00010000, (byte)0b00000000, (byte)0b00000000,
    (byte)0b00010000, (byte)0b00000000, (byte)0b00000000,
    (byte)0b00010000, (byte)0b00000000, (byte)0b00000000,
    (byte)0b00010000, (byte)0b00000000, (byte)0b00000000,
    (byte)0b00010000, (byte)0b00000000, (byte)0b00000000,
    (byte)0b11110000, (byte)0b11111111, (byte)0b11111111,
    (byte)0b00000000, (byte)0b00000000, (byte)0b00000000,
  };

  public static byte nothing[] =
  {
    (byte)0b00000000
  };

  public static void main()
  {
    int ship0_x = 64;
    int shot0_y = 100;
    int ship1_x = 64;
    int shot1_y = 100;
    int rnd = 255;
    int frame = 0;
    int ship0_hit = 0;
    int ship1_hit = 0;
    int pf_hit = 0;
    int wait = 0;
    int score0 = 0;
    int score1 = 0;

    final int left = 12;
    final int right = 122;
    final int ship0_y = 51;
    final int ship1_y = 2;

    // missle width
    Memory.write8(0x04, (byte)16);
    Memory.write8(0x05, (byte)16);
    // reflect playfield
    Memory.write8(0x0a, (byte)1);

    Atari2600.setPlayer0Sprite(ship0);
    Atari2600.setPlayer1Sprite(ship1);
    Atari2600.setMissile0Sprite(shot);
    Atari2600.setMissile1Sprite(shot);
    Atari2600.setBallSprite(nothing);

    Atari2600.setColorPlayer0(0x88);
    Atari2600.setColorPlayer1(0x38);
    Atari2600.setColorPlayfield(0x06);

    Atari2600.setPlayfieldData(background);
    Atari2600.setPlayfieldLength((byte)background.length);

    Atari2600.setMissile0Position((byte)0, (byte)100);
    Atari2600.setMissile1Position((byte)0, (byte)100);

    while(true)
    {
      Atari2600.startVblank();

      Atari2600.setScore0((byte)score0);
      Atari2600.setScore1((byte)score1);

      if(wait > 0)
        wait--;

      if(wait == 0)
      {
        if(Atari2600.isJoystick0Right())
        {
          ship0_x += 1;
          if(ship0_x > right)
            ship0_x = right;
        }
        else if(Atari2600.isJoystick0Left())
        {
          ship0_x -= 1;
          if(ship0_x < left)
            ship0_x = left;
        }

        if(Atari2600.isJoystick0ButtonDown())
        {
          if(shot0_y == 100)
          {
            shot0_y = ship0_y;
            Atari2600.setAudioControl0((byte)0b0100);
            Atari2600.setAudioVolume0((byte)15);
          }
        }

        if(((frame & 3) == 3) && (ship1_x >= ship0_x || ship1_x == left))
        {
          if(shot0_y < 16)
            ship1_x++;
          else
            ship1_x--;
        }

        if(((frame & 3) == 3) && (ship1_x < ship0_x || ship1_x == right))
        {
          if(shot0_y < 16)
            ship1_x--;
          else
            ship1_x++;
        }

        if(ship1_x > right)
          ship1_x = right;
        if(ship1_x < left)
          ship1_x = left;
      }

      if(ship0_hit > 0)
      {
        Atari2600.setColorPlayer0(0x80 + ship0_hit);
        ship0_hit--;
        if(ship0_hit == 0)
        {
          Atari2600.setColorPlayer0(0x88);
          Atari2600.setAudioVolume1((byte)0);
        }
      }

      if(ship1_hit > 0)
      {
        Atari2600.setColorPlayer1(0x30 + ship1_hit);
        ship1_hit--;
        if(ship1_hit == 0)
        {
          Atari2600.setColorPlayer1(0x38);
          Atari2600.setAudioVolume0((byte)0);
        }
      }

      Atari2600.clearMotionRegisters();
      Atari2600.setPlayer0Position((byte)(ship0_x - 3), (byte)ship0_y);
      Atari2600.setPlayer1Position((byte)(ship1_x - 3), (byte)ship1_y);
      Atari2600.setMissile0Position((byte)ship0_x, (byte)shot0_y);
      Atari2600.setMissile1Position((byte)ship1_x, (byte)shot1_y);
      Atari2600.clearCollisionLatches();

      Atari2600.waitVblank();
      Atari2600.drawScreen();
      Atari2600.startOverscan();

      if(Atari2600.isCollisionMissile0Playfield())
      {
        pf_hit = 11;
        shot0_y = 100;
        Atari2600.setAudioVolume0((byte)0);
      }

      if(Atari2600.isCollisionMissile1Player0())
      {
        ship0_hit = 31;
        shot0_y = 100;
        shot1_y = 100;
        Atari2600.setAudioControl1((byte)0b1000);
        Atari2600.setAudioFrequency1((byte)22);
        Atari2600.setAudioVolume0((byte)0);
        Atari2600.setAudioVolume1((byte)15);
        score1++;
        if(score1 > 20)
          score1 = 20;
        wait = 30;
      }

      if(Atari2600.isCollisionMissile0Player1())
      {
        ship1_hit = 31;
        shot0_y = 100;
        shot1_y = 100;
        Atari2600.setAudioControl0((byte)0b1000);
        Atari2600.setAudioFrequency0((byte)16);
        Atari2600.setAudioVolume0((byte)15);
        Atari2600.setAudioVolume1((byte)0);
        score0++;
        if(score0 > 20)
          score0 = 20;
        wait = 30;
      }

      if(pf_hit > 6)
      {
        Atari2600.setColorPlayfield(0x00 + pf_hit);
        pf_hit--;
      }

      if((frame & 1) == 1 && shot0_y < 100)
      {
        Atari2600.setAudioFrequency0((byte)((shot0_y >> 1) + 8));
        shot0_y--;
        if(shot0_y < ship1_y)
        {
          shot0_y = 100;
          Atari2600.setAudioVolume0((byte)0);
        }
      }

      rnd -= 77;
      rnd &= 127;
      if(((rnd ^ ship0_x) & 127) < ship1_y && shot1_y == 100)
      {
        shot1_y = ship1_y;
        Atari2600.setAudioControl1((byte)0b0001);
        Atari2600.setAudioVolume1((byte)15);
      }

      if((frame & 1) == 1 && shot1_y < 100)
      {
        Atari2600.setAudioFrequency1((byte)(shot1_y >> 1));
        shot1_y++;
        if(shot1_y > ship0_y + 4)
        {
          shot1_y = 100;
          Atari2600.setAudioVolume1((byte)0);
        }
      }

      frame++;

      Atari2600.waitOverscan();
    }
  }
}

/*
public class Atari2600JavaDemo
{
  public static int bass_waveform[] = 
  {
    8, 6, 6, 6, 8, 6, 6, 6, 8, 6, 6, 6, 8, 6, 8, 6,
    8, 6, 6, 6, 8, 6, 6, 6, 8, 6, 6, 6, 8, 6, 8, 6,
    8, 6, 6, 6, 8, 6, 6, 6, 8, 6, 6, 6, 8, 6, 8, 6,
    8, 6, 6, 6, 8, 6, 6, 6, 8, 6, 6, 6, 8, 6, 8, 6,
  };

  public static int bass_line[] = 
  {
    29, -1, 19, -1, 14, -1, 19, -1, 29, -1, 19, -1, 14, -1, 19, -1,
    23, -1, 11, 11, 14, -1, 11, -1, 23, -1, 11, 11, 14, -1, 11, -1,
    29, -1, 19, -1, 14, -1, 19, -1, 29, -1, 19, -1, 14, -1, 19, -1,
    23, -1, 11, 11, 14, -1, 11, -1, 23, -1, 11, 11, 14, -1, 11, -1,
  };

  public static int lead_line[] = 
  {
    28, 25, 28, -1, 28, 25, 28, -1, 28, 28, -1, -1, 28, 28, -1, -1,
    22, 22, 25, -1, 22, 25, -1, -1, 22, 22, 25, -1, 28, 25, 28, 28,
    28, -1, 25, -1, 25, -1, 28, -1, 28, -1, 25, -1, 25, 28, 28, -1,
    22, 22, 25, -1, 22, 25, -1, -1, 22, 22, 25, -1, 28, 25, 28, 28,
  };

  public static void main()
  {
    Atari2600.setColorPlayfield(0x80 | 0x70 | 0x02);
    Atari2600.setPlayfieldMode(Atari2600.PLAYFIELD_REFLECT);

    int n = 100;
    int timer = 0;
    int note = 0;
    int temp0, temp1;

    Atari2600.setAudioControl0((byte)6);
    Atari2600.setAudioControl1((byte)4);
    
    Atari2600.setAudioVolume0((byte)0);
    Atari2600.setAudioVolume1((byte)0);

    Atari2600.setAudioFrequency0((byte)0);
    Atari2600.setAudioFrequency1((byte)0);

    while(true)
    {
      Atari2600.startVblank();

      timer++;
      if(timer > 8)
      {
        timer = 0;
        temp0 = bass_line[note];

        if(temp0 < 0)
          Atari2600.setAudioVolume0((byte)0);
        else
          Atari2600.setAudioVolume0((byte)15);

        temp1 = lead_line[note];

        if(temp1 < 0)
          Atari2600.setAudioVolume1((byte)0);
        else
          Atari2600.setAudioVolume1((byte)15);

        Atari2600.setAudioControl0((byte)bass_waveform[note]);
        Atari2600.setAudioFrequency0((byte)temp0);
        Atari2600.setAudioFrequency1((byte)temp1);

        note++;
        if(note >= 64)
          note = 0;
      }

      Atari2600.waitVblank();

      // Draw on screen
      //Atari2600.setColorBackground(0x80 | 0x70 | 0x04);
      Atari2600.waitHsync(10);
      Atari2600.setColorBackground(0x80 | 0x70 | 0x02);
      Atari2600.waitHsync(10);
      Atari2600.setColorBackground(0x80 | 0x70 | 0x01);
      Atari2600.waitHsync(10);

      Atari2600.setPlayfield2((byte)0x98);
      Atari2600.setColorPlayfield(n);
      Atari2600.waitHsync(70);

      Atari2600.setPlayfield2((byte)0x8c);
      Atari2600.setColorPlayfield(n);
      Atari2600.waitHsync(25);

      Atari2600.setPlayfield2((byte)0x86);
      Atari2600.setColorPlayfield(n);
      Atari2600.waitHsync(20);

      Atari2600.setPlayfield2((byte)0x83);
      Atari2600.setColorPlayfield(n);
      Atari2600.waitHsync(15);

      Atari2600.setPlayfield2((byte)0x81);
      Atari2600.setPlayfield1((byte)0x01);
      Atari2600.setColorPlayfield(n);
      Atari2600.waitHsync(10);

      Atari2600.setPlayfield2((byte)0x00);
      Atari2600.setPlayfield1((byte)0x00);
      Atari2600.setColorPlayfield(n);
      Atari2600.waitHsync(22);

      Atari2600.startOverscan();

      n++;

      if (Atari2600.isJoystick0Left())
      {
        Atari2600.setColorBackground(0x80 | 0x70 | 0x01);
      }
        else
      if (Atari2600.isJoystick0Right())
      {
        Atari2600.setColorBackground(0x80 | 0x70 | 0x02);
      }
        else
      if (Atari2600.isJoystick0Down())
      {
        Atari2600.setColorBackground(0x80 | 0x74 | 0x02);
      }
        else
      if (Atari2600.isJoystick0Up())
      {
        Atari2600.setColorBackground(0x80 | 0x64 | 0x02);
      }
        else
      if (Atari2600.isJoystick0ButtonDown())
      {
        Atari2600.setColorBackground(0x80 | 0x64 | 0x08);
      }
        else
      {
        Atari2600.setColorBackground(0x80 | 0x70 | 0x04);
      }

      Atari2600.waitOverscan();
    }
  }
}
*/
