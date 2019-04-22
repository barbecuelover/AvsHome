package com.zw.avshome.utils;

public class VolumeUtil {

    /**
     * Music 15 Segments ,avs[0,100]
     * @param avsVolume
     * @return
     */
    public static int avsToFifteenSegments(byte avsVolume){
        int volume = (avsVolume * 3/2)/10;
        return volume;
    }

    public static byte fifteenSegmentsToAvs(int sysVolume){
        byte avsVolume = 50;
        switch (sysVolume){
            case 0:
                avsVolume = 0;
                break;
            case 1:
            case 2:
                avsVolume = 10;
                break;
            case 3:
                avsVolume = 20;
                break;
            case 4:
            case 5:
                avsVolume = 30;
                break;
            case 6:
                avsVolume = 40;
                break;
            case 7:
            case 8:
                avsVolume = 50;
                break;
            case 9:
                avsVolume = 60;
                break;
            case 10:
            case 11:
                avsVolume = 70;
                break;
            case 12:
                avsVolume = 80;
                break;
            case 13:
            case 14:
                avsVolume = 90;
                break;
            case 15:
                avsVolume = 100;
                break;

        }
        return avsVolume;
    }


    /**
     * Notification & Alert  8 Segments ,avs[0,100]
     * @param avsVolume
     * @return
     */
    public static int avsToEightSegments(byte avsVolume){
        int volume = avsVolume/10;
        if (volume >= 8){
            volume = 7;
        }
        return volume;
    }

    public static byte eightSegmentsToAvs(int sysVolume){
        return (byte)(sysVolume * 10);
    }

}
