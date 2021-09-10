package ru.phoenix.game.tools;

import java.util.ArrayList;
import java.util.List;

import static ru.phoenix.engine.core.constants.TextureInfo.*;

public class GeneratorID {
    private static GeneratorID instance = null;

    private List<Float> groupR;
    private List<Float> groupG;
    private List<Float> groupB;
    private List<Float> groupA;

    public GeneratorID(){
        groupR = new ArrayList<>();
        groupG = new ArrayList<>();
        groupB = new ArrayList<>();
        groupA = new ArrayList<>();
    }

    public float generateID(int group){
        float id = 0.0f;
        float offset = 0.001f;
        if(group == GROUP_R){
            if(groupR.size() == 0){
                id = offset;
            }else{
                id = groupR.get(groupR.size() - 1) + offset;
            }
            groupR.add(id);
        }else if(group == GROUP_G){
            if(groupG.size() == 0){
                id = offset;
            }else{
                id = groupG.get(groupG.size() - 1) + offset;
            }
            groupG.add(id);
        }else if(group == GROUP_B){
            if(groupB.size() == 0){
                id = offset;
            }else{
                id = groupB.get(groupB.size() - 1) + offset;
            }
            groupB.add(id);
        }else if(group == GROUP_A){
            if(groupA.size() == 0){
                id = offset;
            }else{
                id = groupA.get(groupA.size() - 1) + offset;
            }
            groupA.add(id);
        }
        return id;
    }

    public static GeneratorID getInstance(){
        if(instance == null){
            instance = new GeneratorID();
        }
        return instance;
    }
}
