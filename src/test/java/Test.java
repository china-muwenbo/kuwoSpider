import com.dylan.kuwoSpider.parse.KuwoHtmlParse;
import com.dylan.kuwoSpider.MusicBean;

import java.util.List;

public class Test {

    //"http://www.kuwo.cn/artist/contentMusicsAjax?artistId=336&pn=2&rn=15
    //周杰伦
    public static final String url="http://www.kuwo.cn/artist/content?name=%E5%91%A8%E6%9D%B0%E4%BC%A6";

    public static void main(String[] args) {
        int count =new KuwoHtmlParse().parse(url);
        System.out.println(count);
    }
    @org.junit.Test
    public void getJpg(){
        String  url="/artist/content?name=大壮";
        String jpg=new KuwoHtmlParse().getJpg(url);
        System.out.println("jpg:"+jpg);
    }
    @org.junit.Test
    public void getMusic()  {
        String id ="MUSIC_40079875";
        new KuwoHtmlParse().getMusicUrl(id);
    }
    @org.junit.Test
    public void getNextMusic(){
        String url ="http://www.kuwo.cn/artist/contentMusicsAjax?artistId=336&pn=2&rn=15";
        List<MusicBean> musicBeans= new KuwoHtmlParse().getPage(url);
        for (MusicBean musicBean:musicBeans){
            System.out.println(musicBean.toString());
        }
    }
    @org.junit.Test
    public void getArtistId(){
        List<String> list=   new KuwoHtmlParse().getSongerName(0);

        System.out.println(list.toArray().length);
    }


}
