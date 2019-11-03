package com.yeomryo.map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class LoadThread extends BukkitRunnable{
	private LinkedList<Build> list;
	private String name;
	private File dataFolder;
	public LoadThread(File dataFolder, String name) {
		this.dataFolder=dataFolder;
		this.name=name;
		this.list=new LinkedList<>();
	}
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		try{
			File f = new File(dataFolder, "");
			if(!f.exists())
				f.mkdirs();
			f = new File(dataFolder ,name+".yr");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String s=br.readLine();
			br.close();
			fr.close();

			list = new LinkedList<>();
			
			String[] arg = s.split("\\*");
			
			for(int i=0;i<(arg.length)/4;i++){
				Build b = new Build();
				b.setWorld(Bukkit.getWorld(arg[4*i]));
				b.setMaterial(Material.getMaterial(arg[4*i+1]));
				b.setData(Byte.parseByte(arg[4*i+2]));
				String[] a2 = arg[4*i+3].split("\\/");
				b.setX(Integer.parseInt(a2[0]));
				b.setY(Integer.parseInt(a2[1]));
				b.setZ(Integer.parseInt(a2[2]));
				list.add(b);
			}
/*
			for(int y=list.getFirst().getY();y<list.getLast().getY();y++){
				for(int x=list.getFirst().getX();x<list.getLast().getX();x++){
					for(int z=list.getFirst().getZ();z<list.getLast().getZ();z++){
						Block bb = list.getFirst().getWorld().getBlockAt(x, y, z);
						Map.setBlock(bb, Material.AIR, (byte)0);
					}
				}
			}*/
			Map.flow = false;
			LinkedList<Build> b2 = new LinkedList<>();
			for(Build b : list){
				Block bb = b.getWorld().getBlockAt(b.getX(), b.getY(), b.getZ());
				try{
					if(bb.getType() != b.getMaterial()){
						if(b.getMaterial().isSolid() && b.getMaterial() != Material.SAND && b.getMaterial() != Material.GRAVEL && b.getMaterial() != Material.ANVIL){
							bb.setType(b.getMaterial());
							bb.setData(b.getData());
						}else{
							b2.add(b);
						}
					}
					//Map.setBlock(bb, b.getMaterial(), b.getData());
				}catch(IllegalStateException e){
					bb.setType(b.getMaterial());
					bb.setData(b.getData());
					e.printStackTrace();
				}
			}
			
			for(Build b : b2){
				Block bb = b.getWorld().getBlockAt(b.getX(), b.getY(), b.getZ());
				try{
				if(bb.getType() != b.getMaterial()){
					bb.setType(b.getMaterial());
					bb.setData(b.getData());
				}
				}catch(IllegalStateException e){
					bb.setType(b.getMaterial());
					bb.setData(b.getData());
					e.printStackTrace();
				}
			}
			Map.flow = true;
			
		}catch(IOException e){
			
		}
	}
}
