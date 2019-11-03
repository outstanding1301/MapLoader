package com.yeomryo.map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class Map extends JavaPlugin{

	public static Map plugin;
	public static HashMap<Player, Location> p1 = new HashMap<>();
	public static HashMap<Player, Location> p2 = new HashMap<>();
	public static String pf = "§6≪ §fYMap §6≫ ";
	public static boolean flow=true;
	
	
	@Override
	public void onEnable() {
		plugin = this;
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("[ YMap ] 제작 : 염료 (yeomryo@naver.com)");
		System.out.println("[ YMap ] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.println("[ YMap ] 플러그인이 활성화 되었습니다.");
		System.out.println("[ YMap ] 본 플러그인의 저작권은 모두 염료에게 있습니다.");
		System.out.println(" ");
		System.out.println("[ YMap ] 맵 정보를 불러왔습니다.");
		System.out.println("[ YMap ] 등록된 맵 : "+mapList().size());
		System.out.println(" ");
		System.out.println("[ YMap ] 기본 라이센스 - 상업적 이용, 배포가 불가능합니다.");
		System.out.println(" ");
		System.out.println(" ");
		
		getServer().getPluginManager().registerEvents(new Listener() {
			
		@EventHandler(priority = EventPriority.HIGHEST)
		public void onBlockPhysics(BlockPhysicsEvent e) {
			if(!flow)
				e.setCancelled(true);
		}
			 
		@EventHandler(priority = EventPriority.HIGHEST)
		public void onBlockFromTo(BlockFromToEvent e) {
			if(!flow)
				e.setCancelled(true);
			
		}
		
		}, this);
	}
	
	@Override
	public void onDisable() {
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("[ YMap ] 제작 : 염료 (yeomryo@naver.com)");
		System.out.println("[ YMap ] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.println("[ YMap ] 플러그인이 비활성화 되었습니다.");
		System.out.println("[ YMap ] 본 플러그인의 저작권은 모두 염료에게 있습니다.");
		System.out.println(" ");
		System.out.println("[ YMap ] 기본 라이센스 - 상업적 이용, 배포가 불가능합니다.");
		System.out.println(" ");
		System.out.println(" ");
	}
	public static void setBlock(Block b, Material m, byte data){
		new BukkitRunnable() {
			
			@Override
			public void run() {
				b.setType(m);
				b.setData(data);
			}
		}.runTask(plugin);
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("맵") || label.equalsIgnoreCase("ymap")){
			if(!sender.isOp()){
				sender.sendMessage(pf+"§c관리자 전용 명령어입니다.");
				return true;
			}
			if(args.length == 0){
				sender.sendMessage(pf+"/맵 pos1");
				sender.sendMessage(pf+"/맵 pos2");
				sender.sendMessage(pf+"/맵 저장 [이름]");
				sender.sendMessage(pf+"/맵 로드 [이름]");
				sender.sendMessage(pf+"/맵 목록");
				return true;
			}
			if(args[0].equalsIgnoreCase("pos1")){
				if(sender instanceof Player){
					Player p = (Player)sender;
					p1.put(p, p.getLocation());
					p.sendMessage(pf+"§a좌표1 - X:"+p.getLocation().getBlockX()+" Y:"+p.getLocation().getBlockY()+" Z:"+p.getLocation().getBlockZ());
					return true;
				}
			}else if(args[0].equalsIgnoreCase("pos2")){
				if(sender instanceof Player){
					Player p = (Player)sender;
					p2.put(p, p.getLocation());
					p.sendMessage(pf+"§a좌표2 - X:"+p.getLocation().getBlockX()+" Y:"+p.getLocation().getBlockY()+" Z:"+p.getLocation().getBlockZ());
					return true;
				}
			}else if(args[0].equalsIgnoreCase("저장") || args[0].equalsIgnoreCase("save")){
				if(sender instanceof Player){
					Player p = (Player)sender;
					if(args.length==2){
						String name = args[1];
						LinkedList<Build> blocks = save(p);
						if(blocks != null){
							p.sendMessage(pf+"§a맵 "+name+"을(를) 저장합니다.");
							saveBuild(name, blocks);
						}
					}else{
						sender.sendMessage(pf+"/맵 저장 [이름]");
						return true;
					}
				}
			}else if(args[0].equalsIgnoreCase("로드") || args[0].equalsIgnoreCase("load")){
				if(args.length==2){
					String name = args[1];
					if(!mapList().contains(name)){
						sender.sendMessage(pf+"존재하지 않는 맵입니다. /맵 목록 을 확인하세요");
						return true;
					}
					loadBuild(name);
					sender.sendMessage(pf+"맵 "+name+"을(를) 불러옵니다.");
				}else{
					sender.sendMessage(pf+"/맵 로드 [이름]");
					return true;
				}
			}else if(args[0].equalsIgnoreCase("목록") || args[0].equalsIgnoreCase("list")){
				sender.sendMessage(pf+"<---맵 목록--->");
				for(String s : mapList()){
					sender.sendMessage(pf+s);
				}
			}
			return true;
		}
		return false;
	}
	public List<String> mapList(){
		List<String> list = new ArrayList<>();
		File dir = new File(getDataFolder(), "");
		if(!dir.exists())
			dir.mkdirs();
		File[] fl = dir.listFiles();
		for(File f : fl){
			list.add(f.getName().replaceAll(".yr",""));
		}
		return list;
	}
	
	public File getFile(String name){

		File f = new File(getDataFolder(), "");
		if(!f.exists())
			f.mkdirs();
		f = new File(getDataFolder() ,name+".yr");
		
		return f;
	}
	
	@SuppressWarnings("deprecation")
	public LinkedList<Build> save(Player p){
		Location l1 = p1.get(p);
		Location l2 = p2.get(p);

		if(!p1.containsKey(p)){
			p.sendMessage(pf+"§e좌표1이 설정되어있지 않습니다.");
			return null;
		}
		if(!p2.containsKey(p)){
			p.sendMessage(pf+"§e좌표2가 설정되어있지 않습니다.");
			return null;
		}
		
		LinkedList<Build> blocks = new LinkedList<>();
		for(int y=Math.min(l1.getBlockY(), l2.getBlockY());y<=Math.max(l1.getBlockY(), l2.getBlockY());y++){
			for(int x=Math.min(l1.getBlockX(), l2.getBlockX());x<=Math.max(l1.getBlockX(), l2.getBlockX());x++){
				for(int z=Math.min(l1.getBlockZ(), l2.getBlockZ());z<=Math.max(l1.getBlockZ(), l2.getBlockZ());z++){
					Block bb = p.getWorld().getBlockAt(x,y,z);
					Build b = new Build();
					b.setWorld(p.getWorld());
					b.setX(x);
					b.setY(y);
					b.setZ(z);
					b.setData(bb.getData());
					b.setMaterial(bb.getType());
					blocks.add(b);
				}
			}
		}
		return blocks;
	}
	
	public void saveBuild(String name, LinkedList<Build> list){
		SaveThread t = new SaveThread(getDataFolder(), name, list);
		t.start();
	}
	
	
	public void loadBuild(String name){
		LoadThread t = new LoadThread(getDataFolder(), name);
		t.runTask(this);
	}
}
