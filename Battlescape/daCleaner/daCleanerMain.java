package daCleaner;

import java.awt.*;
import java.util.regex.Pattern;
import java.awt.Graphics;
import java.awt.Graphics2D;

import simple.hooks.filters.SimpleSkills;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleGroundItem;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.script.Script;

@ScriptManifest(author = "alex", category = Category.HERBLORE, description = "Cleans all of ur grimy herbs", discord = "empty",
        name = "daCleaner", servers = { "Battlescape" }, version = "1.5")
public class daCleanerMain extends Script {
    public String status;
    public long startTime, currentExp;
    private static final int[] BANKING_OBJECT = {29321, 22819}; //ids for chests, bank booths etc
    public int count, startExperience;
    public int[] HerbIDs = {199, 201, 203, 205, 207, 209, 211, 213, 215, 217, 219, 222, 3051, 2485, 3049};
    private static boolean hidePaint = false;

    @Override
    public void onExecute() {
        System.out.println("Started daCleaner");
        this.startTime = System.currentTimeMillis();
        this.startExperience = ctx.skills.experience(SimpleSkills.Skills.HERBLORE);
        currentExp = this.ctx.skills.experience(SimpleSkills.Skills.HERBLORE);
    }

    @Override
    public void onProcess() {

        if (currentExp != this.ctx.skills.experience(SimpleSkills.Skills.HERBLORE)) {
            count++;
            currentExp = this.ctx.skills.experience(SimpleSkills.Skills.HERBLORE);
        }

        SimpleObject bank = ctx.objects.populate().filter(BANKING_OBJECT).nearest().next();
        SimpleItem herb = ctx.inventory.populate().filter(item -> item.getName().matches(".*Grimy.*")).next();

        if (herb != null && !ctx.bank.bankOpen()) {
            status("Cleaning");
            herb.click(0);
        }

        if (herb == null) {
            if (bank == null) {
                return;
            }
            if (!ctx.bank.bankOpen()) {
                status("Banking 1");
                bank.click(0);
                ctx.sleepCondition(() -> ctx.bank.bankOpen(), 2500);
                return;
            } else if (herb == null) {
                status("Banking 2");
                ctx.bank.depositInventory();
                ctx.sleepCondition(() -> !ctx.inventory.inventoryFull(), 2500);
                for (int itemId : HerbIDs) {
                    ctx.bank.withdraw(itemId, 28);
                    if (ctx.inventory.inventoryFull()){
                        break;
                    }
                }
                ctx.bank.closeBank();
            }
        }
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
        // Check if mouse is hovering over the paint
        Point mousePos = ctx.mouse.getPoint();
        if (mousePos != null) {
            Rectangle paintRect = new Rectangle(368, 260, 150, 75);
            hidePaint = paintRect.contains(mousePos.getLocation());
        }

        Graphics2D g = (Graphics2D) g1;

        if (!hidePaint){
            g.setColor(Color.DARK_GRAY);
            g.fillRect(368, 260, 150, 75);
            g.setColor(Color.BLACK);
            g.drawRect(368, 260, 150, 75);

            Font font1 = new Font("Karla", Font.BOLD, 10); // Adjust the font family, style, and size as desired
            g.setFont(font1);
            g.setColor(Color.GRAY);
            g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 380, 286);
            g.drawString("Status: " + status, 380, 298);
            g.drawString("Cleaned: " + ctx.paint.formatValue(count), 380, 308);
            Font font2 = new Font("Karla", Font.BOLD, 12); // Adjust the font family, style, and size as desired
            g.setFont(font2);
            g.setColor(Color.WHITE);
            g.drawString("daCleaner  v. " + "2.0", 380, 274);
        }
        }
}
