import com.dylan.kuwoSpider.parse.KuwoHtmlParse;
import com.dylan.kuwoSpider.parse.KuwoHtmlParseN;
import com.dylan.kuwoSpider.MusicBean;
import com.dylan.kuwoSpider.dao.SqlHelp;

import java.io.IOException;
import java.util.List;

public class Main {
    public static final String url = "http://www.kuwo.cn/artist/content?name=%E5%91%A8%E6%9D%B0%E4%BC%A6";

    /**
     * @Description: 主程序 总共有7000页 从0-7000 至今按页读取歌手
     * @Param: * @param args
     * @return: void
     */
    public static void main(String[] args) {
        KuwoHtmlParse kuwoHtmlParse = new KuwoHtmlParse();
        for (int i = 413; i < 7000; i++) {
            List<String> list = kuwoHtmlParse.getSongerName(i);
            for (String s : list) {
                System.out.println("正在爬取第" + i + "页，的" + s);
                String id = kuwoHtmlParse.getArtistIdFromSongerName(s);
                if (id != null) spiderFor(id);
            }
        }
    }

    //递归爬取
    public void spiderResursion() {
        new KuwoHtmlParseN().startDown();
    }
    //for 循环读取
    public static void spiderFor(String artistId) {
        KuwoHtmlParse kuwoHtmlParse = new KuwoHtmlParse();
        String firstUrl = kuwoHtmlParse.getSongerListurl(artistId, 0 + "");
        int count = kuwoHtmlParse.parse(firstUrl);
        for (int i = 0; i < count; i++) {
            String url = kuwoHtmlParse.getSongerListurl(artistId, i + "");
            System.out.println(url);
            List<MusicBean> musicBeans = kuwoHtmlParse.getAllMusicUrl(url);
            System.out.println("正在读取:" + i + "页面");
            try {
                SqlHelp.insert(musicBeans);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
