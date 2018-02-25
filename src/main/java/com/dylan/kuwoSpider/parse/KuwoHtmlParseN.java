package com.dylan.kuwoSpider.parse;

import com.dylan.kuwoSpider.MusicBean;
import com.dylan.kuwoSpider.dao.SqlHelp;
import com.dylan.kuwoSpider.data.DataMusic;
import com.dylan.kuwoSpider.parse.HtmlFetcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
* 递归爬虫
* */
public class KuwoHtmlParseN {

    private HashMap<String, String> hashMap = new HashMap<String, String>();
    /*
     * 解析主方法 先解析 出歌手 等信息 歌曲可以通过id获取
     * */

    public int parse(String url) {
        List<MusicBean> musicBeanList = new ArrayList<MusicBean>();
        String html = HtmlFetcher.fetch(url);
        Document document = Jsoup.parse(html);
        Elements playlists = document.select(".listMusic");
        int currentPage = Integer.parseInt(playlists.attr("data-pn"));
        int count = Integer.parseInt(playlists.attr("data-page"));
        return count;
    }
    /**
     * @Description: 递归下载
     * @Param:  * @param url
     * @return: void
     */
    public void  downLoad(String url){
        String html = HtmlFetcher.fetch(url);
        Document document = Jsoup.parse(html);
        Elements playlists = document.select(".listMusic");
        List<MusicBean> musicBeans = getMusicData(playlists);
        try {
            SqlHelp.insert(musicBeans);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int currentPage = Integer.parseInt(playlists.attr("data-pn"));
        int count = Integer.parseInt(playlists.attr("data-page"));
        if (currentPage==count)return;
        System.out.println("下载："+currentPage);
        currentPage++;
        downLoad(getNextUrl(""+currentPage));
    }
    public void startDown(){
        downLoad(getNextUrl("0"));
    }
    /**
     * @Description: 获取音乐信息
     * @Param:  * @param playlists
     * @return: java.util.List<com.dylan.kuwoSpider.MusicBean>
     */
    public List<MusicBean> getMusicData(Elements playlists) {
        List<MusicBean> musicBeanList = new ArrayList<MusicBean>();
        Elements musics = playlists.select(".onLine");
        for (Element element : musics) {
            MusicBean musicBean = new MusicBean();
            //"id":"MUSIC_118990","name":"发如雪","artist":"周杰伦","album":"十一月的萧邦","pay":"16515324"}
            String data = element.select(".tools").attr("data-music");
            DataMusic dataMusic = DataMusic.objectFromData(data);
            musicBean.setArtist(dataMusic.getArtist());
            musicBean.setTitle(dataMusic.getName());
            String str = element.select(".artist").select("a").attr("href");
            String jpg = getJpg(str);
            musicBean.setCover(jpg);
            String mp3 = getMusicUrl(dataMusic.getId());
            musicBean.setMp3(mp3);
            //System.out.println(musicBean.toString());
            musicBeanList.add(musicBean);
        }
        return musicBeanList;
    }

    //按page索引获取一个歌手的某一页的数据页面
    public List<MusicBean> getAllMusicUrl(String pn) {
        String url = "http://www.kuwo.cn/artist/contentMusicsAjax?artistId=336&pn=" + pn + "&rn=15";
        return getPage(url);
    }
    /**
     * @Description: 通过pn获取下一页NextUrl
     * @Param:  * @param pn
     * @return: java.lang.String
     */
    public String getNextUrl(String pn){
        return  "http://www.kuwo.cn/artist/contentMusicsAjax?artistId=336&pn=" + pn + "&rn=15";
    }

    //从一个数据页面获取所有歌曲信息
    public List<MusicBean> getPage(String url) {
        String html = HtmlFetcher.fetch(url);
        Document document = Jsoup.parse(html);
        Elements playlists = document.select(".listMusic");
        return getMusicData(playlists);
    }
    /**
     * @Description: 爬取歌手图片
     * @Param:  * @param subfix
     * @return: java.lang.String
     */
    public String getJpg(String subfix) {
        String prefix = "http://www.kuwo.cn";
        String url = prefix + subfix;
        if (hashMap.get(url) != null) return hashMap.get(url);
        System.out.println(url);
        // <img id="artist_Image" src="http://image.kuwo.cn/website/pc/default/50-50.jpg" onerror="imgOnError(this,'50')" />
        String html = HtmlFetcher.fetch(url);
        Document document = Jsoup.parse(html);
        Elements elements = document.select("img#artist_Image");
        for (Element element : elements) {
            hashMap.put(url, element.attr("src"));
            return element.attr("src");
        }
        return null;
    }
    /**
     * @Description: 获取
     * @Param:  * @param url
     * @return: java.lang.String
     */
    public String getMusicSourceUrl(String url) {
        try {
            String musicmp3 = HtmlFetcher.fetchMusic(url);
            System.out.println(musicmp3);
            return musicmp3;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取原始sourceUrl 未重定向
    public String getMusicUrl(String muiscId) {
        return "http://antiserver.kuwo.cn/anti.s?format=mp3&rid=" + muiscId + "&type=convert_url&response=res";
    }

}
