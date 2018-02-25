package com.dylan.kuwoSpider.dao;

import com.dylan.kuwoSpider.BlogMapper.BlogMapper;
import com.dylan.kuwoSpider.MusicBean;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SqlHelp {
    public static void insert(List<MusicBean> musicBeanList) throws IOException {
        String resource = "mybatis.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            // do work
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            for (MusicBean musicBean:musicBeanList) mapper.insertMusic(musicBean);
            session.commit();
        }catch (Exception e){
            e.printStackTrace();
        }

        finally {
            session.close();
        }
    }
}
