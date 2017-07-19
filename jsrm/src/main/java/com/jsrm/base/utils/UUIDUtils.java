package com.jsrm.base.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public final class UUIDUtils
{
  private int unique;
  private long time;
  private static String address;
  private static int hostUnique = new Object().hashCode();
  private static Object mutex = new Object();
  private static long lastTime = System.currentTimeMillis();
  private static long DELAY = 10L;

  static
  {
    try
    {
      String s = InetAddress.getLocalHost().getHostAddress();

      address = MD5.selfMD5(s);
    } catch (UnknownHostException ex) {
      address = generateNoNetworkID();
    }
  }

  private UUIDUtils()
  {
    synchronized (mutex) {
      boolean done = false;
      while (!done) {
        this.time = System.currentTimeMillis();
        if (this.time < lastTime + DELAY) {
          try {
            Thread.sleep(DELAY);
          } catch (InterruptedException localInterruptedException) {
          }
        } else {
          lastTime = this.time;
          done = true;
        }
      }
      this.unique = hostUnique;
    }
  }

  public boolean equals(Object obj)
  {
    if ((obj != null) && ((obj instanceof UUIDUtils))) {
      UUIDUtils uuid = (UUIDUtils)obj;

      return (this.unique == uuid.unique) && (this.time == uuid.time) && 
        (address.equals(address));
    }
    return false;
  }

  public String toString()
  {
    return Integer.toString(this.unique, 16) + 
      Long.toString(this.time, 16) + address;
  }

  private static String generateNoNetworkID()
  {
    String nid = Thread.activeCount() + 
      System.getProperty(" os.version ") + 
      System.getProperty(" user.name ") + 
      System.getProperty(" java.version ");
    System.out.println(
      " netWorkId = " + nid);

    return MD5.selfMD5(nid);
  }

  public static final String getUUID()
  {
    UUIDUtils uid = new UUIDUtils();
    return uid.toString();
  }

  public static String createId_36()
  {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }

  public static String createId_32()
  {
    return createId_36().replaceAll("-", "");
  }

//  public static void main(String[] args)
//  {
////    String uuid = generateNoNetworkID();
////
////    System.out.println(uuid);
////    System.out.println(uuid.length());
////
////    System.out.println(" ########## ");
////
////    System.out.println(createId_36());
////
// //   System.out.println(" ########## ");
////
////    System.out.println(createId_32());
//  }
}