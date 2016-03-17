package com.giants.hd.desktop.reports.jasper;
public class TestFactory
{
 
  public static java.util.Collection generateCollection()
    {
    java.util.Vector collection = new java.util.Vector();
    collection.add(new CustomerBean("Ted", 20) );
    collection.add(new CustomerBean("Jack", 34) );
    collection.add(new CustomerBean("Bob", 56) );
    collection.add(new CustomerBean("Alice",12) );
    collection.add(new CustomerBean("Robin",22) );
    collection.add(new CustomerBean("Peter",28) );
 
    return collection;
  }
}