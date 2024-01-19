package TeakNMahoganyCutter;

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
        category = Category.WOODCUTTING,
        description = "<html>"
                + "<p><strong>TeakNMahogany Cutter</strong>.</p>"
                + "<ul>"
                + "<li><strong>YOU MUST HAVE A DONATOR RANK FOR THIS SCRIPT TO WORK!!! IT USES THE PREMIUM BANKER!!!</strong></li>"
                + "<li><strong>A simple af Teak & Mahogany cutter for you to use</strong>.</li>"
                + "<li><strong>It supports Teak & Mahogany trees that are at Hardwood Groove please have your last withdrawn loudout to have the required tool, an axe</strong>.</li>"
                + "<li>Start this at the Hardwood Groove (woodcutting teles) and have your last withdrawn loudout to whatever gear you want to be in when you fish.</li>"
                + "<li>This way the bot can use XP tokens and not have any problems with them.</li>"
                + "<li>This script features a cute, <strong>USELESS</strong>, gui which allows you to stop and start the script (the gui disappears after you stop the script).</li>"
                + "</ul>"
                + "</html>",
        discord = "",
        name = "TeakNMahogany Cutter",
        servers = {"RuneX"},
        version = "1.0"
)

public class TeakNMahoganyCutterMain extends Script implements SimpleMessageListener, SimplePaintable {
    public long startTime;
    public String status;
    public int startExperience;
    private TeakNMahoganyCutterGUI gui;
    private static boolean hidePaint = false;
    @Override
    public void onChatMessage(ChatMessageEvent chatMessageEvent) {
    }
    @Override
    public boolean onExecute() {
        this.startTime = System.currentTimeMillis();
        this.startExperience = ctx.skills.getExperience(SimpleSkills.Skill.WOODCUTTING);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui = new TeakNMahoganyCutterGUI();
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
        String selectedItem = gui.getSelectedItem();
        SimpleSceneObject teak = ctx.objects.populate().filter(9036).nextNearest();
        SimpleSceneObject maho = ctx.objects.populate().filter(9034).nextNearest();
        SimpleNpc banker = ctx.npcs.populate().filter(4519).nextNearest();
        if (gui != null && gui.isScriptStarted()) {
            if (ctx.inventory.inventoryFull()) {
                banker.interact(225);
                status("Deposit");
            }
            if (!ctx.inventory.inventoryFull() && ctx.players.getLocal().getAnimation() == -1) {
                if (teak != null && "Teak".equals(selectedItem)) {
                    teak.interact(502);
                    ctx.onCondition(() -> ctx.players.getLocal().getInteracting() != null, 250, 6);
                    status("Cutting Teak");
                }
                if (maho != null && "Mahogany".equals(selectedItem)) {
                    maho.interact(502);
                    ctx.onCondition(() -> ctx.players.getLocal().getInteracting() != null, 250, 6);
                    status("Cutting Mahogany");
                }
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
            int totalExp = ctx.skills.getExperience(SimpleSkills.Skill.WOODCUTTING) - startExperience;
            g.drawString("XP Earned: " + ctx.paint.formatValue(totalExp), 380, 308);
            Font font2 = new Font("Karla", Font.BOLD, 12);
            g.setFont(font2);
            g.setColor(Color.WHITE);
            g.drawString("TeakNMaho Cutter  v. " + "1.0", 380, 274);
        }
    }
}
