package Source;

import GUI.GUIWindow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BattleRecord {

    /* -Properties- */

    //inner class
    public class CreatureRecord{
        int num;
        int pos_x;
        int pos_y;
        public CreatureRecord(int num, int pos_x,int pos_y){
            this.num = num;
            this.pos_x = pos_x;
            this.pos_y = pos_y;
        }
    }
    public class RoundRecord{
        int rounder;
        CreatureRecord[] records = new CreatureRecord[17];
        public RoundRecord(int i){
            rounder = i;
            for(int j = 0; j < 7; j++){
                records[j] = new CreatureRecord(j,Global.calabashBrother.getCalabash(j+1).getPos_x(),
                        Global.calabashBrother.getCalabash(j+1).getPos_y());
            }
            records[7] = new CreatureRecord(7,Global.grandFather.getPos_x(),
                    Global.grandFather.getPos_y());
            for(int j = 0; j < 7; j++){
                records[8+j] = new CreatureRecord(8+j,Global.monsters.getMonster(j+1).getPos_x(),
                        Global.monsters.getMonster(j+1).getPos_y());
            }
            records[15] = new CreatureRecord(15,Global.scorpion.getPos_x(),Global.scorpion.getPos_y());
            records[16] = new CreatureRecord(16,Global.snake.getPos_x(),Global.snake.getPos_y());
        }
    }

    //record list:(rounder: num,posx,posy,num,posx,poy,...）
    List<RoundRecord> roundList = new ArrayList<RoundRecord>();

    //record file list
    public BufferedReader reader = null;

    /* -Initialize- */

    public BattleRecord(){
        roundList.clear();
    }

    /* -methods- */

    //add a record with round i;
    public void addRecord(int i){
        RoundRecord roundRecord = new RoundRecord(i);
        roundList.add(roundRecord);
    }

    //save record
    public void saveFile(Stage stage){
        /*
        if(roundList == null)
            return;
        */
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("choose save dic");
        File file = fileChooser.showSaveDialog(stage);

        System.out.println(file.getPath());

        FileOutputStream fileOutputStream = null;
        if(file != null){
            try {
                fileOutputStream = new FileOutputStream(file);
                String identify = Global.identify+"\n";
                fileOutputStream.write(identify.getBytes());
                for(int i = 0; i < roundList.size(); i++){
                    String rounder = Integer.toString(roundList.get(i).rounder)+"\n";
                    String calabashStr = "";
                    for(int j = 0; j < 8; j++){
                        calabashStr+= Integer.toString(roundList.get(i).records[j].num);
                        calabashStr+=" ";
                        calabashStr+=Integer.toString(roundList.get(i).records[j].pos_x);
                        calabashStr+=" ";
                        calabashStr+=Integer.toString(roundList.get(i).records[j].pos_y);
                        calabashStr+=" ";
                    }
                    calabashStr+="\n";
                    String monsterStr = "";
                    for(int j = 8; j < 17; j++){
                        monsterStr+=Integer.toString(roundList.get(i).records[j].num);
                        monsterStr+=" ";
                        monsterStr+=Integer.toString(roundList.get(i).records[j].pos_x);
                        monsterStr+=" ";
                        monsterStr+=Integer.toString(roundList.get(i).records[j].pos_y);
                        monsterStr+=" ";
                    }
                    monsterStr+="\n";
                    fileOutputStream.write(rounder.getBytes());
                    fileOutputStream.write(calabashStr.getBytes());
                    fileOutputStream.write(monsterStr.getBytes());
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }


    }

    //delete record
    public void clear(){
        this.roundList.clear();
    }

    //get record
    public void readFile(Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("choose play file");
        File file = fileChooser.showOpenDialog(stage);
        System.out.println(file.getPath());

        if(file == null)
            return;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr = reader.readLine();
            if(!tempStr.equals(Global.identify)) {
                System.out.println("uncorrect file");
                return;
            }
            showRecord(reader);

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static int showRecord(BufferedReader reader) throws IOException{
        String tempStr;
        int rounder = 0;
        if ((tempStr=reader.readLine())!= null){

            Global.map.clearMap();
            rounder = Integer.parseInt(tempStr);
            //System.out.println(tempStr);
            tempStr = reader.readLine();
            //System.out.println(tempStr);
            String[] temp1 = tempStr.split("\\s");
            for(int i = 0; i < 21; i+=3){
                int x = Integer.parseInt(temp1[i+1]);
                int y = Integer.parseInt(temp1[i+2]);
                if(x != 0 || y!= 0) {
                    Global.map.setPosition(x,y,
                            Global.calabashBrother.getCalabash(Integer.parseInt(temp1[i])+1));
                }
            }
            //System.out.println(1);
            int gra_x = Integer.parseInt(temp1[22]);
            int gra_y = Integer.parseInt(temp1[23]);
            if(gra_x != 0 || gra_y != 0){
                Global.map.setPosition(gra_x,gra_y,Global.grandFather);
            }

            tempStr = reader.readLine();
            String[] temp2 = tempStr.split("\\s");
            for(int i = 0; i < 21; i+=3){
                int x = Integer.parseInt(temp2[i+1]);
                int y = Integer.parseInt(temp2[i+2]);
                if(x!=0||y!=0){
                    Global.map.setPosition(x,y,Global.monsters.getMonster(Integer.parseInt(temp2[i])-7));
                }
            }

            int scor_x = Integer.parseInt(temp2[22]);
            int scor_y = Integer.parseInt(temp2[23]);
            if(scor_x != 0 || scor_y != 0){
                Global.map.setPosition(scor_x,scor_y,Global.scorpion);
            }

            int snake_x = Integer.parseInt(temp2[25]);
            int snake_y = Integer.parseInt(temp2[26]);
            if(snake_x != 0 || snake_y != 0){
                Global.map.setPosition(snake_x,snake_y,Global.snake);
            }
            GUIWindow.repaintMap();

        }
        else {
            GUIWindow.displayFlag = false;
        }
        return rounder;
    }
}
