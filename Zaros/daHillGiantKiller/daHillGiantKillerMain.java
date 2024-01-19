package daHillGiantKiller;

import simple.hooks.filters.SimplePrayers;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleGroundItem;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.script.Script;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Stream;

@ScriptManifest(
        author = "alex",
        category = Category.COMBAT,
        description = "<html>"
                + "<p></p>"
                + "<p><strong></strong></p>"
                + "<ul>"
                + "<li><strong></strong>.</li>"
                + "<li><strong></strong></li>"
                + "</ul>"
                + "</html>",
        discord = "",
        name = "daHillGiantKiller",
        servers = {"Zaros"},
        version = "4.0"
)
public class daHillGiantKillerMain extends Script {
    final int[] EASY_CLUES = {2702, 2707, 2677, 2699, 2683, 2688, 2678, 2700, 2686, 2682, 2689, 2705, 2684, 2693, 2696, 2706, 2686, 2698};
    final int[] HARD_CLUES = {2786, 2737, 3520, 2792, 2799, 2733, 2776, 2780, 2735, 3538, 2790, 2731, 3524, 2782, 2797, 2741, 2729, 2783, 3532, 3530, 2723, 2739, 3540, 2722, 2796, 3542, 2774, 3525, 2773, 2793, 2775, 3522, 2747};
    final int[] ELITE_CLUES = {12105, 12091, 12086, 12076, 12083, 12073, 12097, 12089, 12079, 12088, 12105};
    final int[] RUNE_ITEMS = {1213, 830, 892, 1432, 811, 824, 44, 3101, 1319, 1163, 1359, 1275, 868, 1373, 1303, 1147, 1333, 19580, 1247, 9185, 4131, 1201, 1113, 1079};
    final int[] DRAGON_ITEMS = {20849};
    final int[] IMPORTANT_ITEMS = {20754, 10976, 22374, 10977, 4151, 2366, 4153, 4101};
    final int[] MISC = {2354};
    final int[] KARUULM_ITEMS = {19677, 19681, 19683, 19679};
    final int[] FoodIds = {391, 385, 379, 7946, 13441, 373, 3144, 11936};
    final int[] PPotsIds = {143, 3030, 141, 3028, 139, 3026, 2434, 3024};
    final int[] ITEM_IDs = Stream.of(EASY_CLUES, HARD_CLUES, ELITE_CLUES, IMPORTANT_ITEMS, RUNE_ITEMS, KARUULM_ITEMS, DRAGON_ITEMS, MISC)
            .flatMapToInt(Arrays::stream)
            .toArray();
    final int[] LE_NPC = {412};
    final int[] SUPPs_NPC = {7402, 7407, 7410, 10399, 1149, 1377, 7158, 11212, 4087, 1432, 1215, 3204, 21930, 11237, 19484, 19582};
    public String status;
    public long startTime;

    @Override
    public void onExecute() {
        System.out.println("Started daSlayerHelper");
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onProcess() {
        SimplePrayers prayers = new SimplePrayers(ctx);
        SimpleGroundItem items = ctx.groundItems.populate().filter(ITEM_IDs).nextNearest();
        SimpleNpc Giant = ctx.npcs.populate().filter(LE_NPC).nextNearest();
        SimpleNpc SUPPs = ctx.npcs.populate().filter(SUPPs_NPC).nextNearest();
        SimpleItem food = ctx.inventory.populate().filter(FoodIds).next();
        SimpleItem ppots = ctx.inventory.populate().filter(PPotsIds).next();

        if (!ctx.groundItems.populate().filter(ITEM_IDs).isEmpty() && items != null) {
            status("Looting items");
            items.click("Take");
            ctx.sleepCondition(() -> !ctx.groundItems.populate().filter(ITEM_IDs).isEmpty(), 1000);
        }

        if (ctx.groundItems.populate().filter(ITEM_IDs).isEmpty()) {
            if (Giant != null && SUPPs == null) {
                if (ctx.getClient().getLocalPlayer().getInteracting() == null) {
                    Giant.click(0);
                    status("Attacking");
                }
            }
            if (SUPPs != null) {
                if (ctx.getClient().getLocalPlayer().getInteracting() == null) {
                    SUPPs.click(0);
                    status("Attacking Supp");
                }
            }
        }
        
        if (ctx.players.getLocal().getHealth() <= 30 && food != null) {
            food.click(0);
        }

        if (prayers.points() <= 30 && ppots != null) {
            ppots.click(0);
        }
    }

    @Override
    public void onChatMessage(ChatMessage chatMessage) {
    }

    @Override
    public void paint(Graphics graphics) {

    }

    @Override
    public void onTerminate() {
        ctx.log("ty fo usin me");
    }

    public void status(final String status) {
        this.status = status;
    }
}
