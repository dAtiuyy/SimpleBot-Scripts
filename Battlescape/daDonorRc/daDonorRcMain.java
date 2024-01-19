package daDonorRc;

import java.awt.*;
import java.util.regex.Pattern;
import java.awt.Graphics;
import java.awt.Graphics2D;

import net.runelite.api.coords.WorldPoint;
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
        category = Category.RUNECRAFTING,
        description = "<html>"
                + "<p>Dumb rooncrafter for Battlescape</p>"
                + "<p><strong>makes roones</strong></p>"
                + "<ul>"
                + "<li>Start <strong>@ the second bank chest at ::dz with the roone altar on ur screen</strong>.</li>"
                + "<li><strong>Have pure ess in ur bank lel</strong></li>"
                + "</ul>"
                + "</html>",
        discord = "",
        name = "daDonorRc",
        servers = {"Battlescape"},
        version = "2.0"
)

public class daDonorRcMain extends Script {
    public String status;
    public long startTime, currentExp;
    private static final int[] BANKING_OBJECT = {29321, 22819}; //ids for chests, bank booths etc
    public int count, startExperience;
    private static boolean hidePaint = false;

    @Override
    public void onExecute() {
        System.out.println("Started daDonorRc");
        this.startTime = System.currentTimeMillis();
        this.startExperience = ctx.skills.experience(SimpleSkills.Skills.RUNECRAFT);
        currentExp = this.ctx.skills.experience(SimpleSkills.Skills.RUNECRAFT);
    }

    @Override
    public void onProcess() {
        SimpleObject bank = ctx.objects.populate().filter(BANKING_OBJECT).nearest().next();
        SimpleItem rune = ctx.inventory.populate().filter(7936).next();
        SimpleObject altar = ctx.objects.populate().filter(29631).nearest().next();

        if (rune != null && !ctx.bank.bankOpen()) {
            status("rooning");
            altar.click(0);
            count++;
        }

        if (rune == null) {
            if (bank == null) {
                return;
            }
            if (ctx.bank.bankOpen() == false) {
                ctx.pathing.step(new WorldPoint(3032, 3492, 0));
                status("Banking 1");
                bank.click(0);
                ctx.sleepCondition(() -> ctx.bank.bankOpen(), 2500);
            } else if (rune == null) {
                status("Banking 2");
                ctx.bank.depositInventory();
                ctx.sleepCondition(() -> ctx.inventory.inventoryFull() == false, 2500);
                ctx.bank.withdraw(7936, 28);
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
            Rectangle paintRect = new Rectangle(10, 338, 510, 140);
            hidePaint = paintRect.contains(mousePos.getLocation());
        }

        Graphics2D g = (Graphics2D) g1;

        if (!hidePaint){
            Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("imgRC.png"));
            g.drawImage(image, 0, 338,null);
            g.setColor(Color.RED);
            g.drawRect(0, 338, 519, 165);

            Font font1 = new Font("Karla", Font.BOLD, 18); // Adjust the font family, style, and size as desired
            g.setFont(font1);
            g.setColor(Color.GRAY);
            g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 20, 450);
            g.drawString("Status: " + status, 20, 410);
            int totalExp = ctx.skills.experience(SimpleSkills.Skills.RUNECRAFT) - startExperience;
            g.drawString("XP: " + ctx.paint.formatValue(totalExp) + " (" + ctx.paint.valuePerHour(totalExp / 1000, startTime) + "k)", 20, 430);
            Font font2 = new Font("Karla", Font.BOLD, 12); // Adjust the font family, style, and size as desired
            g.setFont(font2);
            g.setColor(Color.WHITE);
            g.drawString("2.0", 330, 377);
        }
    }
}
