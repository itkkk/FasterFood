package it.uniba.di.ivu.sms16.gruppo3.fasterfood;


public class AppConfiguration {
    private static Boolean LOGGED;
    private static String user;

    public static Boolean isLogged() {
        return LOGGED;
    }

    public static void setLogged(boolean status){
       LOGGED = status;
    }

    public static String getUser(){
        return user;
    }

    public static void setUser(String name){
        user = name;
    }

}
