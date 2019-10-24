//package com.company;

public class CacheSlot {
    private int [] memBlock;
    private int tag;
    private int validFlag;
    private int dirtyBit;

    public int getDirtyBit() {
        return dirtyBit;
    }

    public void setDirtyBit(int dirtyBit) {
        this.dirtyBit = dirtyBit;
    }

    public CacheSlot() {
        this.memBlock = new int[16];
        this.tag = 0;
        this.validFlag = 0;
        this.dirtyBit = 0;
    }

    public int[] getMemBlock() {
        return memBlock;
    }

    public void setMemBlock(int[] memBlock) {
        this.memBlock = memBlock;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(int validFlag) {
        this.validFlag = validFlag;
    }

}
