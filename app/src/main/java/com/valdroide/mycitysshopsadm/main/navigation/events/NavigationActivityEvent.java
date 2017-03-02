package com.valdroide.mycitysshopsadm.main.navigation.events;

public class NavigationActivityEvent {
    private int type;
    private String error;
    public static final int GETLISTCATEGORY = 0;
    public static final int SAVECATEGORY = 1;
    public static final int UPDATECATEGORY = 2;
    public static final int DELETECATEGORY = 3;
    public static final int ERROR = 4;


}
