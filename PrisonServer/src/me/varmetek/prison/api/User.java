	package me.varmetek.prison.api;



import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import me.varmetek.prison.Main;
import me.varmetek.prison.mine.Mine;
import me.varmetek.prison.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.earth2me.essentials.api.Economy;







	public class User {
		
		public static Map<OfflinePlayer , User> users = new HashMap< OfflinePlayer , User>();
		private Set<UUID> ignoredPlayers = new HashSet<UUID>();
		private List<SellBuff> sellBuffs = new ArrayList<SellBuff>();
		private Violations  infs =  new Violations ();
		private  Map<AutoAction,Boolean> autoActions = new HashMap<AutoAction,Boolean>();
		private Long chatCooldown = 0L;
		private Calendar muteTime = Calendar.getInstance();
		//private Rank rank = Rank.Default;
		private GuiApi gui = null;
		private OfflinePlayer player;
		private UUID id;
		private Inventory inv = null;
		private String banMessage = "";
		private Calendar banExpire = Calendar.getInstance();
		private GameMode gameMode = GameMode.PLAYER;
		private Rank vanishedTo = null;
		private boolean godmode = false;
		private boolean frozen = false;
		private Player lastMsgReciever = null;
		private int combatTimer =-1;
		private Area area = null;
		private Scoreboard scoreB = Bukkit.getScoreboardManager().getNewScoreboard();
		private Objective sbStats;
		private boolean isInvSee = false;
		private ProducerLevel prodLvL = ProducerLevel.NONE;
		private NotifyMode  nMode = NotifyMode.NORMAL;
		private int minerRank = 0;
		private boolean autosmelt = false;
		private boolean autoPack = false;
		private SideBar sb;

		
		public enum GameMode{
			PLAYER(Rank.Default,"p","pl","play"),
			BUILDER(Rank.Admin,"b","build"),
			STAFF(Rank.TrialMod,"a","admin");
			
			public String[] alias;
			public Rank requiredRank;
			private GameMode(Rank r,String...s){
				alias = s;
				requiredRank = r;
			}
			public static GameMode getByName(String mode){
				
				for(GameMode temp : GameMode.values()){
					if(Arrays.asList(temp.alias).contains(mode.toLowerCase()) || temp.name().toLowerCase().equalsIgnoreCase(mode.toLowerCase())){
						return temp;
					}
				}
				return null;
			}
		}
		
		public enum ProducerLevel{
			YOUTUBE("&9[&f&lYou&4&lTube&9]"),
			TWITCH("&9[&5&lTwitch&9]"),
			HITBOX("&9[&2HitBox&9]"),
			DAILYMOTION("&9[&3DailyMotion&9]"),
			NONE("");
			
			private String icon;;
			private ProducerLevel(String suf){
			icon = suf;
			}
			
			public String getIcon(){
				return icon;
			}
		}
		
		public enum NotifyMode{
			
			NORMAL(null),
			STAFF(NotifyMode.NORMAL),
			DEBUG(NotifyMode.STAFF);
			
			
			private NotifyMode child;
			private NotifyMode( NotifyMode child){
				this.child = child;
			}
			
			public boolean doesInherient(NotifyMode checkRank){
				NotifyMode  tempRank  = this;
				
				
				while (tempRank != checkRank) {
					tempRank = tempRank.child;
					if( tempRank == null){
						return false;
					}
				}
				
				if(tempRank == checkRank){
					return true;
				}
				
				return false;
			}
		}
	 
		public enum AutoAction{
	 			SMELT,PACK,AURA,SELL;
	 			
	 			static Map<AutoAction,Boolean> setup(  Map<AutoAction,Boolean> aa){
	 				for(AutoAction a:AutoAction.values()){
	 					aa.put(a, false);
	 				}
	 				return aa;
	 			}
	 			
	 	}
	 	private User (OfflinePlayer name){
			
				//userID = name;
				player = name;
				id = name.getUniqueId();
				sb = new SideBar(name.getName());
				sb.setDisplayName("&7&lAria&c&lPrison");
				if(name.isOnline()){
					((Player)name).setScoreboard(sb.getBoard());
				}
				AutoAction.setup(autoActions);
				users.put(name, this);
		
				updateScoreBoard();
				
				
				
			
		}
		
	 	

		
		public static User getUser(OfflinePlayer pl){
			
			if(pl.equals(null)){
				return null;
			}
		
			User i = users.get(pl);
			
			if(i == null){
				i = new User(pl);
			}
			/* i.sbStats.getScore(Utils.colorCode("&6Kills")).setScore(i.kills);
			 i.sbStats.getScore(Utils.colorCode("&6Deaths")).setScore(i.deaths);
			 i.sbStats.getScore(Utils.colorCode("&6Killstreak")).setScore(i.killstreak);
			 i.sbStats.getScore(Utils.colorCode("&5Bits")).setScore(i.money);*/
			
			//i.updateScoreBoard();
		
			//i.refreshListName();
			return i;
			
		}
		
		public static User[] getAllUsers(){
			return  users.values().toArray(new User[0]);
			
		}
		
		/*public static List<User> getAllPermedUsers(Rank rank){
			List<User> list = new ArrayList<User>();
			for(User u : getAllUsers()){
				if(u.player.isOnline()){
					if(u.hasPermission(rank)){
						list.add(u);
					}
				}
			}
			return list;
		}*/
		
		/*public static List<User> getAllRankedUsers(Rank rank){
			List<User> list = new ArrayList<User>();
			
			for(User u : getAllUsers()){
				if(u.player.isOnline()){
					if(u.getRank() == rank ){
						list.add(u);
					}
				}
			}
			return list;
		}
		
		public static List<User> getAllVanishedUsers(Rank rank){
			List<User> list = new ArrayList<User>();
			
			for(User u : getAllUsers()){
				if(u.player.isOnline()){
					if(u.isVanished() ){
						list.add(u);
					}
				}
			}
			return list;
		}*/
		public static List<User> getUsersWithNotify(NotifyMode mode){
			List<User> list = new ArrayList<User>();
			
			for(User u : getAllUsers()){
				if(u.player.isOnline()){
					if(u.nMode == mode){
						list.add(u);
					}
				}
			}
			return list;
		}


		/////////////////////////////////////////////////////////////////	
		public String getUserID(){
			return player.getName();
		}
		
		
		public OfflinePlayer getPlayer(){
			
			return player;
		}
		public String getRankTitle(){
			if(player.isOnline()){
				PermissionUser gr = PermissionsEx.getUser((Player)player);
				return gr.getGroups()[0].getPrefix()+ player.getName();
				
			}else{
				return player.getName();
			}
		}
		/*public String getRankTitle(){
			return Utils.colorCode(rank.prefix + player.getName() + rank.suffix);
		}*/
	 	public boolean isVanished(){
			return (vanishedTo != null);
		}
		
		
		public Rank getVanishedTo(){
			return vanishedTo;
		}
		
		
		/*public User setVanishedTo(Rank r){
			if(!player.isOnline()){
				
				return this;
			}
			vanishedTo = r;
			refreshVanish();
				return this;	
		}*/
		
		
		@SuppressWarnings("deprecation")
		/*public User refreshVanish(){
			if(!player.isOnline())return this;
			if(vanishedTo != null){
				for(User u : getAllUsers()){
					if(vanishedTo.doesInherient(u.getRank())){
						if(u.player.isOnline()){
							Player pl = (Player)u.player;
							//if(!DuelManager.getMatchedPlayers().contains(pl.getUniqueId()))
								pl.hidePlayer(((Player)this.player));
						}
					}
				}
			}else{
				for(Player e: Bukkit.getOnlinePlayers()){
				//if(!DuelManager.getMatchedPlayers().contains(e.getUniqueId()))
					e.showPlayer((Player)this.player);
				}
			}
			
			return this;
		}*/
	 	
		
		/*public boolean hasPermission(Rank rank){
			
			return this.rank.doesInherient(rank) || this.getPlayer().isOp();
			
			
		}*/
		
		
	 	public UUID getPlayerUUID(){
			
			return id;
		
		}
	 		

	 	public Calendar getBanExpire(){
	 		
			return banExpire;
		}
		public User addBanExpire( int j){
	 		Calendar temp =Calendar.getInstance();
	 		temp.add(Calendar.SECOND, j);
			 banExpire = temp;
			 return this;
		}
	 	public User setBanExpire( Calendar j){
	 		
			 banExpire = j;
			 return this;
		
	 	}
	 	public User resetBanExpire(){
	 		
			 banExpire = Calendar.getInstance();
			 return this;
		}
	 	public User indefiniteBanExpire(){
	 		
			 banExpire = null;
			 return this;
		}
	 	
	 	
		public Calendar getMuteExpire(){
	 		
			return muteTime;
		}
		public User addMuteExpire( int j){
	 		Calendar temp =Calendar.getInstance();
	 		temp.add(Calendar.SECOND, j);
	 		muteTime = temp;
			 return this;
		}
	 	public User setMuteExpire( Calendar j){
	 		
	 		muteTime = j;
			 return this;
		
	 	}
	 	public User resetMuteExpire(){
	 		
	 		muteTime = Calendar.getInstance();
			 return this;
		}
	 	public User indefiniteMuteExpire(){
	 		
			 muteTime = null;
			 return this;
		}
	 	
		
	 	public String getBanMessage(){
	 		
			return banMessage;
		}
	 	
	 	
	 	public User setBanMessage( String msg){
	 		
			 banMessage = msg;
			 return this;
		}
	 	
	 	

	 

	 	
	 	
		public GameMode getGameMode(){
	 		
			return gameMode;
		}
	 	public User setGameMode( GameMode mode){
	 		 Player pl = (Player)player;
	 		//if(DuelManager.getMatchedPlayers().contains(pl.getUniqueId()))return this;
			 gameMode = mode;
			 pl.setHealth(20.0);
			 pl.setSaturation( 20.0f);
			 for(PotionEffect e:  pl.getActivePotionEffects()){
				 pl.removePotionEffect(e.getType());
			 }
		
			 pl.setFireTicks(0);
			
			 godmode = false;
			 frozen = false;
			// changeKit(null);
			// setVanishedTo(null);
			
			 switch(mode){
		
			 case BUILDER:
				 //Utils.debug("User: Giving Mode: "+ mode.name());
				 pl.setCanPickupItems(true);
				 pl.setAllowFlight(true);
				 pl.setGameMode(org.bukkit.GameMode.CREATIVE);
				 break;
			 case STAFF:
				// Utils.debug("User: Giving Mode: "+ mode.name());
				 pl.setCanPickupItems(true);
				 pl.setAllowFlight(true);
				 pl.setGameMode(org.bukkit.GameMode.CREATIVE);
				 break;
			 case PLAYER:
				//Utils.debug("User: Giving Mode: "+ mode.name());
				 pl.setAllowFlight(false);
				 pl.setCanPickupItems(true);
				 //Utils.cleansePlayer(pl);
				// pl.teleport(DataManager.getWarp("spawn").getLocation());
				 pl.setGameMode(org.bukkit.GameMode.SURVIVAL);
				// Utils.giveStartItems(pl);
				 break;
			 }
			 return this;
		}
	 	

	 	public Inventory getInv(){
	 		return inv;
	 	}
	 	public User setInv(Inventory inv){
	 		this.inv = inv;
	 		return this;
	 	}

	 	
	 	public Long getChatCooldown(){
			return chatCooldown;
		}
	 	public User setChatCooldown(Long cd){
			this.chatCooldown = cd;
	 		return this;
		}
	 		
		
		
		/*public Rank getRank(){
			return rank;
		}
		public User setRank(Rank rank){
			
			this.rank = rank;
			refreshVanish();
			 refreshListName();
			;
			return this;
		}*/
		
				
		public User setGui(GuiApi u){
			gui = u;
			return this;
		}
		public GuiApi getGui(){
			return gui;	
		}
		
		
		public void refreshListName(){
			if(player.isOnline()){
				Player pl = (Player)player;
				String listName = Utils.colorCode(getRankTitle());
				if(listName.length() >15){
					listName = listName.substring(0, 15);
				}
				pl.setPlayerListName(listName);
				//NametagAPI.setPrefix(player.getName(),Utils.colorCode( getRank().getPrefix()));
			
			}
		}
		
	
		public boolean isGodModed(){
			return (gameMode != GameMode.PLAYER || godmode );
		}
		public User setGodMode(boolean g){
			godmode =  g;
			return this;
		}
		
		
		public boolean isFrozen(){
			return frozen;
		}
		public User setFrozen(boolean g){
			frozen =  g;
			return this;
		}

	
		public Player getLastMsgReciever(){
			return lastMsgReciever;
		}
		public User setLastMsgReciever(Player pl){
			
			lastMsgReciever= pl;
			return this;
		}
		
		
		public NotifyMode getNotifyMode(){
			return nMode;
		}
		public User setNotifyMode( NotifyMode nm){
			nMode = nm;
			return this;
		}
		
		
		
		public User setCombatLog(int time){
				combatTimer = time;
				return this;
		}
		public int getCombatLog(){
			return combatTimer;
	}
		
		public User setArea(Area time){
			area = time;
			return this;
		}
		public Area getArea(){
			return area;
			
		}

		public void remove(){
			
			//DataManager.savePlayerUser(this);
			users.remove(player);
		}
	


		public User setInvSee(boolean is){
			isInvSee = is;
			return this;
		}
		
		public boolean isInvSee(){
			return isInvSee;
		}
		
		public void updateScoreBoard(){
			if(!player.isOnline()){
				return;
			}
		
			double money = 0;
			try{
				money = Economy.getMoney(player.getName());
			}catch(Exception e){
				
			}
			Mine mine  =  null;
			DecimalFormat df = new DecimalFormat("0.##%");
			boolean onGrid = false;
			if(getMinerRank()>=0){
				if(getMinerRank() < Mine.getRankedMines().size()){
					mine = Mine.getRankedMines().get(getMinerRank());
					onGrid = true;
				}
			}
			
			double per = 0f;
			per= (float) Math.min(1.0,money/Main.rankupcosts.get(getMinerRank()) );
				
			
				int toShow = (int) Math.floor(10*per);
				int empty = 10 - toShow;
				String bar = "";
				if(toShow != 0){
					for(int i = 0; i<toShow;i++){
						bar+="&c|";
					}
				}
				if(empty != 0){
					for(int i = 0; i<empty;i++){
						bar+="&7|";
					}
				}
				
				
				
				sb.setDisplayName("&7&lAria&c&lPrison");
			
				sbStats = scoreB.registerNewObjective("stats", "dummy");
				sbStats.setDisplayName(sb.getObj().getDisplayName());
				sbStats.setDisplaySlot(DisplaySlot.SIDEBAR);
				List<String> ent = new ArrayList<String>(sb.getBoard().getEntries());
				for(int i = 0; i< ent.size();i++){
					sbStats.getScore(ent.get(i)).setScore( ent.size()-i);
				}
		
				((Player)player).setScoreboard(scoreB);
	
				sb.reset();
			
				sb.setSlot(5, "&7Rank: &c"+ ( onGrid ? mine.getIcon():"None" ));
				
				sb.setSlot(4, "&7Sell Multiplier: &cx"+ Utils.formatNum(getTotalBuff()));
		
				sb.setSlot(3, "&7Money: &c"+Utils.formatCur(money));
				if(onGrid){
					sb.setSlot(2,"&7Rankup: "+"&c"+df.format(per));
					sb.setSlot(1,"&8&l["+bar+"&8&l]");
				}
				((Player)player).setScoreboard(sb.getBoard());
				sbStats.unregister();
						
			
			

		}
		public Set<UUID> getIgnoredPlayers(){
			return ignoredPlayers;
		}

		public ProducerLevel getProdLvL() {
			return prodLvL;
		}


		public void setProdLvL(ProducerLevel prodLvL) {
			this.prodLvL = prodLvL;
		}

	 	public void setMinerRank(int r){
	 		if(r<Main.maxRank){
	 			minerRank = r;
	 		}else{
	 			minerRank = Main.maxRank-1;
	 		}
	 	}
	 	public int getMinerRank(){
	 		return minerRank;
	 	}
/*	 
	 	public boolean canAutoSmelt(){
	 		return autosmelt;
	 		
	 	}
	 	public void setAutoSmelt(boolean on){
	 		autosmelt = on;
	 		
	 	}
	 	
	 	public boolean canAutoPack(){
	 		return autoPack;
	 	}
	 	public void setAutoPack(boolean on){
	 		autoPack = on;
	 	}
	*/	
	 	public void setAutoAction(AutoAction a,boolean b){
	 		autoActions.put(a, b);
	 	}
	 	public boolean getAutoAction(AutoAction a){
	 		return autoActions.get(a);
	 	}
	 	public void toggleAutoAction(AutoAction a){
	 		autoActions.put(a, !autoActions.get(a));
	 	}
	 	public 	Violations  getViolations (){
	 		return infs;
	 
	}
	 	public List<SellBuff> getBuffs(){
	 		return new ArrayList<SellBuff>(sellBuffs);
	 	}
	 	public void setBuffs(List<SellBuff> e){
	 		sellBuffs = new ArrayList<SellBuff>(e);
	 	}
	 	public void addBuff(SellBuff b){
	 		sellBuffs.add(b);
	 	}
	 	public void removeBuff(SellBuff b){
	 		sellBuffs.remove(b);
	 	}
	 	public void clearBuff(){
	 		sellBuffs.clear();
	 	}
	 	
	 	public float getTotalBuff(){
	 		float total = 1f;
	 		for(SellBuff sb : sellBuffs){
	 			total+=sb.getAmount();
	 		}
	 		return total;
	 	}
	}



