package daJad;

import simple.hooks.filters.SimplePrayers;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.api.ClientContext;
import simple.robot.script.Script;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

@ScriptManifest(author = "alex", category = Category.COMBAT, description = "<br>1<br><br>2<br><br>3<br>~ AIO4 ~<br>5<br>6<br>7<br><br>8", discord = "", name = "daJad", servers = {
        "Zaros, Battlescape" }, version = "0.1")

public class daJadMain extends Script {

    @Override
    public void onExecute() {
    }

    @Override
    public void onProcess() {
        SimpleNpc JAD = ctx.npcs.populate().filter(3127).nextNearest();
        int RANGE_ANIM = 2652;
        int MAGE_ANIM = 2656;
        if (JAD != null) {
            if (JAD.getAnimation() == RANGE_ANIM) {
                if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MISSILES)) {
                    ctx.prayers.prayer(SimplePrayers.Prayers.PROTECT_FROM_MISSILES, true);
                    ctx.log("prayin range");
                }//pray range
            } else if (JAD.getAnimation() == MAGE_ANIM) {
                if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MAGIC)) {
                    ctx.prayers.prayer(SimplePrayers.Prayers.PROTECT_FROM_MAGIC, true);
                    ctx.log("prayin mage");
                }//pray mage
            }
        }
    }

    @Override
    public void onChatMessage(ChatMessage msg) {
    }

    @Override
    public void onTerminate() {
    }

    @Override
    public void paint(Graphics Graphs) {

    }
}
