import gui.Emu;
import io.Loader;
import memory.Memory;
import sh4.Sh4Context;
import utils.Logger;

import java.nio.file.Paths;

/**
 * Federico Berti
 * <p>
 * Copyright 2021
 */
public class Launcher {

    //ip.bin initial 32K of the first track, gets copied to 8C008000-8C00FFFF
    static final int IP_BIN_START_ADDRESS = 0x80008000;
    static final int PRG_START_ADDRESS = 0x80010000; //??
    static final long sh4_clock_speed = 200_000_000; //200Mhz
    static final long clocksPerFrame = sh4_clock_speed/60; //assuming 60fps
    static final int burstCycles = Sh4Context.burstCycles;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        System.setProperty("org.lwjgl.librarypath", Paths.get(".", "lib").toAbsolutePath().toString());
        Memory.setUpMemoryZones();
        Loader.loadBinaryFile("res/dc_boot.bin", false, Memory.bios, 0);
        Loader.loadBinaryFile("res/dc_flash.bin", false, Memory.flash, 0);

//		Loader.loadBinaryFile("res/ip.bin",false,Memory.mem, Memory.getMemoryAddress(IP_BIN_START_ADDRESS));
        Loader.loadBinaryFile("res/gg_3dEngine.bin", false, Memory.mem, Memory.getMemoryAddress(PRG_START_ADDRESS));
        run();
    }

    static void run(){
        Sh4Context.debugging = false;
        Logger.enabled = false;

        long secondsElapsed = 0;
        Emu emu = new Emu();
        long cycleCnt = 0;
        long frameCnt = 0;
        while(true) {
            emu.run();
            cycleCnt += burstCycles;
            if(cycleCnt > clocksPerFrame){
                sleep(1);
                cycleCnt -= clocksPerFrame;
                frameCnt++;
                if(frameCnt % 50 == 0) {
                    System.out.println("*** Frame: " + frameCnt);
                }
            }
        }
    }

    static void sleep(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
