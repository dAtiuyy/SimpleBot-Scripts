package daTaskScript;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import daTaskScript.Tasks.daBankTask;
import daTaskScript.Tasks.daChopTask;
import simple.hooks.filters.SimpleSkills;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.scripts.task.TaskScript;
import simple.hooks.simplebot.ChatMessage;

@ScriptManifest(
        author = "alex",
        category = Category.WOODCUTTING,
        description = "<html>"
                + "<p>Basically daRedwood but task based</p>"
                + "<p><strong>Cuts redwood @ home in Battlescape</strong>.</p>"
                + "<ul>"
                + "<li><strong>Start @ da big redwood at home with the deposit box on ur screen</strong>.</li>"
                + "<li><strong>Have an axe equipped and dont worry about bird nests or logs, it picks up bird nests and bank em along with logs</strong>.</li>"
                + "</ul>"
                + "</html>",
        discord = "",
        name = "daRedwood2",
        servers = {"Battlescape"},
        version = "3.0"
)

public class daTaskScriptMain extends TaskScript {
    public static String status;
    public long startTime;
    public int startExperience, picked_items;
    private static final int[] BIRD_NEST = {5070, 5072, 5071};
    private static boolean hidePaint = false;
    private List tasks = new ArrayList();

    @Override
    public void onExecute() {
        tasks.addAll(Arrays.asList(new daBankTask(ctx), new daChopTask(ctx)));// Adds our tasks to our {task} list for execution
        System.out.println("Started daRedwood!");
        this.startExperience = ctx.skills.experience(SimpleSkills.Skills.WOODCUTTING);
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public List tasks() {
        return tasks;// Tells our TaskScript these are the tasks we want executed
    }

    @Override
    public boolean prioritizeTasks() {
        return true;// Will prioritize tasks in order added in our {tasks} List
    }

    // This method is not needed as the TaskScript class will call it, itself
    @Override
    public void onProcess() {
        // Can add anything here before tasks have been ran
        super.onProcess();// Needed for the TaskScript to process the tasks
        //Can add anything here after tasks have been ran
        if (!ctx.groundItems.populate().filter(BIRD_NEST).isEmpty() && !ctx.inventory.inventoryFull()) {
            status("Looting items");
            ctx.groundItems.nearest().next().click("Take");
            ctx.sleep(2000);
        } // picks up whatever u tell it to lol
    }

    @Override
    public void onTerminate() {
    }

    public static void status(final String status) {
        daTaskScriptMain.status = status;
    }

    @Override
    public void onChatMessage(ChatMessage m) {
        if (m.getMessage() != null) {
            String message = m.getMessage().toLowerCase();
            if (message.contains("falls out of the tree")) {
                picked_items++;
            }
        }
    }

    @Override
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
            int totalExp = ctx.skills.experience(SimpleSkills.Skills.WOODCUTTING) - startExperience;
            g.drawString("XP: " + ctx.paint.formatValue(totalExp) + " (" + ctx.paint.valuePerHour(totalExp / 1000, startTime) + "k/Hour)", 385, 320);
            g.drawString("Bird Nests: " + ctx.paint.formatValue(picked_items) + " (" + ctx.paint.valuePerHour(picked_items, startTime) + " /Hour)", 385, 332);
            Font font2 = new Font("Karla", Font.BOLD, 12); // Adjust the font family, style, and size as desired
            g.setFont(font2);
            g.setColor(Color.WHITE);
            g.drawString("daRedwood   v" + "3.0", 380, 274);
        }
    }
}