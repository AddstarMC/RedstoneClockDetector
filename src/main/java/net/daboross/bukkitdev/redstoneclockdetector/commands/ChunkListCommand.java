package net.daboross.bukkitdev.redstoneclockdetector.commands;

import java.util.List;
import java.util.Map.Entry;
import net.daboross.bukkitdev.redstoneclockdetector.RCDPlugin;
import net.daboross.bukkitdev.redstoneclockdetector.utils.AbstractCommand;
import net.daboross.bukkitdev.redstoneclockdetector.utils.UsageException;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

public class ChunkListCommand extends AbstractCommand {

    protected RCDPlugin plugin;
    protected final int pageSize = 10;

    public ChunkListCommand(AbstractCommand[] children, RCDPlugin plugin) {
        super("chunklist [page]  List chunks of redstone activities.", "redstoneclockdetector.list", children);
        this.plugin = plugin;
    }

    @Override
    protected boolean execute(CommandSender sender, MatchResult[] data) throws UsageException {
        int pageNum = 1;
        if (data.length > 0) {
            Integer pageData = data[0].getInteger();
            if (pageData == null || pageData <= 0) {
                throw new UsageException(this.coloredUsage, "Page number should be a positive integer.");
            }
            pageNum = pageData;
        }
        int startIndex = (pageNum - 1) * this.pageSize;
        List<Entry<Chunk, Integer>> actList = this.plugin.getRedstoneChunkActivityList();
        int totalPage = actList.isEmpty() ? 0 : (actList.size() - 1) / this.pageSize + 1;
        sender.sendMessage("Page: " + ChatColor.YELLOW + pageNum + ChatColor.WHITE + "/" + ChatColor.GOLD + totalPage);
        if (startIndex >= actList.size()) {
            sender.sendMessage(ChatColor.GRAY + "No data.");
        } else {
            for (int i = startIndex, e = Math.min(startIndex + this.pageSize, actList.size()); i < e; ++i) {
                Entry<Chunk, Integer> entry = actList.get(i);
                Chunk c = entry.getKey();
                sender.sendMessage(String.format(
                        ChatColor.YELLOW.toString() + "%d" + ChatColor.WHITE + ". " + ChatColor.GREEN + "(%d, ?, %d) %s " + ChatColor.DARK_GREEN + "%d",
                        i + 1, (c.getX() * 16), (c.getZ() * 16), c.getWorld().getName(), entry.getValue()));
            }
        }
        return true;
    }
}
