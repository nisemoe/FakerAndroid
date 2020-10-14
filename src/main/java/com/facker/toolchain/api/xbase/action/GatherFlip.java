package com.facker.toolchain.api.xbase.action;

import com.facker.toolchain.api.xbase.*;

import java.util.ArrayList;
import java.util.List;

public class GatherFlip extends WorkerAction {
    private XTarget xTarget;
    private WorkerListener workerListener;
    public GatherFlip(XTarget xTarget, WorkerListener workerListener) {
        this.xTarget = xTarget;
        this.workerListener =workerListener;
    }
    @Override
    public void action() throws Exception {
        ManifestEditor manifestMod = new ManifestEditor(xTarget.getManifestFile());
        String lancherActivityName = manifestMod.getLancherActivityName();
        List<Worker.XFlip> flips = new ArrayList<>();
        List<String> activityNames = manifestMod.getActivityNames();
        for (String string :activityNames){
            Worker.XFlip flip = new Worker.XFlip();
            if(lancherActivityName.equals(string)){
                flip.setType("Activity_LAUNCHER");
            }else{
                flip.setType("Activity_COMMON");
            }
            flip.setTag(string);
            flips.add(flip);
        }
        workerListener.xflip(xTarget.getId(),flips);
    }
}
