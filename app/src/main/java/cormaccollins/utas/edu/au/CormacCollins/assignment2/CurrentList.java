package cormaccollins.utas.edu.au.CormacCollins.assignment2;

public  class CurrentList {
    public static ListData list;
    public static void addList(ListData ls){
        list = ls;
    }
    public ListData getList(){
        return list;
    }
}
