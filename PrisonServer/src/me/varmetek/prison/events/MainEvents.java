package me.varmetek.prison.events;


import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import me.varmetek.prison.Main;
import me.varmetek.prison.api.Area;
import me.varmetek.prison.api.DataManager;
import me.varmetek.prison.api.User;
import me.varmetek.prison.api.User.NotifyMode;
import me.varmetek.prison.mine.Mine;
import me.varmetek.prison.utils.Messenger;
import me.varmetek.prison.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.util.Vector;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class MainEvents implements Listener{

	
	
	@EventHandler
	public void ping(ServerListPingEvent ev){

		ev.setMotd(Utils.colorCode(DataManager.getMotd()));
		ev.setMaxPlayers(0);
		
	}
	

	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void join(PlayerJoinEvent ev){
		final Player pl = ev.getPlayer();
	
		 ev.setJoinMessage("");
		User user =  User.getUser(pl);
		if(pl.hasPermission("ariaprison.notify.staff")){
			user.setNotifyMode(NotifyMode.STAFF);
		}
		pl.performCommand("spawn");
		boolean firstJoin =  ! DataManager.loadUser(user);

		 if(firstJoin)
			 ev.setJoinMessage( Utils.colorCode("&b&6&l&o" + pl.getName() + " &3has joined for the first time!"));

			DataManager.addDataBaseIP(pl.getUniqueId(), pl.getAddress().getAddress().getHostAddress()); 
			DataManager.addDataBaseName(pl.getUniqueId(), pl.getName()); 
		 
		 user.setGameMode(User.GameMode.PLAYER);
		 user.setArea(Area.SPAWN);
	 
	}
	
	@EventHandler
	public void leave(PlayerQuitEvent ev){
		Player pl = ev.getPlayer();
		ev.setQuitMessage("");
		User user =User.getUser(Bukkit.getOfflinePlayer(pl.getName()));
		
	
		if(user.getCombatLog() > 0){
			 ev.setQuitMessage(Utils.colorCode("&4"+pl.getName() + "&c has Combat Logged.")); 
			 pl.setHealth(0d);
			
		}
		DataManager.savePlayerUser(user);
		//pl.setPlayerListName("");
		 user.remove();
	
		 
		
	}
	
	@EventHandler
	public void foodCancel(FoodLevelChangeEvent e) { 
	e.setCancelled(true);
	  e.setFoodLevel(20); 
	  }

	@EventHandler
	public void onEventRespawn(final PlayerRespawnEvent ev){
		if(Main.spawn == null){
			ev.setRespawnLocation(ev.getPlayer().getWorld().getSpawnLocation());
		}else{
			ev.setRespawnLocation(Main.spawn);
		}
	}
/*
	@EventHandler
	public void rgLeave(RegionLeaveEvent ev){
	
		State sf;
		Player pl;
		ProtectedRegion rg;
		
		pl = ev.getPlayer();
		rg= ev.getRegion();
		if(rg == null){
			User.getUser(ev.getPlayer().getName()).setArea(null);
			return;
			}
		
		User user;
		user = User.getUser(pl.getName());
		if(user.getGameMode() != User.GameMode.PLAYER)return;
		if(rg.getFlag(DefaultFlag.ENABLE_SHOP) == null)return;
		
		sf = rg.getFlag(DefaultFlag.ENABLE_SHOP);
		
		if(sf == State.ALLOW &&user.isGodModed()){
			User.getUser(ev.getPlayer().getName()).setArea(null);
			Messenger.send("&cYou are no longer protected.", ev.getPlayer());
			
		}
		
		
	}
	@EventHandler
	public void rgEnter(RegionEnterEvent ev){
		Player pl;
		State sf;
		User user;
		ProtectedRegion rg;
		
		pl = ev.getPlayer();
		rg 	= ev.getRegion();
		if(rg == null){
			User.getUser(ev.getPlayer().getName()).setArea(null);
			return;
			}
		
		user = User.getUser(pl.getName());
		if(user.getGameMode() != User.GameMode.PLAYER)return;
		if(rg.getFlag(DefaultFlag.ENABLE_SHOP) == null)return;
	
		sf = rg.getFlag(DefaultFlag.ENABLE_SHOP);
		if(sf == State.ALLOW && (ev.getMovementWay() == MovementWay.TELEPORT || ev.getMovementWay() == MovementWay.SPAWN) ){
			user.setArea(Area.SPAWN);
			Messenger.send("&aYou are now protected.", ev.getPlayer());
			
		}else{
			if(ev.getMovementWay() == MovementWay.MOVE ){
			//pl.teleport(pl.getLocation().subtract(pl.getLocation().getDirection()));
				//Messenger.send("You may not renter spawn", pl, Messenger.WARN);
			}
		}
		
		
	}
*/	
	//@EventHandler
	public void kick(PlayerKickEvent ev){
		Player pl = ev.getPlayer();
	
		User user =User.getUser(Bukkit.getOfflinePlayer(pl.getName()));
	 DataManager.savePlayerUser(user );
		 	
	}
	

	 @EventHandler
	 public void onSignChange(SignChangeEvent sign)
	  {
		 String line[] = sign.getLines();
	
		if(!line[0].isEmpty()){
			
			if(line[0].toLowerCase().contains("[soup]")){
				line[0] = "&2[Soup]"; 
			}
			
			if(line[0].toLowerCase().contains("[free]")){
				line[0] = "&3[Free]"; 
				if(line[1].isEmpty()){
					line[1]= Utils.colorCode("&4  <ITEM>");
					return;
				}
				
				try{
					Material.valueOf(line[1].toUpperCase());
				}catch(IllegalArgumentException e){
					line[1]= Utils.colorCode("&4  <INVALID");
					return;
				}
				
			}
		}
		 for(int i = 0; i <4; i++){
			 if (sign.getLine(i).contains("&"));
			    sign.setLine(i, sign.getLine(i).replace("&", "§"));
		 }

	  }
	
	@EventHandler
	public void chat(AsyncPlayerChatEvent ev){
		
		Player player = ev.getPlayer();

		User playerUser = User.getUser(Bukkit.getOfflinePlayer(player.getName()));
		
		if(Utils.chatLocked && !PermissionsEx.getUser(player).has("ariaprison.commands.admin.chatsettings") ){
			
			Messenger.send(player, "&cChat is locked!");
			ev.setCancelled(true);
			return;
		}
		
		if(playerUser.getMuteExpire() == null ){
			ev.setCancelled(true);
			Messenger.send(player, "&cMuted forever.");
			return;
		}else{
			if( playerUser.getMuteExpire().after(Calendar.getInstance())){
				
				ev.setCancelled(true);
				Messenger.send(player, "&cMuted for " + Utils.getCalendarDiff(Calendar.getInstance(), playerUser.getMuteExpire()) + ".");
				return;
				}
		}
		if(playerUser.getChatCooldown() > System.currentTimeMillis()){
			ev.setCancelled(true);
			Messenger.send(player, "&cSlow down chat; " + Utils.chatDelay + " milisecond delay.");
		
			playerUser.setChatCooldown(System.currentTimeMillis()+ Utils.chatDelay);
			return;
		}
		{
		
		String msg =  ev.getMessage();
		boolean shouting = msg.startsWith("!");
		if(	player.hasPermission("ariaprison.chatcolor")){
			msg = Utils.colorCode(msg);
		}
		
		msg  = msg.replace("%", " percent");
		PermissionUser gr = PermissionsEx.getUser((Player)player);
		
		String format = Utils.colorCode("&7[&a&l"+Mine.getRankSymbol(playerUser.getMinerRank())+"&r&7]&r "+PermissionsEx.getUser(player).getPrefix()+playerUser.getRankTitle() +gr.getGroups()[0].getSuffix()+playerUser.getProdLvL().getIcon()+ "&8&l> &r"+gr.getSuffix());
		
		int per = 0;
		for(int i = 0;i<msg.length();i++){
			if(msg.charAt(i) =='%' ){
				per++;
			
				
			}
		}
		if(per>=10){
			Messenger.send(player, "&cWe can't allow spam now can we?");
			ev.setCancelled(true);
			return;
		}
		if(shouting ){
			msg = msg.substring(1, msg.length());
			format = Utils.colorCode("&8[&6&l!&8] ")+format;
	
		}
		format+=msg;
		
		Set<Player> toRemove = new HashSet<Player>();
		for(Player pl : ev.getRecipients()){
			User u = User.getUser(pl);
			if(u.getIgnoredPlayers().contains(player.getUniqueId())){
				toRemove.add(pl);
			}
			if(!shouting){
				if(!pl.getWorld().equals(player.getWorld()))continue;
				if(player.getLocation().distance(pl.getLocation())> 40d){
					toRemove.add(pl);
				}
			}
		}
		ev.getRecipients().removeAll(toRemove);
			
		ev.setMessage(msg);
		ev.setFormat(format);
		playerUser.setChatCooldown(System.currentTimeMillis() + Utils.chatDelay);
		
		}
		
	}
	

	
	@EventHandler
	public void killArrows(ProjectileHitEvent ev ){
		if(ev.getEntity() instanceof Arrow)
			ev.getEntity().remove();
		
	}
	
	@EventHandler(priority = EventPriority.HIGH,ignoreCancelled = true)
	public void onHit(EntityDamageByEntityEvent ev){
		if(!(ev.getEntity() instanceof Player)) return;
		if(Utils.getAttacker(ev.getDamager()) == null) return;
		//Player hurt = (Player) ev.getEntity();
		Player damager = Utils.getAttacker(ev.getDamager());
		Player damagee =(Player) ev.getEntity();
		
		if(damager == null || damagee == null) return;
		
		User drUser = User.getUser(damager);
		User deUser = User.getUser(damagee);
		
		if( drUser.isGodModed()|| deUser.isGodModed()){ev.setCancelled(true); return;}
	
		if(drUser.getCombatLog() <1){
			Messenger.send(damager,"&7You have combat tagged &c" +damagee.getName());
		}
		
		if(deUser.getCombatLog() <1){
			Messenger.send(damagee ,"&7You have been combat tagged by &c" +damager.getName());
		}
		drUser.setCombatLog(8);
		deUser.setCombatLog(8);
	
	}
	@EventHandler(ignoreCancelled = true)
	public void onHitKB(EntityDamageByEntityEvent ev){
		
		Entity it = ev.getDamager();
		Vector newVal = it.getVelocity();
	/*if(it instanceof LivingEntity){
			newVal.add(((LivingEntity)it).getEyeLocation().getDirection());
		}else{
			newVal.add(it.getLocation().getDirection());
		}*/
		
		newVal.setY(newVal.getY()+0.05);
		ev.getEntity().setVelocity(newVal);
		//ev.getEntity().setVelocity(); 
	}
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent ev){
		String msg = ev.getMessage().toLowerCase();
		String [] parts = msg.split(" ");
		Player pl =ev.getPlayer();
		User user = User.getUser(pl);
	
		
	
	if(parts[0].equalsIgnoreCase("/reload") && pl.isOp()){
		
			Utils.broadcast("RELOADING");
			//Main.shutDownSystem();
			return;
		}
		if(parts[0].contains("bukkit:")){
			ev.setCancelled(true);
			return;
		}
		//Utils.debug(parts[0]);
		if(parts[0].equalsIgnoreCase("/me")){
			ev.setCancelled(true);
			return;
		}
	
		/*boolean cantTeleport =  !Main.getPlayersNear(pl, 10,10,10).isEmpty() && !user.isGodModed();
		boolean rejectCommand;
	
		
		if(parts[0].equals("/spawn") && rejectCommand){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in combat.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/spawn") && cantTeleport){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in the 10 radius of a player.", pl, Messenger.WARN);
			return;
		}
	
		if(parts[0].equals("/renew") && rejectCommand){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in combat.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/renew") && cantTeleport){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in the 10 radius of a player.", pl, Messenger.WARN);
			return;
		}
		
		if(parts[0].equals("/warp") && rejectCommand){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in combat.", pl, Messenger.WARN);
			return;
		}
		
		if(parts[0].equals("/warp") && cantTeleport){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in the 10 radius of a player.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/mode") && rejectCommand){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in combat.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/mode") && cantTeleport){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in the 10 radius of a player.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/m") && rejectCommand){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in combat.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/m") && cantTeleport){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in the 10 radius of a player.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/repair") && rejectCommand){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in combat.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/repair") && cantTeleport){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in the 10 radius of a player.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/soup") && rejectCommand){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in combat.", pl, Messenger.WARN);
			return;
		}
		if(parts[0].equals("/soup") && cantTeleport){
			ev.setCancelled(true);
			Messenger.send("Command is blocked when in the 10 radius of a player.", pl, Messenger.WARN);
			return;
		}*/
	}
	
	
	
	@EventHandler 
	public void onWeatherChange(WeatherChangeEvent ev){
	
		/*ev.getWorld().setThunderDuration(8);
		ev.getWorld().set
		ev.getWorld().setWeatherDuration(10);*/
	
		if(!ev.getWorld().isThundering()){
			ev.setCancelled(true);
		}
	}
	
	@EventHandler 
	public void onThunderChange(ThunderChangeEvent ev){
		ev.setCancelled(true);
	//	ev.getWorld().setThunderDuration(10);
	}
	
	@EventHandler
	public void onDamage(final EntityDamageEvent ev){
		if(ev.getEntityType() != EntityType.PLAYER)return;
			final Player pl = (Player)ev.getEntity();
			User user = User.getUser(Bukkit.getOfflinePlayer(pl.getName()));
			if(ev.isCancelled()){return;}
			if(user.isGodModed()){
				ev.setDamage(0.0);
				
			}
			if(ev.getDamage() == 0){
				ev.setCancelled(true);
				return;
			}

	}
	
	
	@EventHandler
	public void freeze(PlayerMoveEvent ev){
		final Player pl = (Player)ev.getPlayer();
		User user = User.getUser(Bukkit.getOfflinePlayer(pl.getName()));
		if(user.isFrozen()){
			ev.setTo(ev.getFrom());
		}
	}
	@EventHandler
	public void toggleSprint(PlayerToggleSprintEvent ev){
		Player pl = ev.getPlayer();
		if(pl.isFlying() && pl.isSprinting()){
			pl.setVelocity(new Vector().zero());
		}
		
	}
	@EventHandler
	public void toggleFly(PlayerToggleFlightEvent ev){
		Player pl = ev.getPlayer();
		if(pl.isFlying() && pl.isSprinting() && pl.getWorld().getBlockAt(pl.getLocation().add(0d, -0.1, 0d)).getType() != Material.AIR){
			ev.setCancelled(true);
		}
		
	}
	@EventHandler
	public void superaFly(PlayerMoveEvent ev){
		final Player pl = (Player)ev.getPlayer();
		if(pl.isFlying() && pl.isSprinting()){
			float mSpeed = (pl.getFlySpeed()) *11;
		
			Vector vel = pl.getEyeLocation().getDirection().multiply(mSpeed);
			/*
			double dY = pl.getVelocity().getY();
			double dX = pl.getVelocity().getX();
			double dZ = pl.getVelocity().getZ();
			
			
			double cY = vel.getY();
			double cX = vel.getX();
			double cZ = vel.getZ();
			if( (Math.signum(dY) == -1 && dY<-mSpeed )|| (Math.signum(dY) == 1 && dY>mSpeed ) ){
				cY= 0;
			}
			if( (Math.signum(dX) == -1 && dX<-mSpeed )|| (Math.signum(dX) == 1 && dX>mSpeed ) ){
				cX= 0;
			}
			if( (Math.signum(dZ) == -1 && dZ<-mSpeed )|| (Math.signum(dZ) == 1 && dZ>mSpeed ) ){
				cZ= 0;
			}*/
			//vel.add(new Vector(cY,cX,cZ));
			Effect ef = Effect.LAVA_POP;
			int id = 100;
			pl.getWorld().playEffect(ev.getFrom(), ef,id);
			//pl.getWorld().playEffect(pl.getEyeLocation(), ef, id);
			pl.getWorld().playEffect(pl.getLocation(), ef, id);
			pl.getWorld().playEffect(ev.getTo(), ef ,id);
		
			/*pl.setVelocity(pl.getVelocity().add(new Vector(cX,cY,cZ)));*/
			pl.setVelocity(vel);
		}
	}
	

	
	

	
	@EventHandler
	public void clearInvOnClose(InventoryCloseEvent ev){
		User user = User.getUser((Player) ev.getPlayer());
		if(user.isInvSee()){
			user.setInvSee(false);
		}
		
			user.setInv(null);
			user.setGui(null);
		
		
		
	}
	
	@EventHandler
	public void onPower(BlockRedstoneEvent ev){
		if(Main.ignoredWorlds.contains(ev.getBlock().getWorld().getName())){
				ev.setNewCurrent(0);
		}
	}
	

	/*public void interactTest(PlayerInteractEvent ev){
		
		if(ev.hasItem()){
			if(ev.getMaterial() == Material.BOOK_AND_QUILL){
				Utils.debug("No item");
				ev.setUseItemInHand(Result.DENY);
				ev.setCancelled(true);
			//	Player pl = ev.getPlayer();

				
			}
		}
	}*/


	

/*	
	public void onRegionEnter(RegionEnterEvent ev){
		if (ev.getRegion().getId().equals("spawn")){
			if (ev.getMovementWay().equals(MovementWay.MOVE)){
				Player pl = ev.getPlayer();
				//Block loc = ev.getPlayer().getLocation().getBlock();
				User user = User.getUser(pl.getName());
				if(user.getGameMode() == User.GameMode.PLAYER){
					pl.teleport(pl.getLocation().subtract(pl.getLocation().getDirection()));
					Messenger.send("You may not renter spawn", pl, Messenger.WARN);
			
				}
			}	
		}
		
	}
	//@EventHandler
	public void onRegionLeave(RegionLeaveEvent e){
		Player player = e.getPlayer();
		User user = User.getUser(player.getName());
		if (user.getArea()== Area.SPAWN){
			if (e.getMovementWay() == MovementWay.MOVE ||e.getMovementWay() ==  MovementWay.TELEPORT ){	
				user.setArea(null);
			}
		}
	
	}
*/
}
