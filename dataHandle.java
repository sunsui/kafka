import java.io.*;
public class Main {
	public static void readTxtFile(String filePath){
        try {
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
               InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file));
               BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                int data=0;
                while((lineTxt = bufferedReader.readLine()) != null){
                	String tmp = lineTxt.replaceAll("\"","");
                	writeTxtFile(tmp);
                    data += 1;
                }
                System.out.println();
                System.out.println("文件数量："+data);
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }
    public static void writeTxtFile(String txt){
        String writePath = "F:\\test1.txt";
        File file = new File(writePath);
        FileWriter writer;
        try {
            writer = new FileWriter(file, true);
            writer.write(txt);
            writer.write("\r\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String argv[]){
        String filePath = "F:\\201407data.csv";
        readTxtFile(filePath);
    }
}