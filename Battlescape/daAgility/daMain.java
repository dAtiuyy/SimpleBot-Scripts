package daAgility;

import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimpleSkills.Skills;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.simplebot.Pathing;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.script.Script;

import java.awt.*;

@ScriptManifest(author = "Reminisce && Alex", category = Category.AGILITY, description = "Does agility course", discord = "empty",
        name = "daAgility", servers = { "Battlescape" }, version = "0.1")
public class daMain extends Script {

    public String status;
    public long startTime;
    public int startExperience;
    private final WorldPoint gap3_problem = new WorldPoint(2653, 3310, 3);

    public void onExecute() {
        this.status("Starting daAgility!!!");
        this.startExperience = ctx.skills.experience(Skills.AGILITY);
        this.startTime = System.currentTimeMillis();
    }

    public void onProcess() {
        final Pathing pathing = ctx.pathing;
        WorldPoint currentLocation = ctx.players.getLocal().getLocation();

        if (pathing.reachable(new WorldPoint(2673, 3298, 0))) {
            interactWithObstacle("Climbing Wooden beams", "wooden beams", "climb-up", new WorldPoint(2671, 3299, 3));
        } else if (currentLocation.distanceTo(new WorldPoint(2671, 3299, 3)) == 0) {
            interactWithObstacle("Jumping gap", "Gap", "Jump", new WorldPoint(2665, 3318, 3));
        } else if (currentLocation.distanceTo(new WorldPoint(2665, 3318, 3)) == 0) {
            interactWithObstacle("Crossing plank", "Plank", "Walk-on", new WorldPoint(2656, 3318, 3));
        } else if (currentLocation.distanceTo(new WorldPoint(2656, 3318, 3)) == 0) {
            interactWithObstacle("Jumping gap 2", "Gap", "Jump", new WorldPoint(2653, 3314, 3));
        } else if (currentLocation.distanceTo(new WorldPoint(2653, 3314, 3)) == 0) {
            pathing.step(new WorldPoint(2653, 3310, 3));
            ctx.sleep(1000);
            if (currentLocation.distanceTo(new WorldPoint(2653, 3314, 3)) == 0) {
                ctx.sleep(1000);
                interactWithObstacle("Jumping gap 3", "Gap", "Jump", new WorldPoint(2651, 3309, 3));
            }
        } else if (currentLocation.distanceTo(new WorldPoint(2651, 3309, 3)) == 0) {
            interactWithObstacle("Balancing across roof", "Steep roof", "Balance-across", new WorldPoint(2656, 3297, 3));
        } else if (currentLocation.distanceTo(new WorldPoint(2656, 3297, 3)) == 0) {
            interactWithObstacle("Last Gap", "Gap", "Jump", new WorldPoint(2668, 3297, 3));
        }
    }


    private void interactWithObstacle(String status, String objectName, String action, WorldPoint nextLocation) {
        final SimpleObject o = ctx.objects.populate().filter(objectName).filterHasAction(action).nearest().next();
        if (o != null && o.validateInteractable()) {
            this.status(status);
            if (o.click(action)) {
                ctx.onCondition(() -> !ctx.pathing.reachable(nextLocation), 250, 14);
            }
        }
    }


    @Override
    public void onTerminate() {
    }

    public void paint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        Font font = new Font("Arial", Font.BOLD, 12); // Adjust the font family, style, and size as desired
        g.setFont(font);
        g.setColor(Color.decode("#1C6497"));
        g.drawString("daAgility    v. " + "1.0", 385, 286);
        g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 385, 298);
        g.drawString("Status: " + status, 385, 308);
        int totalExp = ctx.skills.experience(Skills.AGILITY) - startExperience;
        g.drawString("XP: " + ctx.paint.formatValue(totalExp) + " (" + ctx.paint.valuePerHour(totalExp/1000, startTime) + "k/Hour)", 385, 320);
        }

    public void status(final String status) {
        this.status = status;
    }

    @Override
    public void onChatMessage(final ChatMessage e) {
    }


}