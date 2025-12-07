package auth;

public class Events {
    public String name,location,date;
    public Events(String na,String loc,String dat){
        name = na;
        location = loc;
        date = dat;
    }
    
    public Events(){
    }
    
    public void setEvent(String na,String loc,String dat){
        name = na;
        location = loc;
        date = dat;
    }
}
