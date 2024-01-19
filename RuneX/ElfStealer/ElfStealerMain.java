package ElfStealer;

import simple.api.events.ChatMessageEvent;
import simple.api.filters.SimpleSkills;
import simple.api.listeners.SimpleMessageListener;
import simple.api.script.Category;
import simple.api.script.Script;
import simple.api.script.ScriptManifest;
import simple.api.script.interfaces.SimplePaintable;
import simple.api.wrappers.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Graphics2D;

@ScriptManifest(
        author = "alex",
        category = Category.THIEVING,
        description = "<html>"
                + "<p><strong>Elf Pickpocket</strong>.</p>"
                + "<ul>"
                + "<li><strong>A simple af Elf pickpocket to use to do your daily thieving quest with</strong>.</li>"
                + "<li>Start this at the Elves at ::dzone and have your last withdrawn loudout to whatever gear you want to be in when you pickpocket them Elves.</li>"
                + "<li>This way the bot can use XP tokens and not have any problems with them.</li>"
                + "<li>This script features a cute, <strong>USELESS</strong>, gui which allows you to stop and start the script (the gui disappears after you stop the script).</li>"
                + "</ul>"
                + "</html>",
        discord = "",
        name = "Elf Pickpocket",
        servers = {"RuneX"},
        version = "1.0"
)

public class ElfStealerMain extends Script implements SimpleMessageListener, SimplePaintable {
    public long startTime;
    public String status;
    public int startExperience;
    private ElfStealerGUI gui;
    private static boolean hidePaint = false;
    @Override
    public void onChatMessage(ChatMessageEvent chatMessageEvent) {
    }
    @Override
    public boolean onExecute() {
        System.out.println("Started Elf Pickpocket");
        this.startTime = System.currentTimeMillis();
        this.startExperience = ctx.skills.getExperience(SimpleSkills.Skill.THIEVING);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui = new ElfStealerGUI();
            }
        });
        return true;
    }
    @Override
    public void onTerminate() {
        if (gui != null) {
            gui.onCloseGUI();
        }
    }
    @Override
    public void onProcess() {
        SimpleNpc target = ctx.npcs.populate().filter("Elf").nextNearest();
        SimpleSceneObject bankchest = ctx.objects.populate().filter("Bank chest").nextNearest();
        if (gui != null && gui.isScriptStarted()) {
            if (ctx.inventory.inventoryFull()) {
                bankchest.interact(900);
                status("Deposit");
            }
            if (target != null && !ctx.inventory.inventoryFull()) {
                target.interact(20);
                status("Pickpocketing");
                ctx.sleep(250);
            }
        }
    }
    public void status(final String status) {
        this.status = status;
    }
    @Override
    public void onPaint(Graphics2D g1) {
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

            Font font1 = new Font("Karla", Font.BOLD, 10);
            g.setFont(font1);
            g.setColor(Color.GRAY);
            g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 380, 286);
            g.drawString("Status: " + status, 380, 298);
            int totalExp = ctx.skills.getExperience(SimpleSkills.Skill.THIEVING) - startExperience;
            g.drawString("XP Earned: " + ctx.paint.formatValue(totalExp), 380, 308);
            Font font2 = new Font("Karla", Font.BOLD, 12);
            g.setFont(font2);
            g.setColor(Color.WHITE);
            g.drawString("Elf Pickpocket  v. " + "1.0", 380, 274);
        }
    }
}
