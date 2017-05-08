package me.varmetek.prison;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.varmetek.prison.anticheat.AntiAutoClicker;
import me.varmetek.prison.anticheat.AntiFly;
import me.varmetek.prison.anticheat.AntiSpeed;
import me.varmetek.prison.api.Area;
import me.varmetek.prison.api.DataManager;
import me.varmetek.prison.api.User;
import me.varmetek.prison.api.User.NotifyMode;
import me.varmetek.prison.commands.AutoActionCommand;
import me.varmetek.prison.commands.ChatSettingsCommand;
import me.varmetek.prison.commands.ClearChatCommand;
import me.varmetek.prison.commands.IgnoreCommand;
import me.varmetek.prison.commands.LocCommand;
import me.varmetek.prison.commands.LockChatCommand;
import me.varmetek.prison.commands.LookUpCommand;
import me.varmetek.prison.commands.MineSetCommand;
import me.varmetek.prison.commands.ModeCommand;
import me.varmetek.prison.commands.MotdCommand;
import me.varmetek.prison.commands.MsgCommand;
import me.varmetek.prison.commands.MuteCommand;
import me.varmetek.prison.commands.NotifyCommand;
import me.varmetek.prison.commands.PayCommand;
import me.varmetek.prison.commands.RankupCommand;
import me.varmetek.prison.commands.ReplyCommand;
import me.varmetek.prison.commands.ReportCommand;
import me.varmetek.prison.commands.SellCommand;
import me.varmetek.prison.commands.ServerSettingsCommand;
import me.varmetek.prison.commands.SpawnCommand;
import me.varmetek.prison.commands.StatsCommand;
import me.varmetek.prison.commands.UnIgnoreCommand;
import me.varmetek.prison.commands.WorthCommand;
import me.varmetek.prison.events.CrateListener;
import me.varmetek.prison.events.MainEvents;
import me.varmetek.prison.events.MineEvents;
import me.varmetek.prison.utils.InventoryUtil;
import me.varmetek.prison.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
public class Main extends JavaPlugin{
	private static  Plugin plugin;
	
	public static  WorldGuardPlugin wg;
	public static  WorldEditPlugin we;


	 public static final String BAN_MESSAGE = "Appeal at google.com";
	public static final String NO_PERM_MSG = "&cYou don't have permission to execute this command.";
	public static final Map<String,ItemStack> itemRegistry = new HashMap<String,ItemStack>();
	
	public static int resetSamples = 2000;
	public static Set<String> ignoredWorlds = new HashSet<String>();
	public static int maxRank;
	public static List<Double> rankupcosts ;
	public static Location spawn;
	public static Server server ;

