import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author YangMingYang
 * @since 2022/10/5
 */
public class Test {

    public static void main(String[] args) {
        String repositoryUrl="https://gitee.com/YangMingyang123/demo.git";
//        boolean res=repositoryUrl.contains("https://");
//        System.out.println(repositoryUrl);
        String url=repositoryUrl.split("https://")[1];
        System.out.println(url);
    }

    public static Process exec(String cmd) throws IOException {
        String[] cmdArray=cmd.split("\\s+");
        return Runtime.getRuntime().exec(cmdArray);
    }

    public static String execAndConvert(String cmd){
        InputStreamReader isr = null;
        BufferedReader br=null;
        InputStreamReader errorIsr = null;
        BufferedReader errorBre=null;
        StringBuilder info= new StringBuilder();
        StringBuilder error= new StringBuilder();
        try {
            //执行命令
            Process process= exec(cmd);
            isr = new InputStreamReader(process.getInputStream());
            br = new BufferedReader(isr);
            String line=br.readLine();
            while(line!=null){
                info.append(line).append("\n");
                line=br.readLine();
            }

            errorIsr = new InputStreamReader(process.getErrorStream());
            errorBre = new BufferedReader(errorIsr);
            line=errorBre.readLine();
            while(line!=null){
                error.append(line).append("\n");
                line=errorBre.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(isr!=null){
                    isr.close();
                }
                if(br!=null){
                    br.close();
                }
                if(errorIsr!=null){
                    errorIsr.close();
                }
                if(errorBre!=null){
                    errorBre.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(info);
        System.out.println(error);
        return info.toString();
    }
}
