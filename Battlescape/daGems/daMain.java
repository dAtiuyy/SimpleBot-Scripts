package daGems;


import java.awt.*;

import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimpleSkills;
import simple.hooks.queries.SimpleEntityQuery;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.script.Script;


@ScriptManifest(author = "alex", category = Category.MINING, description = "Smashes gem rocks and banks it all", discord = "empty",
        name = "daGem Smasher", servers = { "Battlescape" }, version = "1.0")
public class daMain extends Script {

    private final WorldPoint BANK_TILE = new WorldPoint(3124, 3458, 0); // The bank tile we want to step to for banking

    public String status;

    public long startTime;
    public int startExperience, minerals;

    private static final int[] DROPPED_ITEMS = {5070, 5072, 5071};
    private static final int DEPOSIT_BOX = 25937;
    private static final int GEM_ID = 11380;


    @Override
    public void onExecute() {
        System.out.println("Started daGems");
        this.startExperience = ctx.skills.experience(SimpleSkills.Skills.MINING);
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onProcess() {

        if (ctx.inventory.inventoryFull()) {
            SimpleObject bank = ctx.objects.populate().filter(DEPOSIT_BOX).nearest().next();
            if (bank == null) {
                return;
            }
            if (ctx.bank.depositBoxOpen() == false) {
                status("Banking 1");
                bank.click("Deposit");
                ctx.sleepCondition(() -> ctx.bank.depositBoxOpen(), 2500);
                return;
            } else if (ctx.inventory.inventoryFull()) {
                status("Banking 2");
                ctx.bank.depositInventory();
                ctx.sleepCondition(() -> ctx.inventory.inventoryFull() == false, 2500);
                return;
            }
        }

        if (ctx.bank.depositBoxOpen() && !ctx.inventory.inventoryFull()) {
            status("Closing bank");
            ctx.bank.closeBank();
            ctx.sleepCondition(() -> ctx.bank.depositBoxOpen() == false, 2500);
        }

        if (ctx.groundItems.populate().filter(DROPPED_ITEMS).isEmpty() == false) {
            status("Looting items");
            ctx.groundItems.nearest().next().click("Take");
            ctx.sleep(2000);
            return;
        } // picks up whatever u tell it to lol

        SimpleObject rock = ctx.objects.populate().filter(GEM_ID).nearest().next();

        if (ctx.players.getLocal().getAnimation() == -1) {
            if (rock != null) {
                rock.click("Mine");
                status("call me a jeweler cuz im smashing gems fr fr");
                ctx.sleep(2500);
            }
        } else {
            ctx.sleepCondition(() -> ctx.players.getLocal().getAnimation() == -1, 2500);
        }
    }





    @Override
    public void onTerminate() {
    }

    @Override
    public void onChatMessage(ChatMessage m) {
        if (m.getMessage() != null) {
            String message = m.getMessage().toLowerCase();
            if (message.contains("be interested in these unidentified minerals")) {
                minerals++;
            }
        }
    }


    public void status(final String status) {
        this.status = status;
    }

    public void paint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;

        Font font = new Font("Arial", Font.BOLD, 12); // Adjust the font family, style, and size as desired
        g.setFont(font);
        g.setColor(Color.decode("#1C6497"));
        g.drawString("daGems    v. " + "1.0", 385, 286);
        g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 385, 298);
        g.drawString("Status: " + status, 385, 308);
        int totalExp = ctx.skills.experience(SimpleSkills.Skills.MINING) - startExperience;
        g.drawString("XP: " + ctx.paint.formatValue(totalExp) + " (" + ctx.paint.valuePerHour(totalExp/1000, startTime) + "k/Hour)", 385, 320);
        g.drawString("Minerals: " + ctx.paint.formatValue(minerals) + " (" + ctx.paint.valuePerHour(minerals, startTime) + " /Hour)", 385, 332);
    }
}