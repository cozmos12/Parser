import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Dosya adını girin: ");
        String dosyaYolu = scanner.nextLine();
        boolean result = true;

        try (BufferedReader br = new BufferedReader(new FileReader(dosyaYolu))) {
            String satir;
            int satirNumarasi = 0;

            while ((satir = br.readLine()) != null) {

                satirNumarasi++;

                List<String> parcalar3=new ArrayList<String>();

                String[] parcalar = satir.trim().split("\\s+",2);
                if(parcalar.length>1){
                    String[] parcalar2=parcalar[1].trim().split(",");
                    if(parcalar2.length>1){
                        parcalar3.add(parcalar[0]);
                        parcalar3.add(parcalar2[0]);
                        parcalar3.add(parcalar2[1]);
                    }else{
                        parcalar3.add(parcalar[0]);
                        parcalar3.add(parcalar[1]);
                    }
                }else{
                    System.out.println("Hata: Satır " + satirNumarasi + " - Geçersiz komut: "+parcalar[0]+" "+"komut ile operantlar arasında boşluk bulunmalıdır");
                    continue;
                }

                if (!validateCommand(parcalar3.get(0))) {
                    printError(satirNumarasi,"Geçersiz Satır"+ String.join(" ", parcalar3)+" :: Hatalı Komut:"+parcalar3.get(0));
                    result = false;
                }

                if (!validateOperands(parcalar3)) {
                    printError(satirNumarasi, "Geçersiz Satır: " + String.join(" ", parcalar3) + " :: Hatalı operant: \"" + getInvalidOperand(parcalar3) + "\"");
                        result = false;
                }

                if (!validateCommandAndOperandsCompatibility(parcalar3.get(0), parcalar3)) {
                    printError(satirNumarasi, "Geçersiz Satır: " + String.join(" ", parcalar3) + " :: Komut Ve Operant Uyumsuz : ");

                    result = false;
                }

            }

            if (result) {
                System.out.println("Txt dosyasında Hata bulunamadı");
            } else {
                System.out.println("Txt dosyasında yazım hatası vardır");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean validateCommand(String komut) {

        String[] dogruKomutlar = {"TOP", "CRP", "BOL", "CIK", "D", "DB", "DK", "DKE", "DBE", "DED", "DE", "HRK", "VE", "VEY", "DEG", "OKU", "YAZ"};

        for (String validCommand : dogruKomutlar) {
            if (validCommand.equals(komut)) {
                return true;
            }
        }
        return false;
    }

    private static boolean validateOperands(List<String> operandlar) {
        for (int i=1;i<operandlar.size();i++) {
            if (!operandDefinedCheck(operandlar.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static String getInvalidOperand(List<String> operands) {
        for (String operand : operands.subList(1, operands.size())) {
            if (!operandDefinedCheck(operand)) {
                return operand;
            }
        }
        return "";
    }

    private static boolean operandDefinedCheck(String operand) {
        if (operand.isEmpty()) {
            return false;
        }
        if (operand.matches("ETIKET[1-9]|ETIKET10")) {
            return true;
        }
        if (operand.matches("AX|BX|CX|DX|\\d+")) {
            return true;
        }

        return false;
    }

    private static boolean validateCommandAndOperandsCompatibility(String komut, List<String> operandlar) {
        switch (komut) {
            case "TOP":
            case "BOL":
            case "CRP":
            case "CIK":
            case "VE":
            case "VEY":
            case "HRK":
                return operandlar.size() == 3&& operandlar.get(1).matches("AX|BX|CX|DX|\\d+")&& operandlar.get(2).matches("AX|BX|CX|DX|\\d+");
            case "D":
            case "DB":
            case "DK":
            case "DKE":
            case "DBE":
            case "DED":
            case "DE":
                return operandlar.size() == 2 && operandlar.get(1).matches("ETIKET[1-9]|ETIKET10");
            case "DEG":
            case "OKU":
            case "YAZ":
                return operandlar.size() == 2&& operandlar.get(1).matches("AX|BX|CX|DX|\\d+");
            default:
                return false;
        }
    }

    private static void printError(int lineNumber, String errorMessage) {
        System.out.println("Hata: Satır " + lineNumber + " - " + errorMessage);
    }
}