	public void onEnable(){
		server =  Bukkit.getServer();
		wg = (WorldGuardPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		we = (WorldEditPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		
		 
		
		
		plugin = this;
		

		/*
		 * 
		 * 
		 * 
		 * Setting command Execcutors
		 * 
		 * 
		 * */
		
		/*JavaPlugin es = (JavaPlugin) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
		if(es!= null){
			es.getCommand("r").setExecutor(null);
		}*/
		getCommand("ignore").setExecutor(new IgnoreCommand());
		getCommand("unignore").setExecutor(new UnIgnoreCommand());
		getCommand("sell").setExecutor(new SellCommand());
		getCommand("worth").setExecutor(new WorthCommand());
		getCommand("rankup").setExecutor(new RankupCommand());
		getCommand("mine").setExecutor(new MineSetCommand());
		getCommand("stats").setExecutor(new StatsCommand());
		getCommand("prisonsettings").setExecutor(new ServerSettingsCommand());
		getCommand("autoaction").setExecutor(new AutoActionCommand());

		 //getCommand("rank").setExecutor(new RankCommand());
		 getCommand("lockchat").setExecutor(new LockChatCommand());
		 getCommand("clearchat").setExecutor(new ClearChatCommand());
		 //getCommand("setwarp").setExecutor(new SetWarpCommand());
		 //getCommand("warp").setExecutor(new WarpCommand());
		// getCommand("removewarp").setExecutor(new RemoveWarpCommand());
		 getCommand("chatsettings").setExecutor(new ChatSettingsCommand());
		 getCommand("pay").setExecutor(new PayCommand());
		// getCommand("balance").setExecutor(new BalanceCommand());
		// getCommand("ban").setExecutor(new BanCommand());
		// getCommand("unban").setExecutor(new UnbanCommand());
		// getCommand("tempban").setExecutor(new TempBanCommand());
		getCommand("spawn").setExecutor(new SpawnCommand());
		 getCommand("mute").setExecutor(new MuteCommand());
		// getCommand("invsee").setExecutor(new InvseeCommand());
		// getCommand("kick").setExecutor(new KickCommand());
		 getCommand("mode").setExecutor(new ModeCommand());
		 //getCommand("vanish").setExecutor(new VanishCommand());
		
		 getCommand("loc").setExecutor(new LocCommand());

		 getCommand("msg").setExecutor(new MsgCommand());
		 getCommand("reply").setExecutor(new ReplyCommand());
		 getCommand("notify").setExecutor(new NotifyCommand());
		 getCommand("report").setExecutor(new ReportCommand());
		 getCommand("motd").setExecutor(new MotdCommand());
		 getCommand("lookup").setExecutor(new LookUpCommand());
		 /*
		  * 
		  * 
		  * Setting tab completions
		  * 
		  * 
		  * */
		 
		 getCommand("pay").setTabCompleter(new PayCommand());
		 getCommand("ignore").setTabCompleter(new IgnoreCommand());
		 getCommand("unignore").setTabCompleter(new UnIgnoreCommand());
		 getCommand("notify").setTabCompleter(new NotifyCommand());
		 getCommand("lookup").setTabCompleter(new LookUpCommand());
		 getCommand("msg").setTabCompleter(new MsgCommand());
		 getCommand("mute").setTabCompleter(new MuteCommand());
		 getCommand("stats").setTabCompleter(new StatsCommand());
		/*
		 * 
		 * Setting event listeners
		 * 
		 * */
		 getServer().getPluginManager().registerEvents(new MainEvents(), this);
		 getServer().getPluginManager().registerEvents(new MineEvents(), this);
		 getServer().getPluginManager().registerEvents(new AntiFly(), this);
		 getServer().getPluginManager().registerEvents(new WorthCommand(), this);
		 getServer().getPluginManager().registerEvents(new AntiAutoClicker(), this);
		 getServer().getPluginManager().registerEvents(new AntiSpeed(), this);
		 getServer().getPluginManager().registerEvents(new CrateListener(), this);
		 /*
		  * 
		  * 
		  * Listing schedulers
		  * */
			server.getScheduler().scheduleSyncRepeatingTask(this, Utils.STEP.new CleanUserSet(),0L , 20L);
			server.getScheduler().scheduleSyncRepeatingTask(this, Utils.STEP.new AutoResetMines(),0L , 20L);
			server.getScheduler().runTaskTimer(this, Utils.STEP.new ClearInfractions(),0L , 1200L);
			server.getScheduler().runTaskTimer(this, Utils.STEP.new CheckSellBuffs(),0L , 20L);
			server.getScheduler().runTaskTimer(this, Utils.STEP.new CheckCombatTags(),0L , 20L);
			server.getScheduler().runTaskTimer(this, Utils.STEP.new UpdateAllScoreboards(),0L , 20L);
			//Bukkit.getServer().getScheduler().runTaskTimer(this, Utils.STEP.new CheckCharging(),0L , 3L);

			
			new BukkitRunnable(){
				public void run(){
					ignoredWorlds = Utils.list2set(DataManager.getConfig().getStringList("ignored"));
					if(ignoredWorlds == null){
						ignoredWorlds = new HashSet<String>();
					}
					spawn  = DataManager.getSpawn();
					maxRank = DataManager.getConfig().getInt("maxranks", 10);
					resetSamples = DataManager.getConfig().getInt("samples", 20);
					rankupcosts  = DataManager.getConfig().getDoubleList("rankupcosts");
					if(rankupcosts == null){
						rankupcosts = new ArrayList<Double>();
					}
					updateRankupCosts();
					registerItems();
					DataManager.loadGlobalShop();
					DataManager.loadAllShops();
					DataManager.loadAllMines();
					
					ShapelessRecipe srDVK = new ShapelessRecipe(itemRegistry.get("DVK"));
					srDVK.addIngredient(9,itemRegistry.get("BVK").getData() );
					
					ShapelessRecipe srUVK = new ShapelessRecipe(itemRegistry.get("UVK"));
					srUVK.addIngredient(9,itemRegistry.get("DVK").getData() );
				
					ShapelessRecipe srLVK = new ShapelessRecipe(itemRegistry.get("LVK"));
					srLVK.addIngredient(9,itemRegistry.get("UVK").getData() );
					server.addRecipe(srDVK);
					server.addRecipe(srUVK);
					server.addRecipe(srLVK);
					
					if(server.getOnlinePlayers().isEmpty()){this.cancel();}
				for(Player p : Bukkit.getOnlinePlayers()){
					User u = User.getUser(p);
					if(p.hasPermission("ariaprison.notify.staff")){
						u.setNotifyMode(NotifyMode.STAFF);
					}
					DataManager.loadUser(u);
					if(p.getGameMode() !=  org.bukkit.GameMode.CREATIVE){
				
						//Utils.clearInv(p);	
						u.setGameMode(User.GameMode.PLAYER);
						//u.setGameMode(User.GameMode.PLAYER);
						u.setArea(Area.SPAWN);
					}
					
					
				}
				
			}
				}.runTask(this);
		
				Iterator<Recipe> iter = server.recipeIterator();
				while (iter.hasNext()) {
					 Recipe recipe = (Recipe) iter.next();
					 if(recipe instanceof FurnaceRecipe)Utils.FurnaceRs.add((FurnaceRecipe)recipe);
				}
		
	 }
	public void onDisable(){
		DataManager.saveGlobalShop();
		if(spawn != null){
			DataManager.setSpawn(spawn);
			server.getLogger().severe("Spawn is set");
		}else{
			server.getLogger().severe("Spawn couldnot be set.");
		}
		DataManager.getConfig().set("samples", resetSamples);
		DataManager.getConfig().set("ignored", new ArrayList<String>(ignoredWorlds));
		DataManager.getConfig().set("maxranks", maxRank);
		DataManager.getConfig().set("rankupcosts", rankupcosts);
		DataManager.saveAllShops();
		DataManager.saveConfig();
		DataManager.saveAllUsers();
		DataManager.saveAllMines();
		for(User u: User.getAllUsers()){
			u.setCombatLog(-1);
		}
			
			
	}
	public static Plugin getPluginInstance(){
		return plugin;
	}
   
	public static ConsoleCommandSender getConsole(){
		return server.getConsoleSender();
	}

	private static void registerItems(){
		ItemStack i = null;
		ItemMeta im = null;
		
			i = new ItemStack(Material.GOLD_RECORD);
			im = i.getItemMeta();
			im.setDisplayName(Utils.colorCode("&2&lLegendary Crate Key"));
			im.addEnchant(Enchantment.DURABILITY, 1,true);
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			i.setItemMeta(im);
		
			itemRegistry.put("LVK", i);
		
			i = new ItemStack(Material.RECORD_9);
			im = i.getItemMeta();
			im.setDisplayName(Utils.colorCode("&7&lDull Crate Key"));
			im.addEnchant(Enchantment.DURABILITY, 1,true);
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			i.setItemMeta(im);
		
			itemRegistry.put("DVK", i);
		
			i = new ItemStack(Material.RECORD_11);
			im = i.getItemMeta();
			im.setDisplayName(Utils.colorCode("&c&lBroken Crate Key"));
			
			im.addEnchant(Enchantment.DURABILITY, 1,true);
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			i.setItemMeta(im);
			
		
			itemRegistry.put("BVK", i);
		
			 i = new ItemStack(Material.RECORD_7);
			im = i.getItemMeta();
			im.setDisplayName(Utils.colorCode("&5&lUnreal Crate Key"));
			im.addEnchant(Enchantment.DURABILITY, 1,true);
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			i.setItemMeta(im);
		
			itemRegistry.put("UVK", i);
		
			 i = new ItemStack(Material.NETHER_STAR);

				im = i.getItemMeta();
				im.setDisplayName(Utils.colorCode("&5&lFortune Upgrade"));
	
				List<String> lore = InventoryUtil.getLore(im);
				lore.add(Utils.colorCode("&7Put me on an item with fortune to upgrade that item."));
				im.setLore(lore);
				i.setItemMeta(im);
			
				itemRegistry.put("FUP", i);
		
		
		
		
		
	}
	 
	public static List<Double> updateRankupCosts(){
		List<Double> old = Main.rankupcosts;
		Main.rankupcosts = new ArrayList<Double>();
		for(int i = 0; i < Main.maxRank;i++){
			if(i> old.size()-1){
				Main.rankupcosts.add(0.0);
			}else{
				Main.rankupcosts.add(old.get(i));
			}
		}
		return Main.rankupcosts;
	}
	
}
