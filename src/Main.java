import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static final String PATH_GAME_SAVE = "E://Java_Game//saves";

    public static void main(String[] args) {

        GameProgress gp1 = new GameProgress(100, 250, 1, 10.5);
        GameProgress gp2 = new GameProgress(84, 213, 2, 99.3);
        GameProgress gp3 = new GameProgress(15, 14, 5, 775.5);

        saveGame(PATH_GAME_SAVE + "//save1.dat", gp1);
        saveGame(PATH_GAME_SAVE + "//save2.dat", gp2);
        saveGame(PATH_GAME_SAVE + "//save3.dat", gp3);

        List<String> listFiles = new ArrayList<>();
        listFiles.add("//save1.dat");
        listFiles.add("//save2.dat");
        listFiles.add("//save3.dat");

        zipSave(PATH_GAME_SAVE + "//archive.zip", listFiles);

        deleteSaves(listFiles);

        openZip(PATH_GAME_SAVE + "//archive.zip", PATH_GAME_SAVE);

        openProgress(PATH_GAME_SAVE + "//save1.dat");
    }

    public static void saveGame(String savePath, GameProgress gp) {
        try (FileOutputStream fos = new FileOutputStream(savePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gp);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void zipSave(String zipPath, List<String> files) {

        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (String file : files) {
                FileInputStream fis = new FileInputStream(PATH_GAME_SAVE + file);
                ZipEntry entry = new ZipEntry(file);
                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();
                fis.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void deleteSaves(List<String> fileName) {
        for (String s : fileName) {
            File file = new File(PATH_GAME_SAVE + s);
            if (file.delete()) {
                System.out.println("Файл " + file.getName() + " удалён");
            } else if (file.exists()) {
                System.out.println("файл " + file.getName() + " на месте");
            } else
                System.out.println("Файл " + file.getName() + " не удалён!");
        }
    }

    public static void openZip(String zipPathDir, String unZipPathDir) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPathDir))) {
            ZipEntry entry;
            String name;

            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(unZipPathDir + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
                fout.close();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void openProgress(String pathSaveGame) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(pathSaveGame);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(gameProgress);
    }
}