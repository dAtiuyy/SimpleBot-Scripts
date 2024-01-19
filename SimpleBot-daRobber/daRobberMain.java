package daRobber;

import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;

import simple.hooks.filters.SimpleSkills;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleWidget;
import simple.robot.script.Script;

@ScriptManifest(
        author = "alex",
        category = Category.THIEVING,
        description = "<html>"
                + "<p>Dumb master farmer thief for Battlescape</p>"
                + "<p><strong>Performs pickpockets</strong></p>"
                + "<ul>"
                + "<li><strong>Start @ home at the master farmer place</strong>.</li>"
                + "<li><strong>Drops the useless shit and keeps the good shit</strong></li>"
                + "</ul>"
                + "</html>",
        discord = "",
        name = "daRobber",
        servers = {"Battlescape"},
        version = "3.0"
)

public class daRobberMain extends Script {
    public String status;
    public long startTime;
    public int startExperience, times_stole;
    private static final int DA_VICTIM_ID = 3257;
    public int[] itemIds = {5306, 5281, 5324, 5318, 5322, 5096, 5319, 5308, 5101, 5305, 5309, 5329, 5307, 5320, 5282, 5280, 5098, 5102, 5310, 5099, 5318, 5311, 5104, 5322, 5099, 5103, 5105, 5106, 5291, 5292, 5293};
    //up there be a bunch of item IDs for seeds, these items will be dropped, anything else will be kept
    private static boolean hidePaint = false;


    @Override
    public void onExecute() {
        System.out.println("Started daRobber");
        this.startExperience = ctx.skills.experience(SimpleSkills.Skills.THIEVING);
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onProcess() {

        SimpleNpc pickpocket_ore = ctx.npcs.populate().filter(DA_VICTIM_ID).nearest().next();

        if (pickpocket_ore != null) {
            pickpocket_ore.click(0);
            status("Robbing");
        }

        if (ctx.inventory.inventoryFull()){
            for (int itemId : itemIds) {
                if (ctx.inventory.populate().filter(itemId).next() != null){
                    ctx.inventory.dropItem(ctx.inventory.populate().filter(itemId).next());
                }
            }
        }
    }

    @Override
    public void onChatMessage(ChatMessage m) {
        if (m.getMessage() != null) {
            String message = m.getMessage().toLowerCase();
            if (message.contains("ou pick the")) {
                times_stole++;
            }
        }
    }

    @Override
    public void onTerminate() {
        System.out.println("You stole this many times:");
        System.out.println(times_stole);
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

        if (!hidePaint) {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(368, 260, 150, 75);
            g.setColor(Color.BLACK);
            g.drawRect(368, 260, 150, 75);

            Font font1 = new Font("Karla", Font.BOLD, 10); // Adjust the font family, style, and size as desired
            g.setFont(font1);
            g.setColor(Color.GRAY);
            g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 380, 286);
            g.drawString("Status: " + status, 380, 298);
            int totalExp = ctx.skills.experience(SimpleSkills.Skills.THIEVING) - startExperience;
            g.drawString("XP: " + ctx.paint.formatValue(totalExp) + " (" + ctx.paint.valuePerHour(totalExp / 1000, startTime) + "k)", 380, 308);
            g.drawString("Pickpockets: " + ctx.paint.formatValue(times_stole) + " (" + ctx.paint.valuePerHour(times_stole, startTime) + ")", 380, 320);
            Font font2 = new Font("Karla", Font.BOLD, 12); // Adjust the font family, style, and size as desired
            g.setFont(font2);
            g.setColor(Color.WHITE);
            g.drawString("daRobber  v. " + "2.0", 380, 274);
        }
    }
}