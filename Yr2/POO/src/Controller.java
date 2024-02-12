import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Controller {
    //metodo que gere o programa de acordo com o input do utilizador
    public static void engine() throws IOException, ClassNotFoundException, ParsingException, EntityMissingException {

        State state = new State();
        SmartCity smartCity = state.parser();


        LocalDate newDay;
        int option;
        while(true){
            IO.clearScreen();
            option = IO.UserMenu(smartCity.getDate());
            while(option < 1 || option > 9){
                option = IO.UserMenu(smartCity.getDate());
            }

            switch (option){
                case 1: newDay = IO.askSimulation();
                        while(newDay.isBefore(smartCity.getDate()) || newDay.equals(smartCity.getDate())) {
                            System.out.println("Data inválida!");
                            newDay = IO.askSimulation();
                        }
                        smartCity.moveTime(smartCity.getDate(),newDay);
                        smartCity.setDate(newDay);
                        break;
                case 2: String path = IO.askFilePath();
                        smartCity = smartCity.loadState(path);
                        break;
                case 3: smartCity.saveState();
                        break;
                case 4: SmartHouse s = smartCity.houseMostSpentInSimulation();
                        IO.houseMostSpentResult(s,smartCity.getDate());
                        break;
                case 5: String[] supplier = smartCity.higherTotalBillingSupplier();
                        IO.highestBillingSupplierResult(supplier,smartCity.getDate());
                        break;
                case 6: String supp = IO.askSupplier();
                        Supplier sup=smartCity.findSupplier(supp);
                        IO.allBillsFromSupplierResult(sup,smartCity.getDate());
                        break;
                case 7: LocalDate[] days = IO.askDays();
                        int n = IO.askNumber();
                        List<SmartHouse> list = smartCity.biggestEnergyConsumers(days[0],days[1]);
                        IO.biggestenergyConsumersResult(list,smartCity.getDate(),days,n);
                        break;
                default:IO.clearScreen();
                        System.exit(0);
                        break;
            }
        }
    }
}
