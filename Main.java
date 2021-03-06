//package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int[] mainMem = initMainMem();
        CacheSlot[] cache = initCache();

        prompt(mainMem, cache);

    }

    public static int[] initMainMem() {
        int[] Main_mem = new int[2048];

        int i = 0;
        int j = 0;

        while (i < 8) {
            for (int k = 0; k < 256; k++) {
                Main_mem[k + j] = k;
            }

            j += 256;
            i++;
        }

        return Main_mem;
    }

    public static CacheSlot[] initCache() {
        CacheSlot[] cache = new CacheSlot[16];

        for (int i = 0; i < 16; i++) {
            cache[i] = new CacheSlot();
        }

        return cache;
    }

    public static void prompt(int[] mainMem, CacheSlot[] cache) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("******************************************************************************************");
        System.out.println("(R)ead, (W)rite, or (D)isplay Cache?");
        char operation = keyboard.next().toUpperCase().charAt(0);

        while (operation != 'E') {
            switch (operation) {
                case 'R':
                    read(mainMem, cache);
                    break;
                case 'D':
                    display(cache);
                    break;
                case 'W':
                    write(mainMem, cache);
                    break;
                case 'E':
                    System.exit(0);
                    break;
                default:
                    System.out.println(operation + " is not supported!");
                    break;
            }
            System.out.println("\n*******************************************************************************************");
            System.out.println("(R)ead, (W)rite, (D)isplay Cache or (E)xit?");
            operation = keyboard.next().toUpperCase().charAt(0);
        }


    }

    public static void read(int[] mainMem, CacheSlot[] cache) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("What address would you like to read?");
        String input = keyboard.nextLine();

        int inputInt = Integer.parseInt(input, 16);
        int tag = (inputInt & 0xF00) >>> 8;
        int slotNumber = (inputInt & 0x0F0) >>> 4;
        int offSet = inputInt & 0x00F;
        int blockStart = (inputInt & 0xFF0);
        int[] memBlock = new int[16];

        if (cache[slotNumber].getValidFlag() == 1 && cache[slotNumber].getTag() == tag) {// cache hit
            System.out.println("At that byte there is the value: 0x" + Integer.toHexString(cache[slotNumber].getMemBlock()[offSet]) + " (Cache Hit!)");
        } else { //cache miss

            if (cache[slotNumber].getDirtyBit() == 1) {// write back
                for (int i = 0; i < 16; i++) {
                    mainMem[(cache[slotNumber].getTag() << 8) + (slotNumber << 4) + i] = cache[slotNumber].getMemBlock()[i];
                }
            }//end if

            cache[slotNumber].setValidFlag(1);
            cache[slotNumber].setTag(tag);
            cache[slotNumber].setDirtyBit(0);

            for (int i = 0; i < 16; i++) {
                memBlock[i] = mainMem[blockStart + i];
            }

            cache[slotNumber].setMemBlock(memBlock);

            System.out.println("At that byte there is the value: 0x" + Integer.toHexString(memBlock[offSet]) + " (Cache Miss!!)");


        } // end else


    }

    public static void write(int[] mainMem, CacheSlot[] cache) {
        Scanner keyboard = new Scanner(System.in);

        System.out.println("What address would you like to write to?");
        String input = keyboard.nextLine();
        System.out.println("What data would you like to write at that address?");
        String inputValue = keyboard.nextLine();


        int valueInt = Integer.parseInt(inputValue, 16);
        int inputInt = Integer.parseInt(input, 16);
        int tag = (inputInt & 0xF00) >>> 8;
        int slotNumber = (inputInt & 0x0F0) >>> 4;
        int offSet = inputInt & 0x00F;
        int blockStart = (inputInt & 0xFF0);
        int[] memBlock = new int[16];


        if (cache[slotNumber].getValidFlag() == 1 && cache[slotNumber].getTag() == tag) {
            // cache hit
            cache[slotNumber].getMemBlock()[offSet] = valueInt;
            cache[slotNumber].setDirtyBit(1);

            System.out.println("The Value 0x" + inputValue + " has been written to address 0x" + input + ". (Cache Hit!)");


        } else {//cache miss
            if (cache[slotNumber].getValidFlag() == 1 && cache[slotNumber].getTag() != tag && cache[slotNumber].getDirtyBit() == 1) {
                // cache miss and dirty slot => update memory first
                for (int i = 0; i < 16; i++) {
                    mainMem[(cache[slotNumber].getTag() << 8) + (slotNumber << 4) + i] = cache[slotNumber].getMemBlock()[i];
                }
            }

            cache[slotNumber].setDirtyBit(1);
            cache[slotNumber].setValidFlag(1);
            cache[slotNumber].setTag(tag);

            for (int i = 0; i < 16; i++) {

                memBlock[i] = mainMem[blockStart + i];
            }

            memBlock[offSet] = valueInt;

            cache[slotNumber].setMemBlock(memBlock);

            System.out.println("The Value 0x" + inputValue + " has been written to address 0x" + input + ". (Cache Miss!)");


        }

    }

    public static void display(CacheSlot[] cache) {
        System.out.printf("Slot Dirty Valid Tag ");
        System.out.printf("%10s", "Data");

        for (int i = 0; i < 16; i++) {
            System.out.println();
            System.out.printf("%2s", Integer.toHexString(i).toUpperCase());
            System.out.printf("%6s", Integer.toHexString(cache[i].getDirtyBit()).toUpperCase());
            System.out.printf("%6s", Integer.toHexString(cache[i].getValidFlag()).toUpperCase());
            System.out.printf("%5s", Integer.toHexString(cache[i].getTag()).toUpperCase());
            System.out.printf("%5s", "");
            for (int j = 0; j < 16; j++) {
                System.out.printf("%4s", Integer.toHexString(cache[i].getMemBlock()[j]).toUpperCase());

            }
        }
    }

    }
