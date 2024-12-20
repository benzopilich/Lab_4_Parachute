package org.example;

import org.example.api.Dto.ParachuteDTO;
import org.example.api.Factory.ParachuteFactory;
import org.example.api.Misc.Archiver;

import java.io.IOException;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var storage = new ParachuteFactory();
        Scanner scanner = new Scanner(System.in);


        storage.readFromFile("parachute.txt");
        storage.setListStorage(storage.readFromXml("parachute.xml"));
        storage.setListStorage(storage.readDataFromJsonFile("parachute.json"));
        System.out.println("Список парашютов получен.");
        for (ParachuteDTO dto : storage.getList()) {
            System.out.println(dto.toString());
        }
        System.out.println();
        int id=-1;
        String name="";
        String description="";
        boolean t=true;
        do {
            System.out.println("Введите данные о парашюте в формате cost,name,description:");
            try {
                String input = scanner.nextLine();
                String[] parts = input.split(",");
                id = Integer.parseInt(parts[0]);
                name = parts[1];
                description = parts[2];
                int finalId = id;
                String finalDescription = description;
                String finalName = name;
                if (storage.getList().stream().anyMatch(parachuteDTO -> parachuteDTO.getCost() == finalId) ||
                        storage.getList().stream().anyMatch(ParachuteDTO -> ParachuteDTO.getDescription().equals(finalDescription)) ||
                        storage.getList().stream().anyMatch(CategoryDto -> CategoryDto.getName().equals(finalName))
                ) {
                    System.out.println("Такой парашют уже получен!");
                    return;
                }
            }catch (Exception e){
                System.out.println("Попробуйте снова");
                t=false;
            }
        }while(t!=true);
        System.out.println(storage.getList());


        var newParachute = new ParachuteDTO(id, name, description);
        storage.addToListStorage(newParachute);
        storage.addToMapStorage(id, newParachute);

        storage.writeToFile("parachute.txt");
        storage.writeToXml("parachute.xml", storage.getList());
        storage.writeDataToJsonFile("parachute.json", storage.getList());

        System.out.println("Обновленный список парашютов" + storage.getList());
        boolean ans = false;

        do {
            System.out.println("Выберете поле для сортировки(cost,name,description):");
            String typeSort = scanner.nextLine();
            typeSort = typeSort.toLowerCase();


            switch (typeSort) {

                case "cost":
                    storage.getList().sort(Comparator.comparing(ParachuteDTO::getCost));
                    System.out.println("Парашюты сортированные по cost: ");
                    for (ParachuteDTO dto : storage.getList()) {
                        System.out.println(dto.toString());
                    }
                    ans = true;
                    break;

                case "name":
                    storage.getList().sort(Comparator.comparing(ParachuteDTO::getName));
                    System.out.println("Парашюты сортированные по названию: " + storage.getList());
                    ans = true;
                    break;

                case "description":
                    storage.getList().sort(Comparator.comparing(ParachuteDTO::getDescription));
                    System.out.println("Парашюты сортированные по описанию: " + storage.getList());
                    ans = true;
                    break;
                default:
                    System.out.println("Введено неверное поле");
                    break;
            }
        } while (!ans);


        String[] files = new String[]{
                "parachute.txt",
                "parachute.json",
                "parachute.xml"
        };

        Archiver archiver = new Archiver();
        try {
            archiver.createZipArchive("zipResult.zip", files);
            archiver.createJarArchive("jarResult.jar", files);
        }catch (IOException e)  {
            e.printStackTrace();
        }
    }
}

