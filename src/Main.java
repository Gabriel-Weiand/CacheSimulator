import java.io.FileInputStream;
import java.io.IOException;
import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;
//input: cache_simulator <nsets> <bsize> <assoc> <substituição> <flag_saida> arquivo_de_entrada
public class Main {
    public static void main(String[] args) {
        try (FileInputStream fis = new FileInputStream(args[5])) {
            LinkedList<String> list = new LinkedList<>();
            int byteRead;
            int byteCount= 0;
            String nova="";
            while ((byteRead = fis.read()) != -1) {//le todos arquivos
                nova=nova+ to8bitString(Integer.toBinaryString(byteRead));
                byteCount++;
                if(byteCount==4){//junta 4 bytes e separa do resto do input
                    System.out.println(nova);
                    byteCount=0;
                    list.add(nova);
                    nova="";
                }
            }
            System.out.println("Número de sets: "+args[0]);//args são os comandos digitados depois do java Main
            System.out.println("Tamanho do bloco : "+args[1]);
            System.out.println("Associatividade: "+args[2]);
            System.out.println("Substituição: "+args[3]);
            System.out.println("Flag: "+args[4]);
            System.out.println("Arquivo: "+args[5]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void calcularEndereco (){

    }
    public static String to8bitString(String bits){
        while(bits.length()<8){
            bits="0"+bits;
        }
        return bits;
    }//funcao que transforma os n bits do byte em 8
} 