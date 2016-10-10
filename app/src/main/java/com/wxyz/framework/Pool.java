package com.wxyz.framework;

import java.util.ArrayList;
import java.util.List;
/**
 * <p>Pool类是一个对象池Game-Input-EventList</p>
 * <p>用于减少程序疯狂地创建事件对象的实例而伤害垃圾回收器</p>
 * <p>有了Pool，生成事件时直接拿出Pool类中的实例进行参数修改</p>
 * （而不是新建实例）
 * <p>使用完毕再放回Pool</p>
 */
public class Pool<T> {

	public interface PoolObjectFactory<T>{
		public T createObject();
	}
	
	private final List<T> freeObjects;
	private final PoolObjectFactory<T> factory;
	private final int maxSize;
	/**
	 * 使用Pool前需要新建一个PoolObjectFactory<T>实例并实现createObject方法
	 * @param factory PoolObjectFactory<T>实例
	 * @param maxSize Pool的最大容量
	 */
	public Pool(PoolObjectFactory<T> factory,int maxSize){
		this.factory=factory;
		this.maxSize=maxSize;
		this.freeObjects = new ArrayList<T>(maxSize);
	}
	/**
	 * 创建新对象（实际是拿出Pool中存的）
	 */
	public T newObject(){
		T object = null;
		if(freeObjects.size()==0){
			object = factory.createObject();
		}else{
			object = freeObjects.remove(freeObjects.size()-1);
		}
		return object;
	}
	/**
	 * 回收对象
	 * @param object：被回收的对象
	 */
	public void free(T object){
		if (freeObjects.size()<maxSize){
			freeObjects.add(object);
		}
	}
}
