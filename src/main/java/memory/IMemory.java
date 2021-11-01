package memory;

import java.nio.IntBuffer;

/**
 * Federico Berti
 * <p>
 * Copyright 2021
 */
public interface IMemory {
    int read32i(int i);

    void write16i(int register, int register1);

    void write32i(int register, int register1);

    int read8i(int register);

    int read16i(int register);

    void write8i(int register, byte register1);

    void sqWriteTomemoryInst(int addr, int i);

    void regmapWritehandle32Inst(int tra, int i);

    void read64i(int register, float[] fRm, int i);

    void write64i(int i, float[] fRm, int i1);

    int regmapReadhandle32i(int qacr0);

    IntBuffer getSQ0();

    IntBuffer getSQ1();
}
