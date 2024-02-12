import java.time.LocalDate;
import java.util.*;

public class IO {

    // limpar o ecrâ
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    // Espera o utilizador para continuar o programa
    public static String pause(){
        System.out.println("\n\n\nENTER para continuar...");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    // menu
    public static int UserMenu(LocalDate day){
        IO.clearScreen();
        StringBuilder s = new StringBuilder("      === Menu ===\n\n");
        s.append(" DATA: ").append(day);
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n   | 1  |  Efetuar uma simulação                                           |");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n   | 2  |  Carregar estado                                                 |");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n   | 3  |  Salvar estado                                                   |");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n   | 4  |  Qual a casa que gastou mais gastou durante a simulação?         |");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n   | 5  |  Qual o comercializador com maior volume de faturação?           |");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n   | 6  |  Listar as faturas emitidas por um comercializador               |");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n   | 7  |  Maiores consumidores de energia durante um perı́odo a determinar |");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n   | 8  |  Sair                                                            |");
        s.append("\n   +-----------------------------------------------------------------------+");
        s.append("\n\n   »» Insira a opção: ");
        System.out.print(s.toString());
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

    // Pede a data
    public static LocalDate askSimulation() {
        System.out.print("\n\n > Insira a próxima data (YYYY-MM-DD): ");
        Scanner sc = new Scanner(System.in);
        String in = sc.nextLine();
        return LocalDate.parse(in);
    }

    // Pedir o nome de um Comercializador
    public static String askSupplier(){
        System.out.print("\n\n > Insira o nome do comercializador: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    // Pedir a Data Inicial e a Data Final
    public static LocalDate[] askDays(){
        LocalDate[] days = new LocalDate[2];
        Scanner sc = new Scanner(System.in);
        System.out.print("\n\n > Data Inicial (YYYY-MM-DD): ");
        days[0] = LocalDate.parse(sc.next());
        System.out.print("\n > Data Final (YYYY-MM-DD): ");
        days[1] = LocalDate.parse(sc.next());
        return days;
    }

    // Pedir o caminho para o ficheiro
    public static String askFilePath(){
        System.out.print("\n\n > Insira o caminho para o ficheiro: ");
        Scanner sc = new Scanner(System.in);
        return sc.next();
    }

    // Pedir o número de comercializadores
    public static Integer askNumber(){
        System.out.print("\n\n > Quantidade de consumidores: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

    // Devolve a casa que gastou/consumiu mais
    public static void houseMostSpentResult(SmartHouse sm, LocalDate date){
        IO.clearScreen();
        StringBuilder s = new StringBuilder("\n\n DATA: ").append(date);
        s.append("\n »» Casa que mais gastou: \n\n");
        s.append("-------------------------------------\n");
        s.append(sm.getOwner().toString());
        s.append("-------------------------------------\n");
        s.append("Comercializador: ").append(sm.getSupplier().getCompany()).append("\n");
        String value = String.format("%.3f $\n", sm.totalBilling() + sm.totalInstallationCost());
        s.append("Valor Gasto: ").append(value);
        s.append("-------------------------------------\n\n");
        System.out.print(s.toString());
        IO.pause();
    }

    // Devolve o fornecedor com maior volume de faturacao
    public static void highestBillingSupplierResult (String[] supplier, LocalDate date){
        IO.clearScreen();
        StringBuilder s = new StringBuilder("\n\n DATA: ").append(date);
        s.append("\n »» Comercializador com maior volume de faturação: \n\n");
        s.append("-------------------------------------\n");
        s.append("Nome: ").append(supplier[0]).append("\n");
        String value = String.format("%.3f $\n", Double.parseDouble(supplier[1]));
        s.append("Valor da faturação total: ").append(value);
        s.append("-------------------------------------\n\n");
        System.out.print(s.toString());
        IO.pause();
    }

    // Devolve todas as Faturas do fornecedor
    public static void allBillsFromSupplierResult(Supplier supplier,LocalDate date){
        IO.clearScreen();
        Set <Bill> bills = supplier.getBills();
        StringBuilder s = new StringBuilder("\n\n DATA: ").append(date);
        Iterator<Bill> iterator = bills.iterator();
        s.append("\n\n »» Lista das faturas do comercializador '").append(supplier.getCompany()).append("'  : \n\n");
        s.append("-----------------------------------------------------------------------------\n");
        s.append("Data Inicial - Data Final - Proprietário da Casa - Valor\n\n");
        while (iterator.hasNext()){
            Bill b = iterator.next();
            s.append(b.getStartDate()).append(" - ");
            s.append(b.getFinalDate()).append(" - ");
            String value = String.format("%.3f $", b.getValue());
            s.append(b.getOwner().getName()).append(" - ");
            s.append(value).append("\n");
        }
        s.append("-----------------------------------------------------------------------------\n\n");
        System.out.println(s.toString());
        IO.pause();
    }

    //Devolve o maior consumidor de energia
    public static void biggestenergyConsumersResult(List<SmartHouse> list, LocalDate day, LocalDate[] days , int n){
        IO.clearScreen();
        Iterator<SmartHouse> iterator = list.iterator();
        StringBuilder s = new StringBuilder("\n\n DATA: ").append(day);
        s.append("\n\n »» Top ").append(n).append(" maiores consumidores de energia de ").append(days[0]).append(" a ").append(days[1]).append(" :\n\n");
        s.append("-----------------------------------------------------------------------------\n\n\n");
        for(int i=0; i<n && iterator.hasNext(); i++){
            SmartHouse pin = iterator.next();

            String value = String.format("%.3f $\n", pin.totalBilling()+pin.totalInstallationCost());
            s.append(i+1).append("º :  ").append(pin.getOwner().getName()).append(" - ");
            s.append(pin.getSupplier().getCompany()).append(" - ").append(value);
        }
        s.append("-----------------------------------------------------------------------------\n\n");
        System.out.print(s.toString());
        IO.pause();
    }

}
