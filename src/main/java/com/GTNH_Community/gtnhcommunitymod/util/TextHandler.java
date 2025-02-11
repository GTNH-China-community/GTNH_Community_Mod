package com.GTNH_Community.gtnhcommunitymod.util;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.GTNH_Community.gtnhcommunitymod.GTNHCommunityMod;

/**
 * When Texts need auto generate .lang . Use this.
 */
public class TextHandler {

    /* The Map across all text<Key, Value> */
    public static Map<String, String> LangMap;
    // public static Map<String, String> LangMapBackUp;
    public static Map<String, String> LangMapNeedToWrite = new HashMap<>();

    /**
     * When the String inputs need generate a .lang Key-Value at once,
     * use this method instead directly String.
     *
     * @param aTextLine The String should input as default, input 1 line.
     * @param aKey      The key of this Line. If no key input, will auto generate.
     * @return Return the String as no this method until the text has been changed in file .lang.
     */
    public static String texter(String aTextLine, String aKey) {

        /**
         * If not in Dev mode , return vanilla forge method directly.
         */
        if (GTNHCommunityMod.isInDevMode) {
            if (LangMap.get(aKey) == null) {
                GTNHCommunityMod.LOG.info("Texter get a new key - TextLine: " + aKey + " - " + aTextLine);
                LangMapNeedToWrite.put(aKey, aTextLine);
                return aTextLine;
            } else {
                return translateToLocalFormatted(aKey);
            }
        } else if (null != translateToLocalFormatted(aKey)) {
            return translateToLocalFormatted(aKey);
        }
        return "texterError: " + aTextLine;
    }

    public static String texterButKey(String aTextLine, String aKey) {

        if (GTNHCommunityMod.isInDevMode) {
            if (LangMap.get(aKey) == null) {
                GTNHCommunityMod.LOG.info("Texter get a new key - TextLine: " + aKey + " - " + aTextLine);
                LangMapNeedToWrite.put(aKey, aTextLine);
            }
        }
        return aKey;
    }

    /**
     * Auto generate the Key of textLine
     * {@link TextHandler#texter(String aTextLine, String aKey)}
     *
     * @param aTextLine The default String to where you use.
     * @return
     */
    public static String texter(String aTextLine) {
        String aKey = "Auto." + aTextLine + ".text";
        return texter(aTextLine, aKey);
    }

    /**
     * Init LangMap.
     *
     * @param isInDevMode The signal of whether in development mode.
     */
    public static void initLangMap(Boolean isInDevMode) {
        if (isInDevMode) {
            /* Parse the .lang in LangMap */
            LangMap = LanguageUtil0.parseLangFile("en_US");
            // LangMapBackUp = new HashMap<String, String>(LangMap);
        }

    }

    /**
     * Write the new textLines into Dev/src/main/resources/gtnhcommunitymod/lang/.lang
     *
     * @param isInDevMode The signal of whether in development mode.
     */
    public static void serializeLangMap(boolean isInDevMode) {

        if (isInDevMode) {

            /* If no new text need to write */
            if (LangMapNeedToWrite.isEmpty()) {
                return;
            }
            // if (LangMap.equals(LangMapBackUp)) {
            // GTNHCommunityMod.LOG.info(GTNHCommunityMod.MODID + ": No new text need to handle.");
            // /* If you need to see what the fuck in the LangMap and LangMapBackUp, remove the comment markers. */
            //
            // // for(String key : LangMapBackUp.keySet()){
            // // gtnhcommunitymod.LOG.info("Get LanMapBackUp at serializeLangMap() : " + key + " --- " +
            // // LangMapBackUp.get(key));
            // // }
            // // for (String key : LangMap.keySet()){
            // // gtnhcommunitymod.LOG.info("Get LanMap at serializeLangMap() : " + key + " --- " + LangMap.get(key));
            // // }
            //
            // return;
            // }

            // /* New a Map with new texts need to write. */
            // Map<String, String> LangMapNeedWrite = new HashMap<String, String>(LangMap);
            //
            // /* Remove texts not need to write. */
            // for (String tx : LangMapBackUp.keySet()) {
            // LangMapNeedWrite.remove(tx);
            // }

            /* Prepare the files. */
            File en_US_lang = new File(GTNHCommunityMod.DevResource + "\\assets\\gtnhcommunitymod\\lang\\en_US.lang");
            File zh_CN_lang = new File(GTNHCommunityMod.DevResource + "\\assets\\gtnhcommunitymod\\lang\\zh_CN.lang");
            GTNHCommunityMod.LOG
                .info("File finder with en_US.lang catch a file absolutePath: " + en_US_lang.getAbsolutePath());
            GTNHCommunityMod.LOG.info("File finder with en_US.lang catch a file named: " + en_US_lang.getName());

            /* Write the new textLines in the end of the lang file. */
            GTNHCommunityMod.LOG.info("Start write new text: " + en_US_lang.getAbsolutePath());

            try {
                FileWriter en_Us = new FileWriter(en_US_lang, true);
                FileWriter zh_CN = new FileWriter(zh_CN_lang, true);
                for (String key : LangMapNeedToWrite.keySet()) {
                    GTNHCommunityMod.LOG
                        .info("en_US write a Line START: " + key + "===>" + LangMapNeedToWrite.get(key));
                    en_Us.write(key);
                    en_Us.write("=");
                    en_Us.write(LangMapNeedToWrite.get(key));
                    en_Us.write("\n");
                    GTNHCommunityMod.LOG.info("en_US write a Line COMPLETE.");
                    GTNHCommunityMod.LOG
                        .info("zh_CN write a Line START: " + key + "===>" + LangMapNeedToWrite.get(key));
                    zh_CN.write(key);
                    zh_CN.write("=");
                    zh_CN.write(LangMapNeedToWrite.get(key));
                    zh_CN.write("\n");
                    GTNHCommunityMod.LOG.info("zh_CN write a Line COMPLETE.");
                }
                GTNHCommunityMod.LOG.info("Finish to write new text: " + en_US_lang.getAbsolutePath());
                en_Us.close();
                zh_CN.close();
            } catch (IOException e) {
                GTNHCommunityMod.LOG.info("Error in serializeLangMap() File Writer en_US");
                throw new RuntimeException(e);
            }

            /* Del the backup. */
            LangMapNeedToWrite.clear();

        }
    }
}
