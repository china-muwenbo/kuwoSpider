package com.dylan.kuwoSpider.parse;

import com.dylan.kuwoSpider.MusicBean;
import com.dylan.kuwoSpider.data.DataMusic;
import com.dylan.kuwoSpider.parse.HtmlFetcher;
import com.dylan.kuwoSpider.utils.SaveUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class KuwoHtmlParse {

    /*
    <div class="name">
 <a href="/yinyue/7149583" target="_blank">告白气球</a>
</div>
<div class="artist">
 <a href="/artist/content?name=周杰伦">周杰伦</a>
</div>
<div class="heat">
 <div class="heatValue" style="width:86%"></div>
</div>
<div class="listRight">
 <div class="tools" data-music="{&quot;id&quot;:&quot;MUSIC_7149583&quot;,&quot;name&quot;:&quot;告白气球&quot;,&quot;artist&quot;:&quot;周杰伦&quot;,&quot;album&quot;:&quot;周杰伦的床边故事&quot;,&quot;pay&quot;:&quot;16515324&quot;}" style="display:block;">
  <div class="tips" style="visibility: hidden"></div>
  <a class="play playSongArtist" href="javascript:;"></a>
  <a class="add" href="javascript:;"></a>
  <a class="down click_log" data-down="http://down.kuwo.cn/mbox/kwmusic_web_3.exe" data-click="artistcontent_list" href="javascript:;"></a>
  <div class="share" title="分享">
   <div class="shareBox">
    <div class="sharecontent">
     <a href="javascript:;" class="quickShare new_sina" data-cmd="tsina" title="分享到新浪微博"></a>
     <a href="javascript:;" class="quickShare new_qzone" data-cmd="qzone" title="分享到QQ空间"></a>
     <a href="javascript:;" class="quickShare new_tqq" data-cmd="tqq" title="分享到腾讯微博"></a>
     <a href="javascript:;" class="quickShare new_douban" data-cmd="douban" title="分享到豆瓣网"></a>
     <a href="javascript:;" class="quickShare new_renren" data-cmd="renren" title="分享到人人网"></a>
    </div>
   </div>
  </div>
 </div>
</div>

    *
    */
    private HashMap<String, String> hashMap = new HashMap<String, String>();
    /*
     * 解析主方法 先解析 出歌手 等信息 歌曲可以通过id获取
     * */
    public int parse(String url) {
        int count=0;
        try {
            List<MusicBean> musicBeanList = new ArrayList<MusicBean>();
            String html = HtmlFetcher.fetch(url);
            Document document = Jsoup.parse(html);
            Elements playlists = document.select(".listMusic");
            int currentPage = Integer.parseInt(playlists.attr("data-pn"));
            count = Integer.parseInt(playlists.attr("data-page"));
        }catch (Exception e){
            System.out.println("解析电影总数出错");
        }
        return count;
    }

    public List<MusicBean> getMusicData(Elements playlists) {
        List<MusicBean> musicBeanList = new ArrayList<MusicBean>();
        Elements musics = playlists.select(".onLine");
        for (Element element : musics) {
            MusicBean musicBean = new MusicBean();
            //"id":"MUSIC_118990","name":"发如雪","artist":"周杰伦","album":"十一月的萧邦","pay":"16515324"}
            String data = element.select(".tools").attr("data-music");
            DataMusic dataMusic;
            try{
                dataMusic = DataMusic.objectFromData(data);
            }catch (Exception e){
                return new ArrayList<MusicBean>();
            }
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
    public List<MusicBean> getAllMusicUrl(String url) {
        return getPage(url);
    }
    //按page索引获取一个歌手的某一页的数据页面
    public String getSongerListurl(String artistId,String pn) {
        String url = "http://www.kuwo.cn/artist/contentMusicsAjax?artistId="+artistId+"&pn=" + pn + "&rn=15";
        return  url;
    }

    //从一个数据页面获取所有歌曲信息
    public List<MusicBean> getPage(String url) {
        try{
            String html = HtmlFetcher.fetch(url);
            if (html==null){
                SaveUtils.save("fail.txt",url);
                return new ArrayList<MusicBean>();
            }
            Document document = Jsoup.parse(html);
            Elements playlists = document.select(".listMusic");
            return getMusicData(playlists);
        }catch (Exception e){
            System.out.println("获取页面信息出错");
        }
        return new ArrayList<MusicBean>();
    }

    /*
     * 从另一个网页获取 歌手图片url
     * */
    public String getJpg(String subfix) {
        String jpgurl="http://image.kuwo.cn/www2016/logo.jpg";
        try {
            String prefix = "http://www.kuwo.cn";
            String url = prefix + subfix;
            if (hashMap.get(url) != null) return hashMap.get(url);
            System.out.println(url);
            // <img id="artist_Image" src="http://image.kuwo.cn/website/pc/default/50-50.jpg" onerror="imgOnError(this,'50')" />
            String html = HtmlFetcher.fetch(url);
            Document document = Jsoup.parse(html);
            Elements elements = document.select(".artistTop img");
            hashMap.put(url, elements.attr("data-src"));
            return elements.attr("data-src");
        }catch (Exception e){
            System.out.println("解析图片出错");
        }
        return jpgurl;

    }

    //获取音乐url 重定向后的
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

    //获取原始音乐sourceUrl 未重定向
    public String getMusicUrl(String muiscId) {
        return "http://antiserver.kuwo.cn/anti.s?format=mp3&rid=" + muiscId + "&type=convert_url&response=res";
    }

    //从名字获取歌手id
    public String getArtistIdFromSongerName(String artistName){
        String id=null;
        try {
            String name = "http://www.kuwo.cn/artist/content?name="+artistName;
//        <div class="artistTop" data-artistid="336">
            String html = HtmlFetcher.fetch(name);
            Document document = Jsoup.parse(html);
            Elements playlists = document.select(".artistTop");
            String attr=playlists.attr("data-artistid");
            System.out.println(attr);
            return attr;
        }catch (Exception e){

        }
       return null;

    }
    //爬取所有歌手
    //http://www.kuwo.cn/artist/indexAjax?category=0&prefix=&pn=0
    public List<String > getSongerName(int i){
        try {
            String name = "http://www.kuwo.cn/artist/indexAjax?category=0&prefix=&pn="+i;
//        <div class="artistTop" data-artistid="336">
            String html = HtmlFetcher.fetch(name);
            Document document = Jsoup.parse(html);
            Elements elements= document.select(".a_name");
            List<String > list= new ArrayList<String>();
            for (Element element:elements)
                list.add(element.text());
            return list;
        }catch (Exception e){

        }
       return  new ArrayList<String>();
    }


}
