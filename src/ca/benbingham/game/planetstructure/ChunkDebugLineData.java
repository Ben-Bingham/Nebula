package ca.benbingham.game.planetstructure;

import org.joml.Vector2i;

import java.util.ArrayList;

import static ca.benbingham.engine.util.ArrayUtil.floatListToArray;
import static ca.benbingham.game.planetstructure.Chunk.*;

public class ChunkDebugLineData {
    public final float[] primaryChunkLines = {
            0    , 0    , 0,
            0    , ySize, 0,
    };

    public float[] secondaryChunkLines;

    public ChunkDebugLineData() {
        ArrayList<Float> totalFloatList = new ArrayList<>();

        // Vertical lines
        for (int i = 0; i < xSize; i++) {
            totalFloatList.add((float) i);
            totalFloatList.add((float) 0);
            totalFloatList.add((float) 0);

            totalFloatList.add((float) i);
            totalFloatList.add((float) ySize);
            totalFloatList.add((float) 0);

            totalFloatList.add((float) i);
            totalFloatList.add((float) 0);
            totalFloatList.add((float) zSize);

            totalFloatList.add((float) i);
            totalFloatList.add((float) ySize);
            totalFloatList.add((float) zSize);
        }

        for (int i = 0; i < zSize; i++) {
            totalFloatList.add((float) 0);
            totalFloatList.add((float) 0);
            totalFloatList.add((float) i);

            totalFloatList.add((float) 0);
            totalFloatList.add((float) ySize);
            totalFloatList.add((float) i);

            totalFloatList.add((float) xSize);
            totalFloatList.add((float) 0);
            totalFloatList.add((float) i);

            totalFloatList.add((float) xSize);
            totalFloatList.add((float) ySize);
            totalFloatList.add((float) i);
        }

        totalFloatList.add((float) xSize);
        totalFloatList.add((float) 0);
        totalFloatList.add((float) zSize);

        totalFloatList.add((float) xSize);
        totalFloatList.add((float) ySize);
        totalFloatList.add((float) zSize);

        // Horizontal lines
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

            totalFloatList.add((float) 0);
            totalFloatList.add((float) i);
            totalFloatList.add((float) zSize);

            totalFloatList.add((float) xSize);
            totalFloatList.add((float) i);
            totalFloatList.add((float) zSize);

            totalFloatList.add((float) xSize);
            totalFloatList.add((float) i);
            totalFloatList.add((float) 0);

            totalFloatList.add((float) xSize);
            totalFloatList.add((float) i);
            totalFloatList.add((float) zSize);
        }

        secondaryChunkLines = floatListToArray(totalFloatList);

        for (int i = 0; i < primaryChunkLines.length; i++) {
            primaryChunkLines[i] -= 0.5f;
        }

        for (int i = 0; i < secondaryChunkLines.length; i++) {
            secondaryChunkLines[i] -= 0.5;
        }
    }

    public float[] createTrinaryChunkLines(int halfCircumference) {
        ArrayList<Float> totalFloatList = new ArrayList<>();

        totalFloatList.add((float) halfCircumference * xSize);
        totalFloatList.add((float) 0);
        totalFloatList.add((float) halfCircumference * zSize);

        totalFloatList.add((float) halfCircumference * xSize);
        totalFloatList.add((float) ySize);
        totalFloatList.add((float) halfCircumference * zSize);

        totalFloatList.add((float) halfCircumference * xSize);
        totalFloatList.add((float) 0);
        totalFloatList.add((float) (halfCircumference * zSize * -1) + zSize);

        totalFloatList.add((float) halfCircumference * xSize);
        totalFloatList.add((float) ySize);
        totalFloatList.add((float) (halfCircumference * zSize * -1) + zSize);

        totalFloatList.add((float) (halfCircumference * xSize * -1) + xSize);
        totalFloatList.add((float) 0);
        totalFloatList.add((float) halfCircumference * zSize);

        totalFloatList.add((float) (halfCircumference * xSize * -1) + xSize);
        totalFloatList.add((float) ySize);
        totalFloatList.add((float) halfCircumference * zSize);

        totalFloatList.add((float) (halfCircumference * xSize * -1) + xSize);
        totalFloatList.add((float) 0);
        totalFloatList.add((float) (halfCircumference * zSize * -1) + zSize);

        totalFloatList.add((float) (halfCircumference * xSize * -1) + xSize);
        totalFloatList.add((float) ySize);
        totalFloatList.add((float) (halfCircumference * zSize * -1) + zSize);

        for (int i = 0; i < totalFloatList.size(); i++) {
            totalFloatList.set(i, (float) (totalFloatList.get(i) - 0.5));
        }

        return floatListToArray(totalFloatList);
    }
}
