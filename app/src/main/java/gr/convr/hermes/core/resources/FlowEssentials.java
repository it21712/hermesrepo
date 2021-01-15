package gr.convr.hermes.core.resources;

public class FlowEssentials {

    private static boolean cardExists = false;

    public static void setCardExists(boolean exists){cardExists = exists;}
    public static boolean getCardExists(){return cardExists;}
}
