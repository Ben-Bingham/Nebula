package ca.benbingham.game.planetstructure;

import java.util.ArrayList;

import static ca.benbingham.engine.util.ArrayUtil.floatListToArray;
import static ca.benbingham.game.planetstructure.Chunk.*;

public class ChunkDebugLineData {
    public final float[] primaryChunkLines = {
            0    , 0    , 0    ,
            0    , ySize, 0    ,
    };

    public float[] secondaryChunkLines;

    public ChunkDebugLineData() {
        ArrayList<Float> totalFloatList = new ArrayList<>();

        for (int i = 0; i < xSize; i++) {
            totalFloatList.add((float) i);
            totalFloatList.add((float) 0);
            totalFloatList.add((float) 0);

            totalFloatList.add((float) i);
            totalFloatList.add((float) ySize);
            totalFloatList.add((float) 0);
        }

        for (int i = 0; i < zSize; i++) {
            totalFloatList.add((float) 0);
            totalFloatList.add((float) 0);
            totalFloatList.add((float) i);

            totalFloatList.add((float) 0);
            totalFloatList.add((float) ySize);
            totalFloatList.add((float) i);
        }

        for (int i = 0; i < ySize; i++) {
            totalFloatList.add((float) 0);
            totalFloatList.add((float) i);
            totalFloatList.add((float) 0);

            totalFloatList.add((float) xSize);
            totalFloatList.add((float) i);
            totalFloatList.add((float) 0);

            totalFloatList.add((float) 0);
            totalFloatList.add((float) i);
            totalFloatList.add((float) 0);

            totalFloatList.add((float) 0);
            totalFloatList.add((float) i);
            totalFloatList.add((float) zSize);
        }

        secondaryChunkLines = floatListToArray(totalFloatList);
    }
}
