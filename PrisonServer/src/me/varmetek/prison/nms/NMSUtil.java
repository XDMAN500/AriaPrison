package me.varmetek.prison.nms;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
public class NMSUtil {

	public static final Class<?> packetTitle = getClass("org.spigotmc.ProtocolInjector$PacketTitle");
	public static final Class<?> packetActions= getClass("org.spigotmc.ProtocolInjector$PacketTitle$Action");
//	public  Class<?> nmsChatSerializer= getNMSClass("ChatSerializer");
	private boolean ticks = false;
	private static final Map<Class<?>, Class<?>> CORRESPONDING_TYPES = new HashMap();
	  
	/*  public NMSUtil()
	    throws ClassNotFoundException
	  {
	    loadClasses();
	  }*/
	  
	
	  
	 /* private void loadClasses()
	    throws ClassNotFoundException
	  {
	    this.packetTitle = getClass("org.spigotmc.ProtocolInjector$PacketTitle");
	    this.packetActions = getClass("org.spigotmc.ProtocolInjector$PacketTitle$Action");
	    this.nmsChatSerializer = getNMSClass("ChatSerializer");
	  }*/
	  
	  
	public static int getProtocolVersion(Player player)
	    throws Exception
	  {
	    Object handle = getHandle(player);
	    Object connection = getField(handle.getClass(), "playerConnection").get(handle);
	    Object networkManager = getValue("networkManager", connection);
	    Integer version = (Integer)getMethod("getVersion", networkManager.getClass(), new Class[0]).invoke(networkManager, new Object[0]);
	    return version.intValue();
	  }
	  
	  public static  boolean isSpigot()
	  {
	    return Bukkit.getVersion().contains("Spigot");
	  }
	  
	  public static  Class<?> getClass(String namespace)
	  {
	    try
	    {
	      return Class.forName(namespace);
	    }
	    catch (Exception localException) {}
	    return null;
	  }
	  
	  public static  Field getField(String name, Class<?> clazz)
	    throws Exception
	  {
	    return clazz.getDeclaredField(name);
	  }
	  
	  public static  Object getValue(String name, Object obj)
	    throws Exception
	  {
	    Field f = getField(name, obj.getClass());
	    f.setAccessible(true);
	    return f.get(obj);
	  }
	  
	  public static  Class<?> getPrimitiveType(Class<?> clazz)
	  {
	    return CORRESPONDING_TYPES.containsKey(clazz) ? (Class)CORRESPONDING_TYPES.get(clazz) : clazz;
	  }
	  
	  public static  Class<?>[] toPrimitiveTypeArray(Class<?>[] classes)
	  {
	    int a = classes != null ? classes.length : 0;
	    Class[] types = new Class[a];
	    for (int i = 0; i < a; i++) {
	      types[i] = getPrimitiveType(classes[i]);
	    }
	    return types;
	  }
	  
	  public static  boolean equalsTypeArray(Class<?>[] a, Class<?>[] o)
	  {
	    if (a.length != o.length) {
	      return false;
	    }
	    for (int i = 0; i < a.length; i++) {
	      if ((!a[i].equals(o[i])) && (!a[i].isAssignableFrom(o[i]))) {
	        return false;
	      }
	    }
	    return true;
	  }
	  
	  public static  Object getHandle(Object obj)
	  {
	    try
	    {
	      return getMethod("getHandle", obj.getClass(), new Class[0]).invoke(obj, new Object[0]);
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	    return null;
	  }
	  
	  public static  Method getMethod(String name, Class<?> clazz, Class<?>... paramTypes)
	  {
	    Class[] t = toPrimitiveTypeArray(paramTypes);
	    Method[] arrayOfMethod;
	    int j = (arrayOfMethod = clazz.getMethods()).length;
	    for (int i = 0; i < j; i++)
	    {
	      Method m = arrayOfMethod[i];
	      Class[] types = toPrimitiveTypeArray(m.getParameterTypes());
	      if ((m.getName().equals(name)) && (equalsTypeArray(types, t))) {
	        return m;
	      }
	    }
	    return null;
	  }
	  
	  public static   String getVersion()
	  {
	    String name = Bukkit.getServer().getClass().getPackage().getName();
	    String version = name.substring(name.lastIndexOf('.') + 1) + ".";
	    return version;
	  }
	  
	  public static  Class<?> getNMSClass(String className)
	    throws ClassNotFoundException
	  {
	    String fullName = "net.minecraft.server." + getVersion() + className;
	    Class<?> clazz = null;
	    clazz = Class.forName(fullName);
	    return clazz;
	  }
	  
	  public static  Field getField(Class<?> clazz, String name)
	  {
	    try
	    {
	      Field field = clazz.getDeclaredField(name);
	      field.setAccessible(true);
	      return field;
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	    return null;
	  }
	  
	  public static   Method getMethod(Class<?> clazz, String name, Class<?>... args)
	  {
	    Method[] arrayOfMethod;
	    int j = (arrayOfMethod = clazz.getMethods()).length;
	    for (int i = 0; i < j; i++)
	    {
	      Method m = arrayOfMethod[i];
	      if ((m.getName().equals(name)) && ((args.length == 0) || (ClassListEqual(args, m.getParameterTypes()))))
	      {
	        m.setAccessible(true);
	        return m;
	      }
	    }
	    return null;
	  }
	  
	  public static  boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2)
	  {
	    boolean equal = true;
	    if (l1.length != l2.length) {
	      return false;
	    }
	    for (int i = 0; i < l1.length; i++) {
	      if (l1[i] != l2[i])
	      {
	        equal = false;
	        break;
	      }
	    }
	    return equal;
	  }
	

}
