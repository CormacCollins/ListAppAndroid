package cormaccollins.utas.edu.au.CormacCollins.assignment2;

public  class CurrentList {
    public static ListData list;
    public static boolean hasList;
    public static void addList(ListData ls){
        list = ls;
        hasList = true;
    }

    public static void removeList(){
        list = null;
        hasList = false;
    }

    public static boolean hasList(){
        return hasList;
    }
}
