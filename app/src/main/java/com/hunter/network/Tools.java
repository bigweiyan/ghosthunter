package com.hunter.network;

import java.util.ArrayList;

import com.hunter.game.models.Item;
import com.hunter.game.models.RoomRule;
import com.hunter.game.models.Signal;

public class Tools {
	public static String oneSignalToString(Signal signal){
		if(signal==null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append(signal.latitude).append(',').append(signal.longitude).append(',').append(signal.frequency);
		return sb.toString();
	}

	public static String signalToString(ArrayList<Signal> signal){
		if(signal==null)
			return null;

		StringBuilder sb = new StringBuilder();

		for(Signal i:signal){
			sb.append(oneSignalToString(i)).append(';');
		}
		if(sb.length() > 0) sb.delete(sb.length()-1, sb.length());
		return sb.toString();
	}

	public static ArrayList<Signal> stringToSignalList(String string){
		try{
			ArrayList<Signal> signal = new ArrayList<>();
			String[] list = string.split(";");
			for (String i : list){
				String[] temp = i.split(",");
				Signal onesignal = new Signal(Double.parseDouble(temp[0]), Double.parseDouble(temp[1]), Integer.parseInt(temp[2]));
				signal.add(onesignal);
			}
			return signal;
		}catch (Exception e){
			return null;
		}
	}
	
	public static String itemToString(Item item){
		StringBuilder sb = new StringBuilder();
		sb.append(item.getItemType()).append(' ');
		sb.append(item.getRemainTime());
		return sb.toString();
	}
	
	public static Item stringToItem(String s){
		if(s==null||s.equals(""))
			return null;
		String[] lst = s.split(" ");
		Item item = new Item(Integer.parseInt(lst[0]));
		item.setRemainTime(Float.parseFloat(lst[1]));
		return item;
	}
	
	public static ArrayList<Item> stringToItems(String s){
		if(s==null||s.equals(""))
			return new ArrayList<Item>();
		s = s.trim();
		String[] lst = s.split("\n");
		ArrayList<Item> result = new ArrayList<Item>();
		for (int i = 0; i < lst.length; i++){
			result.add(stringToItem(lst[i]));
		}
		return result;
	}
	
	public static String roomRuleToString(RoomRule rule){
		StringBuilder sb = new StringBuilder();
		sb.append(rule.mode).append(' ').append(rule.useItem).append(' ').append(rule.autoReady).append(' ');
		sb.append(signalToString(rule.signals));
		return sb.toString();
	}
	
	public static RoomRule stringToRoomRule(String s){
		s = s.trim();
		String[] lst = s.split(" ");
		int mode = Integer.parseInt(lst[0]);
		boolean useItem = Boolean.parseBoolean(lst[1]);
		boolean autoReady = Boolean.parseBoolean(lst[2]);
		RoomRule rule = new RoomRule(useItem, autoReady, mode);
		ArrayList<Signal> signals = stringToSignalList(lst[3]);
		rule.signals = signals;
		return rule;
	}

}
