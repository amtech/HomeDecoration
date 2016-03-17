package com.giants.hd.desktop.reports.jasper;
public class CustomerBean
{
  private String name = "";
  private int age = 0;
 
  public CustomerBean(String name, int age)
  {
    this.name = name;
    this.age = age;
  }
  public int getAge()
  {
    return age;
  }
 
  public String getName()
  {
    return name;
  }
}