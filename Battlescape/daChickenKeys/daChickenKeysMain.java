package daChickenKeys;

import java.awt.*;
import java.util.regex.Pattern;
import java.awt.Graphics;
import java.awt.Graphics2D;

import simple.hooks.filters.SimplePrayers;
import simple.hooks.filters.SimpleSkills;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleGroundItem;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.script.Script;

@ScriptManifest(
        author = "alex",
        category = Category.MONEYMAKING,
        description = "<html>"
                + "<p><strong>Helps you do slayer</strong>.</p>"
                + "<ul>"
                + "<li><strong>Start whenever you want, it does actions only when it needs to</strong>.</li>"
                + "<li><strong>It drinks prayer pots and collects items for you</strong>.</li>"
                + "<li><strong>It picks up clues, ckey pieces,barrows amulets, dark totem pieces, ancient sharks, brim keys and some valuable items</strong>.</li>"
                + "</ul>"
                + "</html>",
        discord = "",
        name = "daSlayerHelper",
        servers = {"Battlescape"},
        version = "2.0"
)

public class daChickenKeysMain extends Script {
    public String status;

    public long startTime;
    private static final int[] DESIRED_ITEMS = {985, 987, 989, 4151, 19677, 19679, 19681, 19683, 23083, 60005, 60006, 60007, 60038, 60053, 60054, 60055, 60056};
    /*
    60053 & 60054 & 60055 & 60056 coin bags 1, 2, 3, 4
    60005 & 60006 & 60007 med hard elite clue
    985 & 987 key pieces, tooth and loop 989 is the key
    19677 ancient shard
    19679 & 19681 & 19683 base, middle, top totem pieces
    23083 brimstone key
    4151 abby whip
    60038 barrows amulet
    */
    private static final int BANKING_OBJECT = 25937;
    public int TOOTH_PIECE, LOOP_PIECE, KEYS, COIN_BAGS_1, COIN_BAGS_2, COIN_BAGS_3, COIN_BAGS_4;


    @Override
    public void onExecute() {
        System.out.println("Started daSlayerHelper");
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onProcess() {

        SimplePrayers prayers = new SimplePrayers(ctx);
        SimpleObject bank = ctx.objects.populate().filter(BANKING_OBJECT).nearest().next();
        SimpleGroundItem items = ctx.groundItems.populate().filter(DESIRED_ITEMS).nearest().next();

        if (ctx.inventory.inventoryFull()) {
            if (bank == null) {
                return;
            }
            if (!ctx.bank.depositBoxOpen()) {
                status("Banking 1");
                bank.click("Deposit");
                ctx.sleepCondition(() -> ctx.bank.depositBoxOpen(), 2500);
                return;
            } else if (ctx.inventory.inventoryFull()) {
                status("Banking 2");
                ctx.bank.depositInventory();
                ctx.sleepCondition(() -> !ctx.inventory.inventoryFull(), 2500);
                return;
            }
        }

        if (ctx.bank.depositBoxOpen() && !ctx.inventory.inventoryFull()) {
            status("Closing bank");
            ctx.bank.closeBank();
            ctx.sleepCondition(() -> !ctx.bank.depositBoxOpen(), 2500);
        }

        if (!ctx.groundItems.populate().filter(DESIRED_ITEMS).isEmpty()) {
            status("Looting items");
            items.click("Take");
            ctx.sleepCondition(() -> !ctx.groundItems.populate().filter(DESIRED_ITEMS).isEmpty(), 1000);
        } // picks up whatever u tell it to lol

        if (prayers.points() <= 30){
            final SimpleItem potion = ctx.inventory.populate().filter(Pattern.compile("Prayer potion\\(\\d+\\)")).next();
            final int cached = ctx.prayers.points();
            status("Drinking Prayer Potion");
            if (potion != null && potion.click(0)) {
                ctx.onCondition(() -> ctx.prayers.points() > cached, 250, 12);
            }
        } // da pray point check and use pot

        if (ctx.skills.level(SimpleSkills.Skills.ATTACK) <= 99) {
            final SimpleItem potionS = ctx.inventory.populate().filter(Pattern.compile("Divine super combat potion\\(\\d+\\)")).next();
            status("Drinking DSCombat Potion");
            if (potionS != null) {
                potionS.click(0);
            }
        } // da combat pot check and drinking

        status("Attacking shit");
        TOOTH_PIECE = ctx.inventory.populate().filter(985).population(); //count how many items ya got in da inv
        LOOP_PIECE = ctx.inventory.populate().filter(987).population();
        KEYS = ctx.inventory.populate().filter(989).population()*2;
        COIN_BAGS_1 = ctx.inventory.populate().filter(60053).population(true); //same shit but for stack-able shit like clues or coin bags
        COIN_BAGS_2 = ctx.inventory.populate().filter(60054).population(true);
        COIN_BAGS_3 = ctx.inventory.populate().filter(60055).population(true);
        COIN_BAGS_4 = ctx.inventory.populate().filter(60056).population(true);
    }

    @Override
    public void onTerminate() {
    }

    @Override
    public void onChatMessage(ChatMessage m) {
    }


    public void status(final String status) {
        this.status = status;
    }

    public void paint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(368, 260, 150, 75);
        g.setColor(Color.BLACK);
        g.drawRect(368, 260, 150, 75);
        //g.fillRect(0, 338, 519, 165); //perfect black chat box

        Font font1 = new Font("Karla", Font.BOLD, 10); // Adjust the font family, style, and size as desired
        g.setFont(font1);
        g.setColor(Color.GRAY);
        g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 380, 286);
        g.drawString("Status: " + status, 380, 298);
        g.drawString("Pieces: " + ctx.paint.formatValue(TOOTH_PIECE + LOOP_PIECE + KEYS) + " (" + KEYS/2 + " Keys)", 380, 308);
        g.drawString("Bags: " + ctx.paint.formatValue(COIN_BAGS_1 + COIN_BAGS_2 + COIN_BAGS_3 + COIN_BAGS_4), 380, 320);
        Font font2 = new Font("Karla", Font.BOLD, 12); // Adjust the font family, style, and size as desired
        g.setFont(font2);
        g.setColor(Color.WHITE);
        g.drawString("daSlayerHelper  v. " + "2.0", 380, 274);
        }
}
