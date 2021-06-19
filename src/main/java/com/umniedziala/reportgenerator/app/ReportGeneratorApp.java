package com.umniedziala.reportgenerator.app;

import com.umniedziala.reportgenerator.report.IReport;
import com.umniedziala.reportgenerator.report.Report1;
import lombok.val;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ReportGeneratorApp {

  private static final String REPORT_1 = "1 - Raport przepracowanych godzin dla pracowników";
  private static final String REPORT_2 = "2 - Raport kosztowności czasowej projektów";
  private static final String REPORT_3 = "3 - Szczegółowy wykaz pracy każdego pracownika";
  private static final String EXIT = "0 - Wyjście";
  private IReport report;

  public static void main(String[] args) {
    val availableOptions = Arrays.asList(REPORT_1, REPORT_2, REPORT_3, EXIT);
    Scanner scanner = new Scanner(System.in);
    ReportGeneratorApp reportGeneratorApp = new ReportGeneratorApp();
    System.out.println("RAPORT GENERATOR \n");
    System.out.println("Podaj ścieżkę do folderu zawierającego roczne zestawienia pracowników");
    val absolutePath = reportGeneratorApp.choosePath(new JFileChooser(), scanner);
    if (absolutePath == null) return;
    System.out.println("Wybrano ścieżkę: " + absolutePath);

    // TODO: pass folder name to DataLoader
    // IF loaded

    reportGeneratorApp.showMenu(availableOptions, scanner);
    scanner.close();
  }

  private boolean generateRaport1() {
    report = new Report1();
    report.generateReport();

    return true;
  }

  private boolean generateRaport2() {

    return false;
  }

  private boolean generateRaport3() {

    return false;
  }

  private String choosePath(JFileChooser chooser, Scanner scanner) {
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int retval = chooser.showDialog(null, "Select");
    if (retval == 1) {
      System.out.println("Nie można przejść dalej bez podania ścieżki\n"+
          "Chcesz wybrać jeszcze raz? [t-tak, n-nie]");
      var tryAgain = scanner.next().toLowerCase();
      while (!tryAgain.equals("n") && !tryAgain.equals("t")) {
        System.out.println("Wpisano błędną komendę, wpisz jescze raz");
        tryAgain = scanner.next().toLowerCase();
      }
      if (tryAgain.equals("n")) return null;
      choosePath(chooser, scanner);
    }
    return chooser.getSelectedFile().getAbsolutePath();
  }

  private void showMenu(List<String> availableOptions, Scanner scanner) {
    boolean finishWork = false;
    int action;
    while (!finishWork) {
      System.out.println("\nDostępne akcje:");
      availableOptions.forEach(System.out::println);
      System.out.print("Wybierz akcje: ");
      action = scanner.nextInt();
      switch (action) {
        case 1:
          generateRaport1();
          break;
        case 2:
          generateRaport2();
          break;
        case 3:
          generateRaport3();
          break;
        case 0:
          finishWork = true;
          break;
        default:
          System.out.println("Wybrana akcja nie inieje");
      }
    }
  }
}
