import com.dylan.kuwoSpider.BlogMapper.BlogMapper;
import com.dylan.kuwoSpider.BlogMapper.GetMusicData;
import com.dylan.kuwoSpider.MusicBean;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SqlTest {

    public static void test() throws IOException {
        String resource = "mybatis.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            // do work

                BlogMapper mapper = session.getMapper(BlogMapper.class);
                MusicBean musicBean =new MusicBean();
                musicBean.setMp3("http://123.207.215.205/music/music/yangcong.mp3");
                musicBean.setTitle("测试");
                musicBean.setCover("http://123.207.215.205/music/bgpic/bg.jpg");
                musicBean.setArtist("慕哥哥");
                musicBean.setDuration("100");
                musicBean.setBackground("http://123.207.215.205/music/bgpic/bg.jpg");
                System.out.println("mapper"+mapper);
                System.out.println("musicBean"+musicBean);
                mapper.insertMusic(musicBean);
                session.commit();

        } finally {
            session.close();
        }
    }

    public static void main(String[] args) {
        try {
            test();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
