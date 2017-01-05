package at.barbot.barbot.BluetoothTestClass;

/**
 * Created by philipp on 05.01.2017.
 */

public class BluetoothTest {
    static SlaveUnit slave1 = new SlaveUnit(1,"Tequila");
    static SlaveUnit slave2 = new SlaveUnit(2,"Orange Juice");
    static SlaveUnit slave3 = new SlaveUnit(3,"Pineapple Juice");

    static SlaveUnit[] slaveUnits =  {slave1,slave2,slave3};


    public static void main(String[] args) {
        //Test
        bluetooth("G;1:20;2:30;3:60");
        bluetooth("S;");
        bluetooth("NS;");
        bluetooth("DS;");
    }

    public static String send(String message){
        return message;
    }

    public static void makeDrink(String[] ingridientList){
        //wenn fertig
        send("G;finish");
    }

    public static void bluetooth(String input){
        String[] drinkList;
        drinkList = input.split(";");

        if(drinkList[0].contentEquals("G")){
            if (drinkList.length-1 <= slaveUnits.length){
                send("G;ok");
                makeDrink(drinkList);
            }else if (drinkList.length-1 > slaveUnits.length){
                send("G;tooLong");
            }
        }else if (drinkList[0].contentEquals("S")){
            int numberOfSlaves = slaveUnits.length;
            send("S;"+numberOfSlaves);
        }else if (drinkList[0].contentEquals("NS")){
            int numberOfSlaves = slaveUnits.length; // gehört eigentlich die nummer der angestecken einheit
            send("NS;"+numberOfSlaves);
        }else if (drinkList[0].contentEquals("DS")){
            int numberOfSlaves = slaveUnits.length;
            send("DS;"+numberOfSlaves); // gehört die nummber die abgesteckt wurde
        }
    }
}
