package daPaintTest;

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
        category = Category.UTILITY,
        description = "<html>"
                + "<p></p>"
                + "<p><strong></strong></p>"
                + "<ul>"
                + "<li><strong></strong>.</li>"
                + "<li><strong></strong></li>"
                + "</ul>"
                + "</html>",
        discord = "",
        name = "daHillGr",
        servers = {"Zaros"},
        version = "4.0"
)

public class daPaintTestMain extends Script {
    final int[] EASY_CLUES = {2702, 2707, 2677, 2699, 2683, 2688, 2678, 2700, 2686, 2682, 2689, 2705, 2684, 2693, 2696, 2706, 2686, 2698};
    final int[] HARD_CLUES = {2786, 2737, 3520, 2792, 2799, 2733, 2776, 2780, 2735, 3538, 2790, 2731, 3524, 2782, 2797, 2741, 2729, 2783, 3532, 3530, 2723, 2739, 3540, 2722, 2796, 3542, 2774, 3525};
    final int[] ELITE_CLUES = {12105, 12091, 12086, 12076, 12083, 12073, 12097, 12089, 12079, 12088, 12105};
    final int[] RUNE_ITEMS = {1213, 830, 892, 1432, 811, 824, 44, 3101, 1319, 1163, 1359, 1275, 868, 1373, 1303, 1147, 1333, 19580, 1247, 9185, 4131, 1201, 1113};
    final int[] IMPORTANT_ITEMS = {20754, 10976, 22374, 10977, 995};
    final int[] KARUULM_ITEMS = {19677, 19681, 19683, 19679};
    final int[] FoodIds = {391, 385, 379, 7946, 13441, 373, 3144, 11936};
    final int[] PPotsIds = {143, 3030, 141, 3028, 139, 3026, 2434, 3024};
    final int[] ITEM_IDs = Stream.of(EASY_CLUES, HARD_CLUES, ELITE_CLUES, IMPORTANT_ITEMS, RUNE_ITEMS, KARUULM_ITEMS)
            .flatMapToInt(Arrays::stream)
            .toArray();
    final int[] LE_NPC = {2093, 2090, 2098, 8, 955, 7245, 7246, 7244};
    public String status;
    public long startTime;
    public int times_killed;
    public static boolean botStarted = true;
    // Gui
    private static daPaintTestGui gui;
    private void initializeGUI() {
        gui = new daPaintTestGui();
        gui.setVisible(true);
        gui.setLocale(ctx.getClient().getCanvas().getLocale());
    }
    @Override
    public void onExecute() {
        System.out.println("Started daSlayerHelper");
        this.startTime = System.currentTimeMillis();
        initializeGUI();
    }

    @Override
    public void onTerminate() {
        if (gui != null) {
            //gui.onCloseGUI();
            gui.dispose();
        }

    }

    @Override
    public void onProcess() {
        SimplePrayers prayers = new SimplePrayers(ctx);
        SimpleGroundItem items = ctx.groundItems.populate().filter(ITEM_IDs).nextNearest();
        SimpleNpc Giant = ctx.npcs.populate().filter(LE_NPC).nextNearest();
        SimpleItem food = ctx.inventory.populate().filter(FoodIds).next();
        SimpleItem ppots = ctx.inventory.populate().filter(PPotsIds).next();
        if (!ctx.groundItems.populate().filter(ITEM_IDs).isEmpty()) {
            status("Looting items");
            items.click("Take");
            ctx.sleepCondition(() -> !ctx.groundItems.populate().filter(ITEM_IDs).isEmpty(), 1000);
        }

        if (Giant != null && ctx.groundItems.populate().filter(ITEM_IDs).isEmpty()) {
            if (ctx.getClient().getLocalPlayer().getInteracting() == null) {
                Giant.click(0);
                status("Attacking");
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

    public void status(final String status) {
        this.status = status;
    }
}
