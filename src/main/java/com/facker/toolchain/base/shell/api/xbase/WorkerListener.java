package com.facker.toolchain.base.shell.api.xbase;

import java.io.File;
import java.util.List;

public interface WorkerListener {
    public static class WORKER {
        public static final String WORKING_DECODE_BRIDGE = "working_decode_bridge";
        public static final String WORKING_DECODE_TARGET = "working_decode_target";
        public static final String WORKING_DECODE_MODEL = "working_decode_model";
        public static final String WORKING_FLIP = "working_flip";
        public static final String WORKING_MERGE_BRIDGE_LIB = "working_merge_bridge_lib";
        public static final String WORKING_MERGE_MODELS_LIB = "working_merge_MODELS_lib";
        public static final String WORKING_MERGE_BRIDGE_ASSETS = "working_merge_bridge_assets";
        public static final String WORKING_MERGE_MODELS_ASSETS = "working_merge_models_assets";
        public static final String WORKING_MERGE_BRIDGE_MANIFEST = "working_merge_bridge_manifest";
        public static final String WORKING_MERGE_MODELS_MANIFEST = "working_merge_models_manifest";
        public static final String WORKING_MERGE_BRIDGE_RES = "working_merge_bridge_res";
        public static final String WORKING_MERGE_MODELS_RES = "working_merge_models_res";
        public static final String WORKING_MOD_TARGET_LOGO = "working_mod_target_logo";
        public static final String WORKING_HANDLE_MODELS_DEX = "working_handle_models_dex";
        public static final String WORKING_HANDLE_TARGET_DEX = "working_handle_target_dex";
        public static final String WORKING_COPY_BRIDGE_DEX= "working_copy_bridge_dex";
        public static final String WORKING_MODEL_TARGET_YML= "working_mod_target_yml";





        public static final String WORKING_CHECK_TARGET = "working_check_target";
        public static final String WORKING_PROCESS_DEX = "working_process_dex";
        public static final String WORKING_ORLDER_TARGET = "working_process_dex";
        public static final String WORKING_INJECTWEBCONFIG = "working_injectwebconfig";
        public static final String WORKING_PROCESS_COREMANIFEST = "working_process_coremanifest";

        public static final String WORKING_PROCESS_MODELMANIFEST= "working_process_modelmanifest";
        public static final String WORKING_MEREGE_MODEL= "working_merege_model";
        public static final String WORKING_PROCES_RUNTIME= "working_proces_runtime";
        public static final String WORKING_ZIP_XTARGET= "working_zip_xtarget";
        public static final String WORKING_SIGN_XTARGET= "working_sign_xtarget";

        public static final String WORKING_COMPUTE_XTARGET= "working_compute_xtarget";
    }
    public static class ERRO {
        public static final String ERRO_PREFIX ="erro_";
    }

    void xflip(long id, List<Worker.XFlip> flips);
    void working(long id, String working);

    void erro(long id, String erro);

    void sucess(long id, File file, String md5);
}
