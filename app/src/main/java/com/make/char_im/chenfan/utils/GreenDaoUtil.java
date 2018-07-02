package com.make.char_im.chenfan.utils;

import com.make.char_im.chenfan.greenDao.DisplayMess;
import com.make.char_im.chenfan.greenDao.DisplayMessDao;
import com.make.char_im.chenfan.greenDao.MyPhrase;
import com.make.char_im.chenfan.greenDao.MyPhraseDao;

import java.util.ArrayList;

/**
 * 数据库增删改查工具类
 * Created by chen on 2017/4/13.
 */

public class GreenDaoUtil {

    //增
    public static void insertData(DisplayMessDao dao, DisplayMess displayBean) {
        dao.insert(displayBean);
    }

    //删
    public static void deleteData(DisplayMessDao dao,Long id){
        dao.deleteByKey(id);
    }

    //改
    public static void updateData(DisplayMessDao dao, DisplayMess displayBean) {
        dao.update(displayBean);
    }

    //查
    public static ArrayList<DisplayMess> queryData(DisplayMessDao dao) {
        ArrayList<DisplayMess> displayMesses = (ArrayList<DisplayMess>) dao.loadAll();
        return displayMesses;
    }



    //增
    public static void insertPhraseData(MyPhraseDao dao, MyPhrase displayBean) {
        dao.insert(displayBean);
    }

    //删
    public static void deletePhraseData(MyPhraseDao dao,Long id){
        dao.deleteByKey(id);
    }

    //改
    public static void updatePhraseData(MyPhraseDao dao, MyPhrase displayBean) {
        dao.update(displayBean);
    }

    //查
    public static ArrayList<MyPhrase> queryPhraseData(MyPhraseDao dao) {
        ArrayList<MyPhrase> displayMesses = (ArrayList<MyPhrase>) dao.loadAll();
        return displayMesses;
    }




}
