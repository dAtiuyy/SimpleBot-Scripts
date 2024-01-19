package daDemonix;

import net.runelite.api.HeadIcon;
import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimplePrayers;
import simple.hooks.filters.SimpleSkills;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.LoopingScript;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.script.Script;

import java.awt.*;

@ScriptManifest(author = "alex", category = Category.COMBAT, description = "<br>1<br><br>2<br><br>3<br>~ AIO4 ~<br>5<br>6<br>7<br><br>8", discord = "", name = "daDemonix", servers = {
        "Zaros" }, version = "0.1")

public class daDemonixMain extends Script implements LoopingScript {
    @Override
    public void onExecute() {
    }

    @Override
    public void onProcess() {

        SimplePrayers prayers = new SimplePrayers(ctx);
        SimpleNpc DEMONIC = ctx.npcs.populate().filter("Demonic gorilla").nextNearest();

        int[] FoodIds = {391, 385, 379, 7946, 13441, 373, 3144, 11936};
        int[] PPotsIds = {143, 3030, 141, 3028, 139, 3026, 2434, 3024};
        int[] meleeGearIDs = {13239, 19675, 20366, 11665};
        int[] rangeGearIDs = {13237, 9185, 22249, 11664};
        SimpleItem food = ctx.inventory.populate().filter(FoodIds).next();
        SimpleItem ppots = ctx.inventory.populate().filter(PPotsIds).next();

        int RANGE_ANIM = 7227;
        int MELEE_ANIM = 7226;
        int MAGIC_ANIM = 7238;
        int BOULDER_ANIM = 7228;

        if (ctx.players.getLocal().getHealth() <= 30 && food != null) {
            food.click(0);
        }

        if (prayers.points() <= 30 && ppots != null) {
            ppots.click(0);
        }

        if (DEMONIC != null) {
            if (ctx.getClient().getLocalPlayer().getInteracting() != null && DEMONIC.getInteracting().equals(ctx.getClient().getLocalPlayer())) {
                if (ctx.getClient().getLocalPlayer().getInteracting().getAnimation() == BOULDER_ANIM) {
                    WorldPoint playerLocation = ctx.players.getLocal().getLocation();
                    int xOffset = 0;
                    int yOffset = 1;

                    int newX = playerLocation.getX() + xOffset;
                    int newY = playerLocation.getY() + yOffset;

                    ctx.pathing.step(newX, newY);
                    if (!ctx.pathing.reachable(new WorldPoint(newX, newY, playerLocation.getPlane()))){
                        ctx.pathing.step(newX, playerLocation.getY());
                    } else {
                        ctx.pathing.step(playerLocation.getX(), newY);
                    }
                    ctx.log("moved");
                }
               if (DEMONIC.getOverhead().equals(HeadIcon.MELEE)) {
                   //it protects melee, attack range
                   for (int gearID : rangeGearIDs) {
                       SimpleItem rangeGear = ctx.inventory.populate().filter(gearID).next();
                       if (rangeGear != null) {
                           rangeGear.click(0);
                           ctx.sleep(50, 150); // Optional: Add a delay after clicking the item
                       }
                   }
               } else if (DEMONIC.getOverhead().equals(HeadIcon.RANGED)) {
                   //it protects range, attack melee
                   for (int gearID2 : meleeGearIDs) {
                       SimpleItem meleeGear = ctx.inventory.populate().filter(gearID2).next();
                       if (meleeGear != null) {
                           meleeGear.click(0);
                           ctx.sleep(50, 150);
                       }
                   }
               }

               if (DEMONIC.getAnimation() == RANGE_ANIM) {
                   if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MISSILES)) {
                       ctx.prayers.prayer(SimplePrayers.Prayers.PROTECT_FROM_MISSILES, true);
                       ctx.log("prayin range");
                   }//pray range
               } else if (DEMONIC.getAnimation() == MELEE_ANIM) {
                   if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MELEE)) {
                       ctx.prayers.prayer(SimplePrayers.Prayers.PROTECT_FROM_MELEE, true);
                       ctx.log("prayin melee");
                   }//pray melee
               } else if (DEMONIC.getAnimation() == MAGIC_ANIM) {
                   if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MAGIC)) {
                       ctx.prayers.prayer(SimplePrayers.Prayers.PROTECT_FROM_MAGIC, true);
                       ctx.log("prayin mage");
                   }//pray mage
               }
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

    @Override
    public int loopDuration() {
        return 150;
    }
}